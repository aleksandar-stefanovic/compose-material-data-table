package io.github.aleksandarstefanovic.composematerialdatatable

import kotlinx.datetime.LocalDate

actual fun LocalDate.format(format: String): String {
    return "${this.dayOfMonth}.${this.month}.${this.year}."
}

actual fun String.format(float: Float): String = float.toString()

actual fun String.format(double: Double): String = double.toString()

actual fun String.format(int: Int): String = int.toString()
