package io.github.aleksandar_stefanovic.composematerialdatatable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.YearMonth
import kotlinx.datetime.todayIn
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

private val tileSize = 24.dp

@OptIn(ExperimentalTime::class)
@Composable
public fun DatePickerModal() { // TODO change visibility

    var currentLocalDate by remember { mutableStateOf(Clock.System.todayIn(TimeZone.currentSystemDefault())) }

    val startOfMonthOffset = dayOfWeekToOffset(currentLocalDate.dayOfWeek)

    Card {
        Column {
            Row(Modifier.height(52.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(currentLocalDate.month.name + " " + currentLocalDate.year)
                Button({}) {
                    Text("Previous")
                }
                Button({}) {
                    Text("Next")
                }
            }
            FlowRow(Modifier.width(tileSize * 7)) {
                DayTile("M")
                DayTile("T")
                DayTile("W")
                DayTile("T")
                DayTile("F")
                DayTile("S")
                DayTile("S")
                repeat(startOfMonthOffset - 1) {
                    DayTile(null, false)
                }
                for (i in 1..currentLocalDate.daysInMonth()) {
                    DayTile(i.toString(), currentLocalDate.day == i)
                }
            }

        }
    }
}

@Composable
private fun DayTile(label: String?, selected: Boolean = false) {
    Box(Modifier.size(tileSize)) {
        if (label != null) {
            Text(
                label,
                Modifier.matchParentSize(),
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
[DayOfWeek.ordinal] could technically be used, but could cause issues in the future if the order of
the enum values changes.
*/
private fun dayOfWeekToOffset(dayOfWeek: DayOfWeek): Int {
    // TODO expand for locales which don't start on Monday
    return when (dayOfWeek) {
        DayOfWeek.MONDAY -> 0
        DayOfWeek.TUESDAY -> 1
        DayOfWeek.WEDNESDAY -> 2
        DayOfWeek.THURSDAY -> 3
        DayOfWeek.FRIDAY -> 4
        DayOfWeek.SATURDAY -> 5
        DayOfWeek.SUNDAY -> 6
    }
}

private fun LocalDate.daysInMonth() = YearMonth(this.year, this.month).numberOfDays