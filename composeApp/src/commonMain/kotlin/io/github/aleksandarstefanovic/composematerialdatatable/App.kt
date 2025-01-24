package io.github.aleksandarstefanovic.composematerialdatatable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.ui.tooling.preview.Preview


data class SampleDataClass(
    val aString: String,
    val aInt: Int,
    val aFloat: Double,
    val aDouble: Double,
    val aDate: LocalDate,
    val aBoolean: Boolean
)

val sampleData = listOf(
    SampleDataClass(
        "First entry",
        1,
        1.23,
        4.56,
        LocalDate(2021, 1, 20),
        true
    ),
    SampleDataClass(
        "Second entry",
        2,
        7.89,
        0.12,
        LocalDate(2025, 12, 28),
        false
    ),
    SampleDataClass(
        "Third entry",
        3,
        3.45,
        6.78,
        LocalDate(2024, 5, 15),
        true
    )
)

@Composable
@Preview
fun App(tableModifier: Modifier = Modifier) {
    MaterialTheme {
        val columnSpecs = listOf<ColumnSpec<SampleDataClass, *>>(
            TextColumnSpec("Text", WidthSetting.WrapContent) { it.aString },
            IntColumnSpec("Int", WidthSetting.WrapContent, { it.aInt }),
            DateColumnSpec("Date", WidthSetting.WrapContent, { it.aDate }, "MM/dd/YYYY"),
            DoubleColumnSpec("Double", WidthSetting.WrapContent, valueSelector = { it.aDouble }),
            CheckboxColumnSpec("Checkbox", WidthSetting.WrapContent) { it.aBoolean }
        )

        Column {

            var selectedCount by remember { mutableStateOf(0) }

            Table(
                columnSpecs,
                sampleData,
                modifier = tableModifier.padding(10.dp),
                showSelectionColumn = true,
                onSelectionChange = { list -> selectedCount = list.size }
            )

            if (selectedCount > 0) {
                Text("Selected: $selectedCount")
            }

        }


    }
}

enum class SortOrder {
    ASC, DESC
}

