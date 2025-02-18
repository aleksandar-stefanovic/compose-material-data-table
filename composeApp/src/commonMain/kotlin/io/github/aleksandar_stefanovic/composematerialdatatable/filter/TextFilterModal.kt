package io.github.aleksandar_stefanovic.composematerialdatatable.filter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.aleksandar_stefanovic.composematerialdatatable.DropdownPicker
import io.github.aleksandar_stefanovic.composematerialdatatable.TextColumnSpec
import io.github.aleksandar_stefanovic.composematerialdatatable.onEnterPress

@Composable
internal fun <T> TextFilterModal(
    columnSpec: TextColumnSpec<T>,
    onFilterConfirm: (StringFilter<T>) -> Unit,
    onClose: () -> Unit) {

    val predicates = listOf(
        FilterPredicate.CONTAINS,
        FilterPredicate.NOT_CONTAINS,
        FilterPredicate.IS,
        FilterPredicate.NOT_IS,
        FilterPredicate.STARTS_WITH,
        FilterPredicate.ENDS_WITH,
        // TODO implement EMPTY, NOT_EMPTY
    )

    var selectedPredicate by remember { mutableStateOf(predicates.first()) }

    Column {
        DropdownPicker(
            selectedPredicate,
            predicates,
            valueFormatter = { it.verb },
            onOptionPicked = { selectedPredicate = it }
        )

        var searchTerm by remember { mutableStateOf("") }

        OutlinedTextField(
            searchTerm,
            onValueChange = {
                searchTerm = it
            },
            singleLine = true,
            modifier = Modifier.onEnterPress {
                if (searchTerm.isNotBlank()) {
                    onFilterConfirm(StringFilter(columnSpec, selectedPredicate, searchTerm))
                }
            }
        )

        Row(
            Modifier.fillMaxWidth().padding(top = 12.dp),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(
                onClose,
                Modifier.padding(end = 8.dp)
            ) { Text("Cancel") }
            FilledTonalButton(onClick = {
                if (searchTerm.isNotBlank()) {
                    onFilterConfirm(StringFilter(columnSpec, selectedPredicate, searchTerm))
                }
            }) { Text("Apply") }
        }

    }
}