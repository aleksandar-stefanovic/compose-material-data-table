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
    public val onEdit: ((rowIndex: Int, newValue: S) -> Unit)?
) {
    // Right now, the only reason to copy a ColumnSpec is to modify the widthSetting, thus it's the
    // only parameter of the function, so the code isn't overloaded with boilerplate code of
    // complete copying of an instance
    public abstract fun copy(widthSetting: WidthSetting): ColumnSpec<T, S>
}

public class TextColumnSpec<T>(
    headerName: String,
    widthSetting: WidthSetting,
    valueSelector: (T) -> String,
    onEdit: ((rowIndex: Int, newValue: String) -> Unit)? = null
) : ColumnSpec<T, String>(headerName, widthSetting, valueSelector, onEdit = onEdit) {

    override fun copy(widthSetting: WidthSetting): ColumnSpec<T, String> {
        return TextColumnSpec(headerName, widthSetting, valueSelector, onEdit)
    }
}

public class IntColumnSpec<T>(
    headerName: String,
    widthSetting: WidthSetting,
    valueSelector: (T) -> Int,
    public val numberFormat: String? = null,
    onEdit: ((rowIndex: Int, newValue: Int) -> Unit)? = null
) : ColumnSpec<T, Int>(headerName, widthSetting, valueSelector, Arrangement.End, onEdit) {

    override fun copy(widthSetting: WidthSetting): ColumnSpec<T, Int> {
        return IntColumnSpec(headerName, widthSetting, valueSelector, numberFormat, onEdit)
    }
}

public class DoubleColumnSpec<T>(
    headerName: String,
    widthSetting: WidthSetting,
    valueSelector: (T) -> Double,
    public val numberFormat: String? = null,
    onEdit: ((rowIndex: Int, newValue: Double) -> Unit)? = null
): ColumnSpec<T, Double>(headerName, widthSetting, valueSelector, Arrangement.End, onEdit) {

    override fun copy(widthSetting: WidthSetting): ColumnSpec<T, Double> {
        return DoubleColumnSpec(headerName, widthSetting, valueSelector, numberFormat, onEdit)
    }
}


public class DateColumnSpec<T>(
    headerName: String,
    widthSetting: WidthSetting,
    valueSelector: (T) -> LocalDate,
    public val dateFormat: DateTimeFormat<LocalDate> = LocalDate.Formats.ISO,
    onEdit: ((rowIndex: Int, newValue: LocalDate) -> Unit)? = null
    ) : ColumnSpec<T, LocalDate>(headerName, widthSetting, valueSelector, Arrangement.Start, onEdit) {

    override fun copy(widthSetting: WidthSetting): ColumnSpec<T, LocalDate> {
        return DateColumnSpec(headerName, widthSetting, valueSelector, dateFormat, onEdit)
    }

}

public class CheckboxColumnSpec<T>(
    headerName: String,
    widthSetting: WidthSetting,
    valueSelector: (T) -> Boolean,
    onEdit: ((rowIndex: Int, newValue: Boolean) -> Unit)? = null
) : ColumnSpec<T, Boolean>(headerName, widthSetting, valueSelector, onEdit = onEdit) {

    override fun copy(widthSetting: WidthSetting): ColumnSpec<T, Boolean> {
        return CheckboxColumnSpec(headerName, widthSetting, valueSelector, onEdit)
    }
}

public class DropdownColumnSpec<T, S: Comparable<S>>(
    headerName: String,
    widthSetting: WidthSetting,
    valueSelector: (T) -> S,
    public val valueFormatter: (S) -> String = { it.toString() },
    public val choices: List<S>,
    onEdit: ((rowIndex: Int, newValue: S) -> Unit)? = null
) : ColumnSpec<T, S>(headerName, widthSetting, valueSelector, onEdit = onEdit) {

    override fun copy(widthSetting: WidthSetting): ColumnSpec<T, S> {
        return DropdownColumnSpec(headerName, widthSetting, valueSelector, valueFormatter, choices, onEdit)
    }
}
