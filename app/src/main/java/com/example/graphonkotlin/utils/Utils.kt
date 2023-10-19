package com.example.graphonkotlin.utils

object Utils {
    fun digitInNumber(number: Int): Int {
        return if (number < 100000) {
            if (number < 100) {
                if (number < 10) {
                    1
                } else {
                    2
                }
            } else {
                if (number < 1000) {
                    3
                } else {
                    if (number < 10000) {
                        4
                    } else {
                        5
                    }
                }
            }
        } else {
            if (number < 10000000) {
                if (number < 1000000) {
                    6
                } else {
                    7
                }
            } else {
                if (number < 100000000) {
                    8
                } else {
                    if (number < 1000000000) {
                        9
                    } else {
                        10
                    }
                }
            }
        }
    }
}