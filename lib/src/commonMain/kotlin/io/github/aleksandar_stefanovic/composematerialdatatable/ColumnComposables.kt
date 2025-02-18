package io.github.aleksandar_stefanovic.composematerialdatatable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Checkbox
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.material.TriStateCheckbox
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format.DateTimeFormat

@Composable
internal fun TextHeader(
    headerText: String,
    horizontalArrangement: Arrangement.Horizontal,
    sortOrder: SortOrder? = null,
    onClick: () -> Unit
) {
    Row(
        Modifier.background(Color.White).clickable { onClick() }
            .padding(16.dp),
        horizontalArrangement,
        Alignment.CenterVertically
    ) {
        if (horizontalArrangement == Arrangement.Start) {
            Text(headerText, fontWeight = FontWeight.SemiBold)
        }

        when (sortOrder) {
            SortOrder.ASC -> Image(Icons.Default.KeyboardArrowUp, "Ascending sorting")
            SortOrder.DESC -> Image(Icons.Default.KeyboardArrowDown, "Descending sorting")
            null -> {
                // Keep an invisible icon to reserve column space
                Image(Icons.Default.KeyboardArrowUp, "Ascending sorting", Modifier.alpha(0f))
            }
        }

        if (horizontalArrangement == Arrangement.End) {
            Text(headerText, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
internal fun CheckboxHeader(state: ToggleableState, onClick: () -> Unit) {
    TriStateCheckbox(state, onClick, Modifier.background(Color.White))
}

private val defaultCellModifier = Modifier.background(Color.White).padding(16.dp)


@Composable
internal fun TextCell(text: String, textAlign: TextAlign) {
    Text(text, defaultCellModifier, textAlign = textAlign)
}

@Composable
internal fun IntCell(int: Int, numberFormat: String? = null, textAlign: TextAlign) {
    val stringValue = numberFormat?.format(int) ?: int.toString()
    Text(
        stringValue,
        defaultCellModifier,
        textAlign = textAlign,
    )
}

@Composable
internal fun DoubleCell(double: Double, numberFormat: String? = null, textAlign: TextAlign) {
    Text(
        numberFormat?.format(double) ?: double.toString(),
        defaultCellModifier,
        textAlign = textAlign
    )
}

@Composable
internal fun DateCell(date: LocalDate, dateFormat: DateTimeFormat<LocalDate>, textAlign: TextAlign) {
    Text(dateFormat.format(date), defaultCellModifier, textAlign = textAlign)
}

@Composable
internal fun CheckboxCell(selected: Boolean, onClick: (Boolean) -> Unit) {
    Checkbox(selected, onClick, Modifier.background(Color.White))
}

@Composable
internal fun <T, S : Comparable<S>> DropdownCell(
    spec: DropdownColumnSpec<T, S>,
    rowData: T,
) {
    var expanded by remember { mutableStateOf(false) }

    Box(Modifier.clickable { expanded = true }.background(Color.White).padding(16.dp)) {
        Text(spec.valueFormatter(spec.valueSelector(rowData)))
        DropdownMenu(expanded, onDismissRequest = { expanded = false }) {
            spec.choices.forEach { choice ->
                DropdownMenuItem(onClick = {
                    spec.onChoicePicked(choice)
                    expanded = false
                }) {
                    Text(spec.valueFormatter(choice))
                }
            }
        }
    }
}