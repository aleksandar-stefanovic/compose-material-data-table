package io.github.aleksandarstefanovic.composematerialdatatable

import kotlinx.datetime.LocalDate

actual fun LocalDate.format(format: String): String {
    return "${this.dayOfMonth}.${this.month}.${this.year}."
}

actual fun String.format(float: Float): String {
    return float.toString()
}

actual fun String.format(double: Double): String {
    return double.toString()
}