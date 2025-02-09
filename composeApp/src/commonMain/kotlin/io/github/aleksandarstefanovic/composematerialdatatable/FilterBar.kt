package io.github.aleksandarstefanovic.composematerialdatatable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.unit.dp
import composematerialdatatable.composeapp.generated.resources.Res
import composematerialdatatable.composeapp.generated.resources.filter_list
import org.jetbrains.compose.resources.vectorResource

@Composable
fun <T> FilterBar(
    modifier: Modifier = Modifier,
    columnSpecs: List<ColumnSpec<T, *>>,
    filters: List<ColumnFilter<T>>,
    onFilterConfirm: (columnSpec: ColumnSpec<T, *>, predicate: FilterPredicate, searchTerm: String) -> Unit,
    onRemoveFilter: (ColumnFilter<T>) -> Unit
) {

    var showFilterPicker by remember { mutableStateOf(false) }
    var showFilterSetup by remember { mutableStateOf(false) }
    var selectedColumn: ColumnSpec<T, *>? by remember { mutableStateOf(null) }

    Row(modifier) {
        IconButton(onClick = { showFilterPicker = !showFilterPicker }) {
            Image(vectorResource(Res.drawable.filter_list), "Filter")
        }
        DropdownMenu(showFilterPicker, onDismissRequest = { showFilterPicker = false }) {
            columnSpecs.forEach { columnSpec ->
                DropdownMenuItem({ showFilterPicker = false; showFilterSetup = true; selectedColumn = columnSpec }) {
                    Text(columnSpec.headerName)
                }
            }
        }
        // Dropdown menu used as a Popup because it's simpler to use and has animations
        DropdownMenu(
            showFilterSetup,
            modifier = Modifier.padding(16.dp),
            onDismissRequest = { showFilterSetup = false }) {
            Text(selectedColumn?.headerName ?: "", style = MaterialTheme.typography.h6)

            // TODO consider remembering to avoid recalculation
            val predicateOptions: List<FilterPredicate> = when (selectedColumn) {
                is TextColumnSpec -> {
                    listOf(
                        FilterPredicate.CONTAINS,
                        FilterPredicate.NOT_CONTAINS,
                        FilterPredicate.IS,
                        FilterPredicate.NOT_IS,
                        FilterPredicate.STARTS_WITH,
                        FilterPredicate.ENDS_WITH
                    )
                }
                else -> emptyList() // TODO
            }

            var selectedPredicate: FilterPredicate by remember { mutableStateOf(predicateOptions.first()) }
            var searchTerm by remember { mutableStateOf("") }

            DropdownPicker(
                selectedPredicate,
                predicateOptions,
                valueFormatter = { it.verb },
                onOptionPicked = { selectedPredicate = it }
            )

            TextField(
                searchTerm,
                onValueChange = { searchTerm = it },
                maxLines = 1,
                modifier = Modifier.onKeyEvent {
                    if (it.key == Key.Enter) {
                        onFilterConfirm(selectedColumn!!, selectedPredicate, searchTerm)
                        return@onKeyEvent true
                    } else {
                        return@onKeyEvent false
                    }
                }
            )
        }
        filters.forEach {
            InputChip(
                true,
                onClick = { onRemoveFilter(it) },
                label = { Text(it.label) },
                colors = InputChipDefaults.inputChipColors(containerColor = Color.White)
            )
        }
    }
}