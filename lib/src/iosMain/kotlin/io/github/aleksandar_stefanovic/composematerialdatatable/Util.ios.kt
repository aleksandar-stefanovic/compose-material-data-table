package io.github.aleksandar_stefanovic.composematerialdatatable

actual fun String.format(int: Int): String = int.toString()

actual fun String.format(float: Float): String = float.toString()

actual fun String.format(double: Double): String = double.toString()