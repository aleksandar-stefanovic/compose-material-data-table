package io.github.aleksandarstefanovic.composematerialdatatable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.material.TriStateCheckbox
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.datetime.LocalDate

@Composable
fun TextHeader(
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
fun CheckboxHeader(state: ToggleableState, onClick: () -> Unit) {
    TriStateCheckbox(state, onClick, Modifier.background(Color.White))
}

private val defaultCellModifier = Modifier.background(Color.White).padding(16.dp)


@Composable
fun TextCell(text: String, textAlign: TextAlign) {
    Text(text, defaultCellModifier, textAlign = textAlign)
}

@Composable
fun IntCell(int: Int, numberFormat: String? = null, textAlign: TextAlign) {
    val stringValue = numberFormat?.format(int) ?: int.toString()
    Text(
        stringValue,
        defaultCellModifier,
        textAlign = textAlign,
    )
}

@Composable
fun DoubleCell(double: Double, numberFormat: String? = null, textAlign: TextAlign) {
    Text(
        numberFormat?.format(double) ?: double.toString(),
        defaultCellModifier,
        textAlign = textAlign
    )
}

@Composable
fun DateCell(date: LocalDate, dateFormat: String, textAlign: TextAlign) {
    Text(date.format(dateFormat), defaultCellModifier, textAlign = textAlign)
}

@Composable
fun CheckboxCell(selected: Boolean, onClick: (Boolean) -> Unit) {
    Checkbox(selected, onClick, Modifier.background(Color.White))
}