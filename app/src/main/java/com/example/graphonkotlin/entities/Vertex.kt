package com.example.graphonkotlin.entities

data class Vertex(var x: Float, var y:Float, var radius: Int, val number: Int) {

    constructor(rad: Int) : this(0f,0f,0,rad)

    override fun toString(): String {
        return "Vertex{" +
                "x=" + x +
                ", y=" + y +
                ", radius=" + radius +
                ", number=" + number +
                '}'
    }
}