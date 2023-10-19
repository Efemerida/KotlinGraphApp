package com.example.graphonkotlin.views

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.net.Uri
import android.text.InputType
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AlertDialog
import com.example.graphonkotlin.R
import com.example.graphonkotlin.adapters.ActionsAdapter
import com.example.graphonkotlin.entities.Edge
import com.example.graphonkotlin.entities.Graph
import com.example.graphonkotlin.entities.Vertex
import com.example.graphonkotlin.utils.States
import com.example.graphonkotlin.utils.Utils
import java.util.function.BinaryOperator
import kotlin.math.abs

class DrawView(context: Context?) : View(context) {

        var currEdge: Edge? = null
        var vertexTmp: Vertex? = null



        var touchX = 0f
        var touchY = 0f

        var txt: EditText? = null

        var bitmap: Bitmap
        var all: Bitmap

        var vertexes: MutableList<Vertex> = ArrayList<Vertex>()
        var edges: MutableList<Edge> = ArrayList<Edge>()

        var myCanvas: Canvas

        val paintVertex = Paint()
        val paintText = Paint()
        val paintLine = Paint()

        init {
            val config = Bitmap.Config.ARGB_8888
            bitmap = Bitmap.createBitmap(100, 100, config)
            bitmap.eraseColor(Color.BLACK)
            all = Bitmap.createBitmap(1000, 1000, config)
            myCanvas = Canvas(all)

            paintVertex.color = Color.RED
            paintVertex.isAntiAlias = true

            paintText.color = Color.YELLOW
            paintText.textSize = 70f

            paintLine.color = Color.RED
            paintLine.strokeWidth = 23f
        }

        fun collectInput(edt: EditText?): String {
            if (edt != null) {
                return edt.getText().toString();
            }
            return TODO("Provide the return value")
        }

        fun collectAndVerifyIntInput(edt: EditText?, edge: Edge?) {
            val ref = collectInput(edt)
            Log.d("errors", " ref is$ref")
            if (ref == null || ref.trim { it <= ' ' } == "" || ref.trim { it <= ' ' } == "0") {
                Toast.makeText(context, R.string.remind_weight, Toast.LENGTH_SHORT)
            } else {
                if (edge != null) {
                    edge.weight = ref.toInt()
                };
            }
        }

        @SuppressLint("DrawAllocation")
        override fun onDraw(canvas: Canvas) {
            for (vertex in vertexes) {
                canvas.drawCircle(vertex.x, vertex.y, 50f, paintVertex)
            }


            var textCoords: List<Int>


            for (edge in edges) {
                canvas.drawLine(
                    edge.vertex1!!.x,
                    edge.vertex1!!.y,
                    edge.vertex2!!.x,
                    edge.vertex2!!.y,
                    paintLine
                )
            }

            val offset = 20

            for (vertex in vertexes) {
                canvas.drawText(
                    "" + vertex.number,
                    vertex.x -
                            if (vertex.number >= 10) offset * (1 + (Utils.digitInNumber(vertex.number) - 1) / 10) else 0,
                    vertex.y + offset,
                    paintText
                )
            }


            paintText.textSize = 100f
            for (edge in edges) {
                textCoords = edge.getPlaceForWeight()
                canvas.drawText(
                    "" + edge.weight,
                    textCoords[0].toFloat(),
                    textCoords[1].toFloat(),
                    paintText
                )
            }

            val textView = TextView(this.context)
            textView.setTextColor(Color.BLACK)
            textView.draw(canvas)
        }

        fun clear() {
            edges.clear()
            vertexes.clear()
            invalidate()
        }

        fun loadGraph(graph: Graph) {
            vertexes.clear()
            edges.clear()
            edges.addAll(graph.edgeList)
            vertexes.addAll(graph.vertexList)
            invalidate()
        }

