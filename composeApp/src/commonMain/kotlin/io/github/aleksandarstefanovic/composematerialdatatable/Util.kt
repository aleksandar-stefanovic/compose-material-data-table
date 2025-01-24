package io.github.aleksandarstefanovic.composematerialdatatable

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun LocalDate.now(): LocalDate {
    val instant = Clock.System.now()
    val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    return LocalDate(
        localDateTime.year,
        localDateTime.month,
        localDateTime.dayOfMonth
    )
}