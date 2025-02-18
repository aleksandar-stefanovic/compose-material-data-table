package io.github.aleksandar_stefanovic.composematerialdatatable.filter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import io.github.aleksandar_stefanovic.composematerialdatatable.CheckboxColumnSpec
import io.github.aleksandar_stefanovic.composematerialdatatable.DropdownPicker

@Composable
internal fun <T> CheckboxFilterModal(
    columnSpec: CheckboxColumnSpec<T>,
    onFilterConfirm: (BooleanFilter<T>) -> Unit,
    onClose: () -> Unit
) {
    Column {
        val predicates = listOf(
            FilterPredicate.SELECTED,
            FilterPredicate.NOT_SELECTED
        )

        var selectedPredicate by remember { mutableStateOf(predicates.first()) }

        DropdownPicker(
            selectedPredicate,
            predicates,
            valueFormatter = { it.verb },
            onOptionPicked = { selectedPredicate = it }
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
                val filter = BooleanFilter(columnSpec, selectedPredicate)
                onFilterConfirm(filter)
            }) { Text("Apply") }
        }
    }

}