package io.github.aleksandarstefanovic.composematerialdatatable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.unit.Dp
import kotlinx.datetime.LocalDate

sealed class WidthSetting {
    class Static(val width: Dp) : WidthSetting()
    data object WrapContent : WidthSetting()
    class Flex(val weight: Float): WidthSetting()
}

sealed class ColumnSpec<T, S: Comparable<S>>(
    val headerName: String,
    val widthSetting: WidthSetting,
    val valueSelector: (T) -> S,
    val horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
) {
    // Right now, the only reason to copy a ColumnSpec is to modify the widthSetting, thus it's the
    // only parameter of the function, so the code isn't overloaded with boilerplate code of
    // complete copying of an instance
    abstract fun copy(widthSetting: WidthSetting): ColumnSpec<T, S>
}

class TextColumnSpec<T>(
    headerName: String,
    widthSetting: WidthSetting,
    valueSelector: (T) -> String
) : ColumnSpec<T, String>(headerName, widthSetting, valueSelector) {

    override fun copy(widthSetting: WidthSetting): ColumnSpec<T, String> {
        return TextColumnSpec(headerName, widthSetting, valueSelector)
    }
}

class IntColumnSpec<T>(
    headerName: String,
    widthSetting: WidthSetting,
    valueSelector: (T) -> Int,
    val numberFormat: String? = null,
) : ColumnSpec<T, Int>(headerName, widthSetting, valueSelector, Arrangement.End) {

    override fun copy(widthSetting: WidthSetting): ColumnSpec<T, Int> {
        return IntColumnSpec(headerName, widthSetting, valueSelector, numberFormat)
    }
}

class DoubleColumnSpec<T>(
    headerName: String,
    widthSetting: WidthSetting,
    valueSelector: (T) -> Double,
    val numberFormat: String? = null,
): ColumnSpec<T, Double>(headerName, widthSetting, valueSelector, Arrangement.End) {

    override fun copy(widthSetting: WidthSetting): ColumnSpec<T, Double> {
        return DoubleColumnSpec(headerName, widthSetting, valueSelector, numberFormat)
    }
}


class DateColumnSpec<T>(
    headerName: String,
    widthSetting: WidthSetting,
    valueSelector: (T) -> LocalDate,
    val dateFormat: String = "YYYY-MM-dd",
    ) : ColumnSpec<T, LocalDate>(headerName, widthSetting, valueSelector, Arrangement.End) {

    override fun copy(widthSetting: WidthSetting): ColumnSpec<T, LocalDate> {
        return DateColumnSpec(headerName, widthSetting, valueSelector, dateFormat)
    }

}

class CheckboxColumnSpec<T>(
    headerName: String,
    widthSetting: WidthSetting,
    valueSelector: (T) -> Boolean,
) : ColumnSpec<T, Boolean>(headerName, widthSetting, valueSelector) {

    override fun copy(widthSetting: WidthSetting): ColumnSpec<T, Boolean> {
        return CheckboxColumnSpec(headerName, widthSetting, valueSelector)
    }
}

class DropdownColumnSpec<T, S: Comparable<S>>(
    headerName: String,
    widthSetting: WidthSetting,
    valueSelector: (T) -> S,
    val valueFormatter: (S) -> String = { it.toString() },
    val choices: List<S>,
    val onChoicePicked: (S) -> Unit
) : ColumnSpec<T, S>(headerName, widthSetting, valueSelector) {

    override fun copy(widthSetting: WidthSetting): ColumnSpec<T, S> {
        return DropdownColumnSpec(headerName, widthSetting, valueSelector, valueFormatter, choices, onChoicePicked)
    }
}
