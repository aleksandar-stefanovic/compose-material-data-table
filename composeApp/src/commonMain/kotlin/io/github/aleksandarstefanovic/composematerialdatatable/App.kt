package io.github.aleksandarstefanovic.composematerialdatatable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.layout.IntrinsicMeasurable
import androidx.compose.ui.layout.IntrinsicMeasureScope
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.layout.MultiContentMeasurePolicy
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.ui.tooling.preview.Preview

enum class SampleState {
    INACTIVE, ACTIVE, PAUSED
}

data class SampleDataClass(
    val aString: String,
    val aInt: Int,
    val aFloat: Double,
    val aDouble: Double,
    val aDate: LocalDate,
    val aBoolean: Boolean,
    val aEnum: SampleState
)

val sampleData = listOf(
    SampleDataClass(
        "First entry",
        1,
        1.23,
        4.56,
        LocalDate(2021, 1, 20),
        true,
        SampleState.PAUSED
    ),
    SampleDataClass(
        "Second entry",
        2,
        7.89,
        0.12,
        LocalDate(2025, 12, 28),
        false,
        SampleState.ACTIVE
    ),
    SampleDataClass(
        "Third entry",
        3,
        3.45,
        6.78,
        LocalDate(2024, 5, 15),
        true,
        SampleState.INACTIVE
    )
)

