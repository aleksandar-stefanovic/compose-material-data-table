package io.github.aleksandar_stefanovic.composematerialdatatable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.unit.Dp
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format.DateTimeFormat

public sealed class WidthSetting {

    /**
     * Use this width setting when you want the column to have a fixed size.
     */
    public class Static(public val width: Dp) : WidthSetting()

    /**
     * Use this width setting when you want the column to be the size of the widest element in the
     * column. Note that the width is calculated based on the rows currently shown on the screen —
     * when changing the pages, the width is recalculated, and is likely to be different than the
     * previous.
     */
    public data object WrapContent : WidthSetting()

    /**
     * Use this width setting when you want the column to use all remaining horizontal space in the
     * column.
     *
     * @param weight Determines how much of the available space should this column take compared to
     * other columns. Examples:
     * - If there are two columns, and they both have the weight of `1f`, each of them will take 50% of the space.
     * - If there are three columns, two of them have the weight of `0.25f`, and the third one has `0.5f`,
     * then the first two will take 25% of the available space, while the third one will take 50%.
     *
     * There is no need for the sum of weights to be a specific number, as the relative weights are calculated
     * based on the total sum of weights provided.
     */
    public class Flex(public val weight: Float): WidthSetting()
}

public sealed class ColumnSpec<T, S: Comparable<S>>(
    internal val headerName: String,
    internal val widthSetting: WidthSetting,
    internal val valueSelector: (T) -> S,
    internal val horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    internal val onEdit: ((rowIndex: Int, newValue: S) -> Unit)?
) {
    // Right now, the only reason to copy a ColumnSpec is to modify the widthSetting, thus it's the
    // only parameter of the function, so the code isn't overloaded with boilerplate code of
    // complete copying of an instance
    internal abstract fun copy(widthSetting: WidthSetting): ColumnSpec<T, S>
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
    public val formatToString: (Int) -> String = { it.toString() },
    public val parseToInt: (String) -> Int = { it.toInt()  },
    onEdit: ((rowIndex: Int, newValue: Int) -> Unit)? = null
) : ColumnSpec<T, Int>(headerName, widthSetting, valueSelector, Arrangement.End, onEdit) {

    override fun copy(widthSetting: WidthSetting): ColumnSpec<T, Int> {
        return IntColumnSpec(headerName, widthSetting, valueSelector, formatToString, parseToInt, onEdit)
    }
}

public class DoubleColumnSpec<T>(
    headerName: String,
    widthSetting: WidthSetting,
    valueSelector: (T) -> Double,
    public val formatToString: (Double) -> String = { it.toString() },
    public val parseToDouble: (String) -> Double = { it.toDouble()  },
    onEdit: ((rowIndex: Int, newValue: Double) -> Unit)? = null
): ColumnSpec<T, Double>(headerName, widthSetting, valueSelector, Arrangement.End, onEdit) {

    override fun copy(widthSetting: WidthSetting): ColumnSpec<T, Double> {
        return DoubleColumnSpec(headerName, widthSetting, valueSelector, formatToString, parseToDouble, onEdit)
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