        override fun onTouchEvent(event: MotionEvent): Boolean {

            if (event.getAction() == MotionEvent.ACTION_DOWN) {

                if (ActionsAdapter.currentStates.equals(States.ADD_POINT)) {
                    touchX = event.getX()
                    touchY = event.getY()
                    var number = 1

                    if (!vertexes.isEmpty()) {
                        number = vertexes.stream()
                            .reduce(BinaryOperator<Vertex> {
                                    x: Vertex, y: Vertex -> if (x.number > y.number) x else y
                            })
                            .get().number + 1
                    }

                    vertexes.add(Vertex(touchX, touchY, 50, number))
                    invalidate()

                }



                ////////////////////////////////////////////////////////////////////////////
                else if (ActionsAdapter.currentStates.equals(States.DELETE_POINT)) {
                    var tmp: Vertex? = null

                    for (vertex in vertexes) {
                        if (abs(vertex.x - event.x) <= vertex.radius) {
                            if (abs(vertex.y - event.y) <= vertex.radius) {
                                tmp = vertex
                            }
                        }
                    }

                    if (tmp != null) {
                        var tmpEdge: Edge? = null
                        var i = 0

                        while (i < edges.size) {
                            if (edges[i].vertex1!! == tmp || edges[i].vertex2!! == tmp) {
                                edges.removeAt(i--)
                            }
                            i++
                        }

                        for (edge in edges) {
                            if (edge.vertex1!! == tmp || edge.vertex2!! == tmp) {
                                tmpEdge = edge
                            }
                        }

                        if (tmpEdge != null) {
                            edges.remove(tmpEdge)
                        }
                        vertexes.remove(tmp)
                    }
                    invalidate()
                }


                /////////////////////////////////////////////////////////////
                else if (ActionsAdapter.currentStates.equals(States.ADD_LINE)) {
                    var tmp: Vertex? = null

                    for (vertex in vertexes) {
                        if (abs(vertex.x - event.x) <= vertex.radius) {
                            if (abs(vertex.y - event.y) <= vertex.radius) {
                                tmp = vertex
                            }
                        }
                    }

                    if (tmp != null) {
                        if (currEdge == null) {
                            currEdge = Edge(tmp)
                        }

                        else {
                            if (tmp == currEdge!!.vertex1) return true

                            val alertWeight = AlertDialog.Builder(this.context)
                            val edt = EditText(this.context)
                            edt.inputType = InputType.TYPE_CLASS_NUMBER
                            alertWeight.setTitle(R.string.alert_to_add_weight_title)
                            alertWeight.setView(edt)
                            val layoutalert = LinearLayout(this.context)
                            layoutalert.setOrientation(LinearLayout.VERTICAL)
                            layoutalert.addView(edt)
                            alertWeight.setView(layoutalert)
                            currEdge!!.vertex2 = tmp
                            alertWeight.setPositiveButton(
                                R.string.accept_rus
                            ) { dialog, which ->
                                txt = edt
                                collectAndVerifyIntInput(txt, currEdge)
                                edges.add(currEdge!!)
                                currEdge = null
                                invalidate()
                            }
                            alertWeight.setNegativeButton(
                                R.string.deny_rus
                            ) { dialog, which ->
                                dialog.cancel()
                                currEdge = null
                                invalidate()
                            }
                            alertWeight.show()
                        }
                    }
                }


                ////////////////////////////////////////////////////////////////////
                else if (ActionsAdapter.currentStates.equals(States.DELETE_LINE)) {
                    var tmp: Vertex? = null

                    for (vertex in vertexes) {
                        if (abs(vertex.x - event.x) <= vertex.radius) {
                            if (abs(vertex.y - event.y) <= vertex.radius) {
                                tmp = vertex
                            }
                        }
                    }

                    if (tmp != null) {
                        var tmpEdge: Edge? = null
                        for (edge in edges) {
                            if (edge.vertex1!! == tmp || edge.vertex2!! == tmp) {
                                if (currEdge == null) {
                                    currEdge = Edge(tmp)
                                } else {
                                    if (edge.vertex1 == tmp) {
                                        if (edge.vertex2 == currEdge!!.vertex1) {
                                            tmpEdge = edge
                                        }
                                    }
                                    if (edge.vertex2 == tmp) {
                                        if (edge.vertex1== currEdge!!.vertex1) {
                                            tmpEdge = edge
                                        }
                                    }
                                }
                            }
                        }

                        if (tmpEdge != null) {
                            edges.remove(tmpEdge)
                            currEdge = null
                            invalidate()
                        }
                    }

                }


                ////////////////////////////////////////////////////////////////
                else if (ActionsAdapter.currentStates.equals(States.CHECK_POINT)) {
                    var tmp: Vertex? = null

                    for (vertex in vertexes) {
                        if (abs(vertex.x - event.x) <= vertex.radius) {
                            if (abs(vertex.y - event.y) <= vertex.radius) {
                                tmp = vertex
                            }
                        }
                    }

                    if (tmp != null) {
                        if (vertexTmp == null) {
                            vertexTmp = tmp
                        } else if (vertexTmp === tmp) {
                            return true
                        } else {
                            if (graph.isAdjacent(vertexTmp!!, tmp)) Toast.makeText(
                                context, "Вершины смежные", Toast.LENGTH_LONG
                            ).show() else Toast.makeText(
                                context, "Вершины не смежные", Toast.LENGTH_LONG
                            ).show()
                            vertexTmp = null
                        }
                    }
                }


                ///////////////////////////////////////////////////////////////////
                else if (ActionsAdapter.currentStates.equals(States.CHECK_WEIGHT)) {
                    var tmp: Vertex? = null

                    for (vertex in vertexes) {
                        if (abs(vertex.x - event.x) <= vertex.radius) {
                            if (abs(vertex.y - event.y) <= vertex.radius) {
                                tmp = vertex
                            }
                        }
                    }

                    if (tmp != null) {
                        if (vertexTmp == null) {
                            vertexTmp = tmp
                        } else if (vertexTmp === tmp) {
                            return true
                        } else {
                            val weight: Int = graph.getWeight(vertexTmp!!, tmp)
                            if (weight == 0) Toast.makeText(
                                context,
                                "Ребра не существует",
                                Toast.LENGTH_LONG
                            ).show() else Toast.makeText(
                                context, weight.toString(), Toast.LENGTH_LONG
                            ).show()
                            vertexTmp = null
                        }
                    }
                }
            }
            return true
        }

        val graph: Graph
            get() {
                val graph = Graph()
                for (edge in edges) {
                    graph.edgeList.add(edge)
                }
                for (vertex in vertexes) {
                    graph.vertexList.add(vertex)
                }
                return graph
            }

        companion object {
            var currEdge: Edge? = null
            var vertexTmp: Vertex? = null
            var launcher: ActivityResultLauncher<Array<String>>? = null
            var launcherSave: ActivityResultLauncher<String>? = null
            var pathSave: Uri? = null
        }
    }