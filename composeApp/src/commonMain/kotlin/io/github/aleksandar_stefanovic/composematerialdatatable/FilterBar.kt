package io.github.aleksandar_stefanovic.composematerialdatatable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import composematerialdatatable.composeapp.generated.resources.Res
import composematerialdatatable.composeapp.generated.resources.filter_list
import org.jetbrains.compose.resources.vectorResource

@Composable
internal fun <T> FilterBar(
    modifier: Modifier = Modifier,
    columnSpecs: List<ColumnSpec<T, *>>,
    filters: List<ColumnFilter<T, *>>,
    onFilterConfirm: (columnFilter: ColumnFilter<T, *>) -> Unit,
    onRemoveFilter: (ColumnFilter<T, *>) -> Unit
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
            val (predicateOptions: List<FilterPredicate>, predicateType: KeyboardType) = when (selectedColumn) {
                is TextColumnSpec -> listOf(
                    FilterPredicate.CONTAINS,
                    FilterPredicate.NOT_CONTAINS,
                    FilterPredicate.IS,
                    FilterPredicate.NOT_IS,
                    FilterPredicate.STARTS_WITH,
                    FilterPredicate.ENDS_WITH
                ) to KeyboardType.Text
                is IntColumnSpec -> listOf(
                    FilterPredicate.IS,
                    FilterPredicate.NOT_IS,
                    FilterPredicate.GREATER_THAN,
                    FilterPredicate.GREATER_THAN_EQUALS,
                    FilterPredicate.LESS_THAN,
                    FilterPredicate.LESS_THAN_EQUALS,
                    FilterPredicate.BETWEEN
                ) to KeyboardType.Number
                is DoubleColumnSpec -> listOf(
                    FilterPredicate.IS,
                    FilterPredicate.NOT_IS,
                    FilterPredicate.GREATER_THAN,
                    FilterPredicate.GREATER_THAN_EQUALS,
                    FilterPredicate.LESS_THAN,
                    FilterPredicate.LESS_THAN_EQUALS,
                    FilterPredicate.BETWEEN
                ) to KeyboardType.Decimal
                else -> TODO()
            }

            var selectedPredicate: FilterPredicate by remember { mutableStateOf(predicateOptions.first()) }

            DropdownPicker(
                selectedPredicate,
                predicateOptions,
                valueFormatter = { it.verb },
                onOptionPicked = { selectedPredicate = it }
            )

            var canConfirm by remember { mutableStateOf(false) }

            var produceFilter by remember { mutableStateOf<(() -> ColumnFilter<T, *>)?>(null) }

            val textFieldModifier = Modifier.onEnterPress {
                val filter = produceFilter?.invoke()
                if (filter != null) {
                    onFilterConfirm(filter)
                }
            }

            when (predicateType) {
                KeyboardType.Text -> {
                    var searchTerm by remember { mutableStateOf("") }

                    produceFilter = {
                        StringFilter((selectedColumn as TextColumnSpec<T>),
                            selectedPredicate,
                            searchTerm
                        )
                    }
                    OutlinedTextField(
                        searchTerm,
                        onValueChange = {
                            searchTerm = it
                            canConfirm = it.isNotBlank()
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = predicateType),
                        modifier = textFieldModifier                    )
                }
                KeyboardType.Number -> {
                    var firstText by remember { mutableStateOf("") }
                    val firstValue: Int? by remember(firstText) {
                        derivedStateOf {
                            firstText.toIntOrNull()
                        }
                    }

                    var secondText by remember { mutableStateOf("") }
                    val secondValue: Int? by remember(secondText) {
                        derivedStateOf {
                            secondText.toIntOrNull()
                        }
                    }

                    produceFilter = {
                        NumberFilter((selectedColumn as IntColumnSpec<T>), selectedPredicate, listOf(firstValue!!, secondValue ?: 0))
                    }

                    fun updateCanConfirm() {
                        canConfirm = if (selectedPredicate == FilterPredicate.BETWEEN) {
                            firstValue != null && secondValue != null
                        } else {
                            firstValue != null
                        }
                    }

                    Row {
                        OutlinedTextField(
                            firstText,
                            onValueChange = { firstText = it; updateCanConfirm() },
                            keyboardOptions = KeyboardOptions(keyboardType = predicateType),
                            isError = firstValue == null && firstText != "",
                            modifier = textFieldModifier,
                            singleLine = true
                        )

                        if (selectedPredicate == FilterPredicate.BETWEEN) {
                            Text(" and ")
                            OutlinedTextField(
                                secondText,
                                onValueChange = { secondText = it; updateCanConfirm() },
                                keyboardOptions = KeyboardOptions(keyboardType = predicateType),
                                isError = secondValue == null && secondText != "",
                                modifier = textFieldModifier,
                                singleLine = true
                            )
                        }
                    }
                }

                KeyboardType.Decimal -> {
                    var firstText by remember { mutableStateOf("") }
                    val firstValue: Double? by remember(firstText) {
                        derivedStateOf {
                            firstText.toDoubleOrNull()
                        }
                    }

                    var secondText by remember { mutableStateOf("") }
                    val secondValue: Double? by remember(secondText) {
                        derivedStateOf {
                            secondText.toDoubleOrNull()
                        }
                    }

                    fun updateCanConfirm() {
                        canConfirm = if (selectedPredicate == FilterPredicate.BETWEEN) {
                            firstValue != null && secondValue != null
                        } else {
                            firstValue != null
                        }
                    }

                    produceFilter = {
                        NumberFilter((selectedColumn as DoubleColumnSpec<T>), selectedPredicate, listOf(firstValue!!, secondValue ?: 0.0))
                    }

                    Row {
                        OutlinedTextField(
                            firstText,
                            onValueChange = { firstText = it; updateCanConfirm() },
                            keyboardOptions = KeyboardOptions(keyboardType = predicateType),
                            isError = firstValue == null && firstText != "",
                            modifier = textFieldModifier,
                            singleLine = true
                        )

                        if (selectedPredicate == FilterPredicate.BETWEEN) {
                            Text(" and ")
                            OutlinedTextField(
                                secondText,
                                onValueChange = { secondText = it; updateCanConfirm() },
                                keyboardOptions = KeyboardOptions(keyboardType = predicateType),
                                isError = secondValue == null && secondText != "",
                                modifier = textFieldModifier,
                                singleLine = true
                            )
                        }
                    }
                }
            }


            Row(Modifier.fillMaxWidth().padding(top = 12.dp), horizontalArrangement = Arrangement.End) {
                TextButton({ showFilterSetup = false }, Modifier.padding(end = 8.dp)) { Text("Cancel") }
                FilledTonalButton({
                    if (canConfirm) {
                        val filter = produceFilter?.invoke()
                        if (filter != null) {
                            onFilterConfirm(filter)
                        }
                    }
                }, enabled = canConfirm) { Text("Apply") }
            }


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