package com.example.graphonkotlin.ui

import android.text.InputFilter
import android.text.Spanned

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
    class InputFilterMinMax: InputFilter {
        private var min:Int = 0
        private var max:Int = 0
        constructor(min:Int, max:Int) {
            this.min = min
            this.max = max
        }
        constructor(min:String, max:String) {
            this.min = Integer.parseInt(min)
            this.max = Integer.parseInt(max)
        }
        override fun filter(source:CharSequence, start:Int, end:Int, dest: Spanned, dstart:Int, dend:Int): CharSequence? {
            try
            {
                val input = Integer.parseInt(dest.toString() + source.toString())
                if (isInRange(min, max, input))
                    return null
            }
            catch (nfe:NumberFormatException) {}
            return ""
        }
        private fun isInRange(a:Int, b:Int, c:Int):Boolean {
            return if (b > a) c in a..b else c in b..a
        }
    }
}