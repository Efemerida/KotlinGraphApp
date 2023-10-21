package com.example.graphonkotlin.ui.entities

import android.util.Log
import android.view.View
import com.example.graphonkotlin.ui.BlankFragment

data class Graph(var edgeList: MutableList<Edge> = ArrayList<Edge>(), var vertexList: MutableList<Vertex> = ArrayList<Vertex>()) {
    override fun toString(): String {
        return "Graph{" +
                "edgeList=" + edgeList +
                ", vertices=" + vertexList +
                '}'
    }

    val countEdge: Int
        get() = edgeList.size
    val countVertex: Int
        get() = vertexList.size

    fun isAdjacent(vertex1: Vertex, vertex2: Vertex): Boolean {
        for ((vertex11, vertex21) in edgeList) {
            if (vertex11 == vertex1 && vertex21 == vertex2 || vertex21 == vertex1 && vertex11 == vertex2) return true
        }
        return false
    }

    fun getEdgeByAdjacent(vertex1: Vertex, vertex2: Vertex): Edge? {
        for (edge in edgeList) {
            if (edge.vertex1 == vertex1 && edge.vertex2 == vertex2 || edge.vertex2 == vertex1 && edge.vertex1 == vertex2) return edge
        }
        return null
    }

    fun getWeight(vertex1: Vertex, vertex2: Vertex): Int {
        val edge = getEdgeByAdjacent(vertex1, vertex2)
        return edge?.weight ?: 0
    }

    companion object {
        fun loadGraph(string: String): Graph {
            val graph = Graph()
            val view: View? = BlankFragment.viewTmp
            val width: Int = view!!.width
            val height: Int = view.height
            val edges = string.split("\n")

            var i =0;
            var size = edges.size-1
            for (edge in edges) {
                val edgeTmp = Edge()
                val edgeStr = edge.split(" ")
                if(i==size) break
                Log.d("taggg", "str is " + edges.size + " " + i)
                var vertex1 = Vertex(edgeStr[0].toInt())
                var vertex2 = Vertex(edgeStr[1].toInt())

                for (vertex in graph.vertexList) {
                    if (vertex.number == vertex1.number) {
                        vertex1 = vertex
                    }
                    if (vertex.number == vertex2.number) {
                        vertex2 = vertex
                    }
                }


                var w = (Math.random() * (width - 1) + 1).toInt()
                var h = (Math.random() * (height - 1) + 1).toInt()

                vertex1.x = w.toFloat()
                vertex1.y = h.toFloat()
                vertex1.radius = 50

                graph.vertexList.add(vertex1)

                w = (Math.random() * (width - 1) + 1).toInt()
                h = (Math.random() * (height - 1) + 1).toInt()

                vertex2.x = w.toFloat()
                vertex2.y = h.toFloat()
                vertex2.radius = 50

                graph.vertexList.add(vertex2)

                edgeTmp.vertex1 = vertex1
                edgeTmp.vertex2 = vertex2
                edgeTmp.weight = edgeStr[2].toInt()
                graph.edgeList.add(edgeTmp)
                i++
            }
            return graph
        }
    }
}