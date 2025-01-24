package io.github.aleksandarstefanovic.composematerialdatatable

import kotlinx.datetime.LocalDate
import kotlinx.datetime.toJavaLocalDate
import java.time.format.DateTimeFormatter

actual fun String.format(int: Int): String = String.format(this, int)

actual fun LocalDate.format(format: String): String {
    val localDate = this.toJavaLocalDate()
    return DateTimeFormatter.ofPattern(format).format(localDate)
}

actual fun String.format(float: Float): String = String.format(this, float)

actual fun String.format(double: Double): String = String.format(this, double)