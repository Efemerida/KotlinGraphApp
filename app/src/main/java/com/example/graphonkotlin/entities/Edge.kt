package com.example.graphonkotlin.entities

import androidx.compose.ui.text.font.FontWeight

data class Edge(var vertex1: Vertex? = null, var vertex2: Vertex? = null, var weight: Int = 0) {


    fun getPlaceForWeight(): List<Int> {
            val res: MutableList<Int> = ArrayList()
            val offset = 70
            val forCalc = offset /
                    Math.pow(
                        Math.pow((vertex1!!.y - vertex2!!.y).toDouble(), 2.0) +
                                Math.pow((vertex2!!.x - vertex1!!.x).toDouble(), 2.0), 0.5
                    )
            val coord1 =
                ((vertex2!!.x + vertex1!!.x) / 2 - forCalc * (vertex1!!.y - vertex2!!.y)).toInt()
            val coord2 =
                ((vertex2!!.y + vertex1!!.y) / 2 - forCalc * (vertex2!!.x - vertex1!!.x)).toInt()
            res.add(coord1)
            res.add(coord2)
            return res
        }

    override fun toString(): String {
        return "Edge{" +
                "vertex1=" + vertex1 +
                ", vertex2=" + vertex2 +
                ", weight=" + weight +
                '}'
    }
}