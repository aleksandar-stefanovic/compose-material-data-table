package io.github.aleksandarstefanovic.composematerialdatatable

actual fun String.format(float: Float): String = float.toString()

actual fun String.format(double: Double): String = double.toString()

actual fun String.format(int: Int): String = int.toString()