@Composable
@Preview
fun App(modifier: Modifier = Modifier) {
    MaterialTheme {
        val columnSpecs = listOf<ColumnSpec<SampleDataClass, *>>(
            TextColumnSpec("Text", WidthSetting.WrapContent) { it.aString },
            IntColumnSpec("Int", WidthSetting.WrapContent, { it.aInt }),
            DateColumnSpec("Date", WidthSetting.WrapContent, { it.aDate }),
            DoubleColumnSpec("Double", WidthSetting.WrapContent, valueSelector = { it.aDouble }),
            DropdownColumnSpec("Dropdown", WidthSetting.WrapContent, { it.aEnum }, {
                when (it) {
                    SampleState.INACTIVE -> "Inactive"
                    SampleState.ACTIVE -> "Active"
                    SampleState.PAUSED -> "Paused"
                }
            }, listOf(SampleState.ACTIVE, SampleState.PAUSED, SampleState.INACTIVE), {}),
            CheckboxColumnSpec("Checkbox", WidthSetting.WrapContent) { it.aBoolean }
        )

        Column(modifier) {
            var selectedCount by remember { mutableStateOf(0) }

            Table(
                columnSpecs,
                sampleData,
                modifier = Modifier.padding(20.dp),
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

    var filters: List<ColumnFilter<T>> by remember { mutableStateOf(emptyList()) }

    val filteredSortedData by remember(data, filters, sortedColumnSpec) {
        derivedStateOf {
            // This is kinda inefficient, since the list is traversed for each filter, instead of just once
            val filteredData = filters.fold(data) { data: List<T>, filter ->
                filter.filter(data)
            }

            if (sortedColumnSpec != null) {
                if (columnSortOrder == SortOrder.DESC) {
                    filteredData.sortedBy {
                        @Suppress("UNCHECKED_CAST")
                        (sortedColumnSpec!!.valueSelector as (T) -> Comparable<Any>).invoke(it)
                    }
                } else {
                    filteredData.sortedByDescending {
                        @Suppress("UNCHECKED_CAST")
                        (sortedColumnSpec!!.valueSelector as (T) -> Comparable<Any>).invoke(it)
                    }
                }
            } else filteredData
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
                filteredSortedData.toSet()
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

    val composableLambdasByRow: List<@Composable () -> Unit> = filteredSortedData.map { rowData ->
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
                    is DropdownColumnSpec -> DropdownCell(columnSpec, rowData)
                }
            }
        }
    }

    // Header is appended to body rows in order to calculate the column width together
    // TODO headers should not figure into the column width, text should be truncated instead
    val headerAndBodyRowComposables: List<@Composable () -> Unit> = listOf(headerRowComposableLambda) + composableLambdasByRow

    Column(modifier.clip(RoundedCornerShape(4.dp)).background(Color(0x1f000000))) {

        FilterBar(
            Modifier.fillMaxWidth().background(Color.White),
            columnSpecsNormalized,
            filters,
            onFilterConfirm = { columnSpec, predicate, searchTerm ->
                filters += when (columnSpec) {
                    is TextColumnSpec -> StringFilter(columnSpec, predicate, searchTerm)
                    else -> TODO()
                }
            },
            onRemoveFilter = { filters -= it }
        )

        SelectionContainer {
            Layout(
                headerAndBodyRowComposables,
                measurePolicy = object : MultiContentMeasurePolicy {

                    val totalColumnCount =
                        columnSpecsNormalized.size + if (showSelectionColumn) 1 else 0

                    // By spec, it should be 1.dp, but it doesn't work as intended when converted to px, TODO find an elegant solution
                    val borderPx = 1
                    val totalHorizontalPadding = 2 * borderPx
                    val totalVerticalPadding = (headerAndBodyRowComposables.size + 1) * borderPx

                    override fun MeasureScope.measure(
                        // Each element in the outer list is a single row
                        measurables: List<List<Measurable>>,
                        constraints: Constraints
                    ): MeasureResult {

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
                                    measurables.mapIndexed { index, rowMeasurables ->
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
                            measurables.mapIndexed { rowIndex, rowMeasurables ->
                                val targetHeight = (if (rowIndex == 0) 56.dp else 52.dp).roundToPx()
                                rowMeasurables.mapIndexed { colIndex, measurable ->
                                    val columnWidth = columnWidths[colIndex]
                                    val cellConstraints = Constraints(
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

                        return layout(tableWidth, tableHeight) {
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

                    // There isn't really any functionality in the spec to define how the table
                    // should respond to the available width, so the min and max intrinsic heights
                    // are the same
                    override fun IntrinsicMeasureScope.maxIntrinsicHeight(
                        measurables: List<List<IntrinsicMeasurable>>,
                        width: Int
                    ) = minIntrinsicHeight(measurables, width)

                    override fun IntrinsicMeasureScope.minIntrinsicHeight(
                        measurables: List<List<IntrinsicMeasurable>>,
                        width: Int
                    ): Int {
                        val headerHeight = 56.dp
                        // Deducting one from size because that's the header row
                        val totalRowHeight = (measurables.size - 1) * 52.dp
                        return (headerHeight + totalRowHeight).roundToPx() + totalVerticalPadding
                    }

                    override fun IntrinsicMeasureScope.maxIntrinsicWidth(
                        measurables: List<List<IntrinsicMeasurable>>,
                        height: Int
                    ): Int {
                        // If there are any flex columns, table width is unbound
                        return if (columnSpecsNormalized.any { it.widthSetting is WidthSetting.Flex }) {
                            Constraints.Infinity
                        } else {
                            // Spec doesn't define responsiveness of the table based on the given
                            // height, so min and max intrinsic widths are the same
                            this.minIntrinsicWidth(measurables, height)
                        }
                    }

                    // When calculating the min width, flex columns are reduced to 0
                    override fun IntrinsicMeasureScope.minIntrinsicWidth(
                        measurables: List<List<IntrinsicMeasurable>>,
                        height: Int
                    ): Int {
                        val widths = columnSpecsNormalized.mapIndexed { colIndex, columnSpec ->
                            when (columnSpec.widthSetting) {
                                is WidthSetting.Flex -> 0
                                is WidthSetting.Static -> columnSpec.widthSetting.width.roundToPx()
                                is WidthSetting.WrapContent -> {
                                    measurables.mapIndexed { index, measurables ->
                                        val rowHeight =
                                            (if (index == 0) 56.dp else 52.dp).roundToPx()
                                        val cIndex =
                                            if (showSelectionColumn) colIndex + 1 else colIndex
                                        val measurable = measurables[cIndex]
                                        measurable.minIntrinsicWidth(rowHeight)
                                    }.max() // Return the biggest intrinsic width
                                }
                            }
                        }
                        return widths.sum()
                    }
                }
            )
        }
    }
}