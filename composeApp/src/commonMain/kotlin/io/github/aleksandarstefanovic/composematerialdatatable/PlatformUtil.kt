package io.github.aleksandarstefanovic.composematerialdatatable

import kotlinx.datetime.LocalDate

expect fun String.format(int: Int): String

expect fun String.format(float: Float): String

expect fun String.format(double: Double): String

expect fun LocalDate.format(format: String): String