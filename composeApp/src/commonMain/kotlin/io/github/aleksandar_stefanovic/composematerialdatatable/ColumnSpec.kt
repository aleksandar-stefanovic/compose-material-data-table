package io.github.aleksandar_stefanovic.composematerialdatatable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.unit.Dp
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format.DateTimeFormat

public sealed class WidthSetting {
    public class Static(public val width: Dp) : WidthSetting()
    public data object WrapContent : WidthSetting()
    public class Flex(public val weight: Float): WidthSetting()
}

public sealed class ColumnSpec<T, S: Comparable<S>>(
    public val headerName: String,
    public val widthSetting: WidthSetting,
    public val valueSelector: (T) -> S,
    public val horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
) {
    // Right now, the only reason to copy a ColumnSpec is to modify the widthSetting, thus it's the
    // only parameter of the function, so the code isn't overloaded with boilerplate code of
    // complete copying of an instance
    public abstract fun copy(widthSetting: WidthSetting): ColumnSpec<T, S>
}

public class TextColumnSpec<T>(
    headerName: String,
    widthSetting: WidthSetting,
    valueSelector: (T) -> String
) : ColumnSpec<T, String>(headerName, widthSetting, valueSelector) {

    override fun copy(widthSetting: WidthSetting): ColumnSpec<T, String> {
        return TextColumnSpec(headerName, widthSetting, valueSelector)
    }
}

public class IntColumnSpec<T>(
    headerName: String,
    widthSetting: WidthSetting,
    valueSelector: (T) -> Int,
    public val numberFormat: String? = null,
) : ColumnSpec<T, Int>(headerName, widthSetting, valueSelector, Arrangement.End) {

    override fun copy(widthSetting: WidthSetting): ColumnSpec<T, Int> {
        return IntColumnSpec(headerName, widthSetting, valueSelector, numberFormat)
    }
}

public class DoubleColumnSpec<T>(
    headerName: String,
    widthSetting: WidthSetting,
    valueSelector: (T) -> Double,
    public val numberFormat: String? = null,
): ColumnSpec<T, Double>(headerName, widthSetting, valueSelector, Arrangement.End) {

    override fun copy(widthSetting: WidthSetting): ColumnSpec<T, Double> {
        return DoubleColumnSpec(headerName, widthSetting, valueSelector, numberFormat)
    }
}


public class DateColumnSpec<T>(
    headerName: String,
    widthSetting: WidthSetting,
    valueSelector: (T) -> LocalDate,
    public val dateFormat: DateTimeFormat<LocalDate> = LocalDate.Formats.ISO,
    ) : ColumnSpec<T, LocalDate>(headerName, widthSetting, valueSelector, Arrangement.Start) {

    override fun copy(widthSetting: WidthSetting): ColumnSpec<T, LocalDate> {
        return DateColumnSpec(headerName, widthSetting, valueSelector, dateFormat)
    }

}

public class CheckboxColumnSpec<T>(
    headerName: String,
    widthSetting: WidthSetting,
    valueSelector: (T) -> Boolean,
) : ColumnSpec<T, Boolean>(headerName, widthSetting, valueSelector) {

    override fun copy(widthSetting: WidthSetting): ColumnSpec<T, Boolean> {
        return CheckboxColumnSpec(headerName, widthSetting, valueSelector)
    }
}

public class DropdownColumnSpec<T, S: Comparable<S>>(
    headerName: String,
    widthSetting: WidthSetting,
    valueSelector: (T) -> S,
    public val valueFormatter: (S) -> String = { it.toString() },
    public val choices: List<S>,
    public val onChoicePicked: (S) -> Unit
) : ColumnSpec<T, S>(headerName, widthSetting, valueSelector) {

    override fun copy(widthSetting: WidthSetting): ColumnSpec<T, S> {
        return DropdownColumnSpec(headerName, widthSetting, valueSelector, valueFormatter, choices, onChoicePicked)
    }
}
