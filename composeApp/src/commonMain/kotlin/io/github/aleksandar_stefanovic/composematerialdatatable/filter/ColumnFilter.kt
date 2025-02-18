package io.github.aleksandar_stefanovic.composematerialdatatable.filter

import io.github.aleksandar_stefanovic.composematerialdatatable.CheckboxColumnSpec
import io.github.aleksandar_stefanovic.composematerialdatatable.ColumnSpec
import io.github.aleksandar_stefanovic.composematerialdatatable.TextColumnSpec

// A single predicate enum for all types of filters, a single enum can be applicable to multiple
// types, for example, "IS" works on all scalar types
internal enum class FilterPredicate(val verb: String) {
    CONTAINS("contains"),
    NOT_CONTAINS("doesn't contain"),
    IS("is"),
    NOT_IS("is not"),
    STARTS_WITH("starts with"),
    ENDS_WITH("ends with"),

    GREATER_THAN("greater than"),
    GREATER_THAN_EQUALS("greater or equal than"),
    LESS_THAN("less than"),
    LESS_THAN_EQUALS("less or equal than"),
    BETWEEN("between"),

    SELECTED("selected"),
    NOT_SELECTED("not selected")
}

internal abstract class ColumnFilter<T, S : Comparable<S>>(val columnSpec: ColumnSpec<T, S>) {
    abstract fun test(item: T): Boolean
    abstract val label: String
}

internal class StringFilter<T>(
    columnSpec: TextColumnSpec<T>, private val predicate: FilterPredicate, private val term: String
) : ColumnFilter<T, String>(columnSpec) {

    override val label = "${columnSpec.headerName} ${predicate.verb} $term"

    override fun test(item: T): Boolean {
        val value = columnSpec.valueSelector(item)

        return when (predicate) {
            FilterPredicate.CONTAINS -> value.contains(term, ignoreCase = true)
            FilterPredicate.NOT_CONTAINS -> !value.contains(term, ignoreCase = true)
            FilterPredicate.IS -> value.equals(term, ignoreCase = true)
            FilterPredicate.NOT_IS -> !value.equals(term, ignoreCase = true)
            FilterPredicate.STARTS_WITH -> value.startsWith(term, ignoreCase = true)
            FilterPredicate.ENDS_WITH -> value.endsWith(term, ignoreCase = true)
            else -> throw Error("Filter predicate ${predicate.verb} is not supported")
        }
    }
}

internal class NumberFilter<T, S>(
    columnSpec: ColumnSpec<T, S>,
    private val predicate: FilterPredicate,
    private val parameters: List<S>
) :
    ColumnFilter<T, S>(columnSpec) where S : Number, S : Comparable<S> {

    override fun test(item: T): Boolean {
        val value = columnSpec.valueSelector(item)

        return when (predicate) {
            FilterPredicate.IS -> parameters[0] == value
            FilterPredicate.NOT_IS -> parameters[0] != value
            FilterPredicate.GREATER_THAN -> value > parameters[0]
            FilterPredicate.GREATER_THAN_EQUALS -> value >= parameters[0]
            FilterPredicate.LESS_THAN -> value < parameters[0]
            FilterPredicate.LESS_THAN_EQUALS -> value <= parameters[0]
            FilterPredicate.BETWEEN -> value in parameters[0]..parameters[1]
            else -> throw Error("Filter predicate ${predicate.verb} is not supported")
        }
    }

    override val label: String
        get() = when (predicate) {
            FilterPredicate.IS,
            FilterPredicate.NOT_IS,
            FilterPredicate.GREATER_THAN,
            FilterPredicate.GREATER_THAN_EQUALS,
            FilterPredicate.LESS_THAN,
            FilterPredicate.LESS_THAN_EQUALS -> "${columnSpec.headerName} ${predicate.verb} ${parameters.first()}"
            FilterPredicate.BETWEEN -> "${columnSpec.headerName} is between ${parameters[0]} and ${parameters[1]}"
            else -> throw Error("Filter predicate ${predicate.verb} is not supported")
        }

}

internal class BooleanFilter<T>(
    columnSpec: CheckboxColumnSpec<T>,
    private val predicate: FilterPredicate
) : ColumnFilter<T, Boolean>(columnSpec) {
    override fun test(item: T): Boolean {
        val selected = columnSpec.valueSelector(item)
        return when (predicate) {
            FilterPredicate.SELECTED -> selected
            FilterPredicate.NOT_SELECTED -> !selected
            else -> throw Error("Filter predicate ${predicate.verb} is not supported")
        }
    }

    override val label: String = when (predicate) {
        FilterPredicate.SELECTED,
        FilterPredicate.NOT_SELECTED -> "${columnSpec.headerName} is ${predicate.verb}"
        else -> throw Error("Filter predicate ${predicate.verb} is not supported")
    }

}