@Composable
fun <T> Table(
    columnSpecs: List<ColumnSpec<T, *>>,
    data: List<T>,
    modifier: Modifier = Modifier,
    showSelectionColumn: Boolean = false,
    onSelectionChange: (Set<T>) -> Unit = {},
) {
    val totalFlexWeight =
        columnSpecs.map { it.widthSetting }.filterIsInstance<WidthSetting.Flex>().map { it.weight }
            .sum()

    val columnSpecsNormalized = columnSpecs.map {
        if (it.widthSetting is WidthSetting.Flex) {
            val weight = it.widthSetting.weight / totalFlexWeight
            it.copy(widthSetting = WidthSetting.Flex(weight))
        } else {
            it
        }
    }

    var sortedColumnSpec: ColumnSpec<*, *>? by remember { mutableStateOf(null) }
    var columnSortOrder by remember { mutableStateOf(SortOrder.ASC) }

    val onHeaderClick = remember<(columnSpec: ColumnSpec<*, *>) -> Unit> {
        { columnIndex ->
            if (columnIndex !== sortedColumnSpec) {
                sortedColumnSpec = columnIndex
                columnSortOrder = SortOrder.ASC
            } else {
                if (columnSortOrder == SortOrder.ASC) {
                    columnSortOrder = SortOrder.DESC
                } else if (columnSortOrder == SortOrder.DESC) {
                    // Deactivate sorting
                    sortedColumnSpec = null
                }
            }
        }
    }

    val sortedData by remember(data, sortedColumnSpec) {
        derivedStateOf {
            if (sortedColumnSpec != null) {
                if (columnSortOrder == SortOrder.DESC) {
                    data.sortedBy {
                        @Suppress("UNCHECKED_CAST") // It is known that `valueSelector` always produces Comparable values
                        (sortedColumnSpec!!.valueSelector as (T) -> Comparable<Any>).invoke(it)
                    }
                } else {
                    data.sortedByDescending {
                        @Suppress("UNCHECKED_CAST") // It is known that `valueSelector` always produces Comparable values
                        (sortedColumnSpec!!.valueSelector as (T) -> Comparable<Any>).invoke(it)
                    }
                }
            } else data
        }
    }

    var selectedData by remember { mutableStateOf<Set<T>>(setOf()) }

    SideEffect {
        onSelectionChange(selectedData)
    }

    val headerSelectionCheckboxState by remember(selectedData) {
        derivedStateOf {
            when (selectedData.size) {
                0 -> ToggleableState.Off
                data.size -> ToggleableState.On
                else -> ToggleableState.Indeterminate
            }
        }
    }

    val onHeaderSelectionClick = remember {
        {
            selectedData = if (headerSelectionCheckboxState == ToggleableState.On) {
                emptySet()
            } else {
                data.toSet()
            }
        }
    }

    val onBodySelectionClick: (dataItem: T) -> Unit = remember {
        { index ->
            if (index !in selectedData) {
                selectedData += index
            } else {
                selectedData -= index
            }
        }
    }

    val headerRowComposableLambda: @Composable () -> Unit = {
        if (showSelectionColumn) {
            CheckboxHeader(headerSelectionCheckboxState, onHeaderSelectionClick)
        }
        columnSpecsNormalized.map { columnSpec ->
            val sortOrder = if (columnSpec == sortedColumnSpec) columnSortOrder else null

            TextHeader(
                columnSpec.headerName,
                columnSpec.horizontalArrangement,
                sortOrder,
                onClick = { onHeaderClick(columnSpec) }
            )
        }
    }

    val composableLambdasByRow: List<@Composable () -> Unit> = sortedData.map { rowData ->
        // One lambda per row (will become List<List<Measurable>> in the Layout composable)
        return@map {
            if (showSelectionColumn) {
                CheckboxCell(rowData in selectedData) { onBodySelectionClick(rowData) } // TODO
            }
            columnSpecsNormalized.forEach { columnSpec ->

                val textAlign = when (columnSpec.horizontalArrangement) {
                    Arrangement.Start -> TextAlign.Start
                    Arrangement.End -> TextAlign.End
                    else -> TextAlign.Center
                }

                when (columnSpec) {
                    is TextColumnSpec -> TextCell(columnSpec.valueSelector(rowData), textAlign)
                    is IntColumnSpec -> IntCell(columnSpec.valueSelector(rowData), columnSpec.numberFormat, textAlign)
                    is DoubleColumnSpec -> DoubleCell(columnSpec.valueSelector(rowData), columnSpec.numberFormat, textAlign)
                    is DateColumnSpec -> DateCell(columnSpec.valueSelector(rowData), columnSpec.dateFormat, textAlign)
                    is CheckboxColumnSpec -> CheckboxCell(columnSpec.valueSelector(rowData), onClick = { }) // TODO
                }
            }
        }
    }

    // Header is appended to body rows in order to calculate the column width together
    val headerAndBodyRowComposables: List<@Composable () -> Unit> = listOf(headerRowComposableLambda) + composableLambdasByRow

    val tableModifier = modifier.clip(RoundedCornerShape(4.dp)).background(Color(0x1f000000))

    SelectionContainer {
        Layout(
            headerAndBodyRowComposables,
            tableModifier
        ) { measurablesByRow: List<List<Measurable>>, constraints ->

            val totalColumnCount = columnSpecsNormalized.size + if (showSelectionColumn) 1 else 0

            // By spec, it should be 1.dp, but it doesn't work as intended when converted to px, TODO find an elegant solution
            val borderPx = 1
            val totalHorizontalPadding = 2 * borderPx
            val totalVerticalPadding = (measurablesByRow.size + 1) * borderPx

            val availableWidth = constraints.maxWidth - totalHorizontalPadding

            val columnWidthsInitial: List<Int> = (0..<totalColumnCount).map { colIndex ->
                // Account for the selection column
                val widthSetting = if (showSelectionColumn) {
                    if (colIndex == 0) {
                        WidthSetting.WrapContent
                    } else {
                        columnSpecs[colIndex - 1].widthSetting
                    }
                } else {
                    columnSpecs[colIndex].widthSetting
                }

                when (widthSetting) {
                    is WidthSetting.Flex -> 0 // Will be set at a later point
                    is WidthSetting.Static -> widthSetting.width.roundToPx()
                    WidthSetting.WrapContent -> {
                        // Iterate over all the measurables in this row, find the tallest one
                        measurablesByRow.mapIndexed { index, rowMeasurables ->
                            val targetHeight =
                                if (index == 0) 56.dp else 52.dp // Per Material 2 specs
                            rowMeasurables[colIndex].maxIntrinsicWidth(targetHeight.roundToPx())
                        }.max()
                    }
                }
            }

            val remainingWidth = availableWidth - columnWidthsInitial.sum()

            // Calculate width of the flex columns based on the remaining width
            val columnWidths = (columnWidthsInitial).mapIndexed { colIndex, width ->
                val widthSetting = if (showSelectionColumn) {
                    if (colIndex == 0) {
                        WidthSetting.WrapContent
                    } else {
                        columnSpecsNormalized[colIndex - 1].widthSetting
                    }
                } else {
                    columnSpecsNormalized[colIndex].widthSetting
                }
                if (widthSetting is WidthSetting.Flex) {
                    (remainingWidth * widthSetting.weight).toInt()
                } else {
                    width
                }
            }

            val placeablesByRow: List<List<Placeable>> =
                measurablesByRow.mapIndexed { rowIndex, rowMeasurables ->
                    val targetHeight = (if (rowIndex == 0) 56.dp else 52.dp).roundToPx()
                    rowMeasurables.mapIndexed { colIndex, measurable ->
                        val columnWidth = columnWidths[colIndex]
                        val cellConstraints = constraints.copy(
                            minWidth = columnWidth,
                            maxWidth = columnWidth,
                            minHeight = targetHeight,
                            maxHeight = targetHeight
                        )
                        measurable.measure(cellConstraints)
                    }
                }


            val tallestCellHeightByRow = placeablesByRow.map { rowPlaceables ->
                rowPlaceables.maxOf { it.height }
            }

            val widestCellByColumn = (0..<totalColumnCount).map { colIndex ->
                placeablesByRow.maxOf { rowPlaceables ->
                    rowPlaceables[colIndex].width
                }
            }

            val tableHeight =
                (tallestCellHeightByRow.sum() + totalVerticalPadding).coerceAtMost(constraints.maxHeight)
            val tableWidth =
                (widestCellByColumn.sum() + totalHorizontalPadding).coerceAtMost(constraints.maxWidth)

            layout(tableWidth, tableHeight) {
                var yPosition = borderPx

                placeablesByRow.forEachIndexed { rowIndex, rowPlaceables ->
                    var xPosition = borderPx
                    rowPlaceables.forEach { placeable ->
                        placeable.placeRelative(xPosition, yPosition)
                        xPosition += placeable.width
                    }
                    yPosition += tallestCellHeightByRow[rowIndex] + borderPx
                }
            }
        }
    }
}