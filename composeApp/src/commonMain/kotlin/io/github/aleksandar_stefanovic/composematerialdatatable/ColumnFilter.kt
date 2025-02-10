package io.github.aleksandar_stefanovic.composematerialdatatable


abstract class ColumnFilter<T>(val columnSpec: TextColumnSpec<T>) {
    abstract fun filter(data: List<T>): List<T>
    abstract val label: String
}

// A single predicate enum for all types of filters, a single enum can be applicable to multiple
// types
enum class FilterPredicate(val verb: String) {
    CONTAINS("contains"),
    NOT_CONTAINS("doesn't contain"),
    IS("is"),
    NOT_IS("is not"),
    STARTS_WITH("starts with"),
    ENDS_WITH("ends with")
}


class StringFilter<T>(columnSpec: TextColumnSpec<T>, private val predicate: FilterPredicate, private val term: String): ColumnFilter<T>(columnSpec) {

    override val label = "${columnSpec.headerName} ${predicate.verb} $term"

    override fun filter(data: List<T>): List<T> {

        return when (predicate) {
            FilterPredicate.CONTAINS -> data.filter {
                columnSpec.valueSelector(it).contains(term, ignoreCase = true)
            }
            FilterPredicate.NOT_CONTAINS -> data.filterNot {
                columnSpec.valueSelector(it).contains(term, ignoreCase = true)
            }
            FilterPredicate.IS -> data.filter {
                columnSpec.valueSelector(it).equals(term, ignoreCase = true)
            }
            FilterPredicate.NOT_IS -> data.filter {
                !columnSpec.valueSelector(it).equals(term, ignoreCase = true)
            }
            FilterPredicate.STARTS_WITH -> data.filter {
                columnSpec.valueSelector(it).startsWith(term, ignoreCase = true)
            }
            FilterPredicate.ENDS_WITH -> data.filter {
                columnSpec.valueSelector(it).endsWith(term, ignoreCase = true)
            }
        }
    }
}