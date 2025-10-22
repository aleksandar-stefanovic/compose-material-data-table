package io.github.aleksandar_stefanovic.composematerialdatatable

internal actual fun String.format(int: Int): String = String.format(this, int)

internal actual fun String.format(float: Float): String = String.format(this, float)

internal actual fun String.format(double: Double): String = String.format(this, double)