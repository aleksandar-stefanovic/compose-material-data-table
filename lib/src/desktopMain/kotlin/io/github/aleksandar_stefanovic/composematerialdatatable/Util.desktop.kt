package io.github.aleksandar_stefanovic.composematerialdatatable

actual fun String.format(int: Int): String = String.format(this, int)

actual fun String.format(float: Float): String = String.format(this, float)

actual fun String.format(double: Double): String = String.format(this, double)