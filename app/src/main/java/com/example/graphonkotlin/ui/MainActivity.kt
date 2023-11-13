package com.example.graphonkotlin.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.text.Spanned
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.graphonkotlin.R
import com.example.graphonkotlin.ui.adapters.ActionsAdapter
import com.example.graphonkotlin.ui.entities.Edge
import com.example.graphonkotlin.ui.entities.Graph
import com.example.graphonkotlin.ui.entities.Vertex
import com.example.graphonkotlin.ui.services.FIleService
import java.lang.Math.abs
import java.util.function.BinaryOperator

class MainActivity : AppCompatActivity() {


    companion object{
        lateinit var pathSave: Uri
        lateinit var launcher : ActivityResultLauncher<Array<String>>
        lateinit var launcherSave : ActivityResultLauncher<String>

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_class)

        launcher = registerForActivityResult<Array<String>, Uri>(
            ActivityResultContracts.OpenDocument(), ActivityResultCallback<Uri> { url: Uri? ->
                val fIleService = FIleService(this)
                val view  = BlankFragment.viewTmp
                val graphCode: String = fIleService.loadGraph(url!!, this)
                        view?.loadGraph(Graph.loadGraph(graphCode))
            }
        )
        launcherSave = registerForActivityResult<String, Uri>(
            ActivityResultContracts.CreateDocument(), ActivityResultCallback<Uri> { url: Uri ->
                val fIleService = FIleService(this)
                val view  = BlankFragment.viewTmp
                fIleService.saveGraph( view!!.graph, url)
                pathSave = url
            }
        )


        var recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val drawable1: Drawable? = getDrawable(R.drawable.addpoint)
        val drawable2: Drawable? = getDrawable(R.drawable.addline)
        val drawable3: Drawable? = getDrawable(R.drawable.deleteline)
        val drawable4: Drawable? = getDrawable(R.drawable.deletepoint)
        val drawable5: Drawable? = getDrawable(R.drawable.clear)
        val drawable6: Drawable? = getDrawable(R.drawable.upload)
        val drawable7: Drawable? = getDrawable(R.drawable.download)
        val drawable8: Drawable? = getDrawable(R.drawable.sumpoints)
        val drawable9: Drawable? = getDrawable(R.drawable.sumlines)
        val drawable10: Drawable? = getDrawable(R.drawable.hasconnection)
        val drawable11: Drawable? = getDrawable(R.drawable.wt)
        val drawable12: Drawable? = getDrawable(R.drawable.path)
        val imageViews: MutableList<Drawable?> = ArrayList()
        imageViews.add(drawable1)
        imageViews.add(drawable2)
        imageViews.add(drawable3)
        imageViews.add(drawable4)
        imageViews.add(drawable5)
        imageViews.add(drawable6)
        imageViews.add(drawable7)
        imageViews.add(drawable8)
        imageViews.add(drawable9)
        imageViews.add(drawable10)
        imageViews.add(drawable11)
        imageViews.add(drawable12)


        val onStateClickListener: ActionsAdapter.OnStateClickListener = object : ActionsAdapter.OnStateClickListener{
            override fun onStateClick(position: Int) {
                ActionsAdapter.currentStates = States.getState(position)!!
            }
        }
        recyclerView.adapter = ActionsAdapter(this, imageViews, onStateClickListener)

    }





    class myView (context: Context) : View(context){
        private val paint = Paint()
        var currEdge: Edge? = null
        var vertexTmp: Vertex? = null
        var launcher: ActivityResultLauncher<Array<String>>? = null
        var launcherSave: ActivityResultLauncher<String>? = null
        var pathSave: Uri? = null


//        override fun onDraw(canvas: Canvas?) {
//            super.onDraw(canvas)
//
//            paint.color = Color.RED
//            paint.style = Paint.Style.FILL
//
//            // Рисуем круг на Canvas
//            canvas!!.drawCircle(width / 2f, height / 2f, 100f, paint)
//
//            // Устанавливаем цвет и стиль для Paint
//            paint.color = Color.BLUE
//            paint.style = Paint.Style.STROKE
//            paint.strokeWidth = 10f
//
//            // Рисуем прямоугольник на Canvas
//            canvas!!.drawRect(50f, 50f, 200f, 200f, paint)
//        }



        var touchX = 0f
        var touchY = 0f

        var txt: EditText? = null

        var bitmap: Bitmap
        var all: Bitmap

        var vertexesToDraw: MutableList<Vertex> = ArrayList<Vertex>()
        var edgeToDraw: MutableList<Edge> = ArrayList<Edge>()

        var vertexes: MutableList<Vertex> = ArrayList<Vertex>()
        var edges: MutableList<Edge> = ArrayList<Edge>()

        var myCanvas: Canvas

        val paintVertex = Paint()
        val paintText = Paint()
        val paintLine = Paint()
        val paintTextNumber = Paint()
        val paintPath = Paint()
        val paintPathVertex = Paint()

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

            paintTextNumber.color = Color.YELLOW
            paintTextNumber.textSize = 50f

            paintLine.color = Color.RED
            paintLine.strokeWidth = 23f

            paintPathVertex.color = Color.BLUE
            paintPathVertex.isAntiAlias = true

            paintPath.color = Color.BLUE
            paintPath.strokeWidth = 20f
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

        override fun onDraw(canvas: Canvas) {
            Log.d("tagg","ssss draw");


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

            val offset = 30

            for (vertex in vertexes) {
                if(vertex.number!=-1){
                    canvas.drawText(
                        "" + vertex.number,
                        vertex.x -
                                if (vertex.number >= 10) offset * (1 + (Utils.digitInNumber(vertex.number) - 1) / 10) else 0,
                        vertex.y + offset,
                        paintTextNumber
                    )
                }

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
            for (vertex in vertexesToDraw){
                canvas.drawCircle(vertex.x, vertex.y, 48f, paintPathVertex)
            }
            for (edge in edgeToDraw) {
                canvas.drawLine(
                    edge.vertex1!!.x,
                    edge.vertex1!!.y,
                    edge.vertex2!!.x,
                    edge.vertex2!!.y,
                    paintPath
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


                if (ActionsAdapter.currentStates == States.ADD_POINT) {
                    touchX = event.x
                    touchY = event.y
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
                }else if(ActionsAdapter.currentStates.equals(States.CHECK_PATH) and !vertexes.isEmpty()){

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
        fun drawPath(edt: EditText?) {
            val vertexamount = edt?.text.toString().toInt()
            val vertexNumArray = (1..vertexes.count()).shuffled().take(vertexamount).toList()
            vertexesToDraw = vertexes.filter { vertex -> vertexNumArray.contains(vertex.number) }.toMutableList()

            for (i in 0 until vertexesToDraw.size-1){
                edgeToDraw.add(Edge(vertexesToDraw.get(i),vertexesToDraw.get(i+1)))
            }

        }
        fun checkPath(){
            for (edge1 in edgeToDraw){
                val tocheck=edges.filter { edge -> (edge.vertex1?.number==edge1.vertex1?.number && edge.vertex2?.number==edge1.vertex2?.number) ||
                        (edge.vertex2?.number== edge1.vertex1?.number &&edge.vertex1?.number==edge1.vertex2?.number)}
                if(tocheck.isEmpty()){
                    Toast.makeText(
                        context,
                        "Не часть пути",
                        Toast.LENGTH_SHORT
                    ).show()
                    break
                }
            }
            Toast.makeText(
                context,
                "Часть пути",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}


