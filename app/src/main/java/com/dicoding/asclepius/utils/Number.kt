package com.dicoding.asclepius.utils

class Number {
    companion object {
        fun decimalToPercentage(decimal: Float): String{
            val percentage =(decimal * 100).toInt().toString() + "%"
            return percentage
        }
    }
}