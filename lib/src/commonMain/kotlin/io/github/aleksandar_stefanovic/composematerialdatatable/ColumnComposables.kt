package io.github.aleksandar_stefanovic.composematerialdatatable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.DisableSelection
import androidx.compose.material.Checkbox
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TriStateCheckbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.github.aleksandar_stefanovic.composematerialdatatable.icons.ArrowDownward
import io.github.aleksandar_stefanovic.composematerialdatatable.icons.ArrowUpward
import io.github.aleksandar_stefanovic.composematerialdatatable.icons.DataTableIcons
import io.github.aleksandar_stefanovic.composematerialdatatable.icons.Edit
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
            Text(
                headerText,
                fontWeight = FontWeight.SemiBold,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }

        when (sortOrder) {
            SortOrder.ASC -> Image(
                imageVector = DataTableIcons.ArrowUpward,
                contentDescription = "Ascending sorting",
                modifier = Modifier.alpha(0.5f)
            )
            SortOrder.DESC -> Image(
                imageVector = DataTableIcons.ArrowDownward,
                contentDescription = "Descending sorting",
                modifier = Modifier.alpha(0.5f)
            )
            null -> {
                // Keep an invisible icon to reserve column space
                Image(
                    imageVector = DataTableIcons.ArrowUpward,
                    contentDescription = "Ascending sorting",
                    modifier = Modifier.alpha(0f)
                )
            }
        }

        if (horizontalArrangement == Arrangement.End) {
            Text(
                headerText,
                fontWeight = FontWeight.SemiBold,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }
    }
}

@Composable
internal fun CheckboxHeader(state: ToggleableState, onClick: () -> Unit) {
    TriStateCheckbox(state, onClick, Modifier.background(Color.White))
}

// Approximation of what the color is when the surface is white and the hovered element is clickable
// Ideally should be a Material token, but this will suffice for now
private val hoverColor = Color(0xfff3f3f3)

@Composable
internal fun TextCell(
    text: String,
    textAlign: TextAlign,
    interactionSource: MutableInteractionSource,
    rowIndex: Int,
    onOpenEditModal: ((rowIndex: Int, positionInRoot: Offset) -> Unit)? = null
) {
    val isHovered by interactionSource.collectIsHoveredAsState()
    val backgroundColor = if (isHovered) hoverColor else Color.White

    var positionInRoot: Offset = remember { Offset.Zero }

    Row(Modifier.hoverable(interactionSource).background(backgroundColor).padding(16.dp).onGloballyPositioned {
        positionInRoot = it.positionInParent()
    }) {
        Text(
            text,
            textAlign = textAlign,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
        if (isHovered && onOpenEditModal != null) {
            IconButton({ onOpenEditModal(rowIndex, positionInRoot) }) {
                Image(Edit, "Dropdown")
            }
        }
    }
}

@Composable
internal fun IntCell(
    int: Int,
    formatToString: (Int) -> String,
    textAlign: TextAlign,
    interactionSource: MutableInteractionSource,
    rowIndex: Int,
    onOpenEditModal: ((rowIndex: Int, positionInRoot: Offset) -> Unit)? = null
) {
    val stringValue = formatToString(int)
    TextCell(
        stringValue,
        textAlign,
        interactionSource,
        rowIndex,
        onOpenEditModal
    )
}

@Composable
internal fun DoubleCell(
    double: Double,
    formatToString: (Double) -> String,
    textAlign: TextAlign,
    interactionSource: MutableInteractionSource,
    rowIndex: Int,
    onOpenEditModal: ((rowIndex: Int, positionInRoot: Offset) -> Unit)? = null
) {
    TextCell(
        formatToString(double),
        textAlign,
        interactionSource,
        rowIndex,
        onOpenEditModal
    )
}

@Composable
internal fun DateCell(
    date: LocalDate,
    dateFormat: DateTimeFormat<LocalDate>,
    textAlign: TextAlign,
    interactionSource: MutableInteractionSource
) {

    val isHovered by interactionSource.collectIsHoveredAsState()
    val backgroundColor = if (isHovered) hoverColor else Color.White

    Text(
        dateFormat.format(date),
        Modifier.hoverable(interactionSource).background(backgroundColor).padding(16.dp),
        textAlign = textAlign,
        overflow = TextOverflow.Ellipsis,
        maxLines = 1
    )
}

@Composable
internal fun CheckboxCell(
    selected: Boolean,
    interactionSource: MutableInteractionSource,
    rowIndex: Int,
    onClick: (rowIndex: Int, newValue: Boolean) -> Unit
) {

    val isHovered by interactionSource.collectIsHoveredAsState()
    val backgroundColor = if (isHovered) hoverColor else Color.White

    Checkbox(
        selected,
        onCheckedChange = { newValue -> onClick(rowIndex, newValue) },
        Modifier.hoverable(interactionSource).background(backgroundColor).padding(16.dp)
    )
}

@Composable
internal fun <T, S : Comparable<S>> DropdownCell(
    spec: DropdownColumnSpec<T, S>,
    rowData: T,
    rowIndex: Int,
    interactionSource: MutableInteractionSource
) {
    val isHovered by interactionSource.collectIsHoveredAsState()
    val backgroundColor = if (isHovered) hoverColor else Color.White

    var expanded by remember { mutableStateOf(false) }

    val modifier = Modifier.hoverable(interactionSource).background(backgroundColor).padding(16.dp).let {
        if (spec.onEdit != null) {
            it.clickable { expanded = true }
        } else {
            it
        }
    }

    Box(modifier) {
        Text(spec.valueFormatter(spec.valueSelector(rowData)))
        // This is necessary because the DropdownCell is wrapped with a SelectionContainer
        DisableSelection {
            DropdownMenu(expanded, onDismissRequest = { expanded = false }) {
                spec.choices.forEach { choice ->
                    DropdownMenuItem(onClick = {
                        spec.onEdit?.let { it(rowIndex, choice) }
                        expanded = false
                    }) {
                        Text(spec.valueFormatter(choice))
                    }
                }
            }
        }
    }
}