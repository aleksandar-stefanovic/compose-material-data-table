package io.github.aleksandar_stefanovic.composematerialdatatable.filter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import io.github.aleksandar_stefanovic.composematerialdatatable.DropdownPicker
import io.github.aleksandar_stefanovic.composematerialdatatable.IntColumnSpec
import io.github.aleksandar_stefanovic.composematerialdatatable.onEnterPress

@Composable
internal fun <T> IntFilterModal(
    columnSpec: IntColumnSpec<T>,
    onFilterConfirm: (NumberFilter<T, Int>) -> Unit,
    onClose: () -> Unit
) {
    val predicates = listOf(
        FilterPredicate.IS,
        FilterPredicate.NOT_IS,
        FilterPredicate.GREATER_THAN,
        FilterPredicate.GREATER_THAN_EQUALS,
        FilterPredicate.LESS_THAN,
        FilterPredicate.LESS_THAN_EQUALS,
        FilterPredicate.BETWEEN
    )

    var selectedPredicate by remember { mutableStateOf(predicates.first()) }

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

    val canConfirm by remember(selectedPredicate, firstValue, secondValue) {
        derivedStateOf {
            if (selectedPredicate == FilterPredicate.BETWEEN) {
                firstValue != null && secondValue != null
            } else {
                firstValue != null
            }
        }
    }

    val onConfirm = {
        if (canConfirm) {
            onFilterConfirm(NumberFilter(columnSpec, selectedPredicate, listOf(firstValue!!, secondValue ?: 0)))
        }
    }

    Column {
        DropdownPicker(
            selectedPredicate,
            predicates,
            valueFormatter = { it.verb },
            onOptionPicked = { selectedPredicate = it }
        )

        Row {
            OutlinedTextField(
                firstText,
                onValueChange = { firstText = it; },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = firstValue == null && firstText != "",
                modifier = Modifier.onEnterPress(onConfirm),
                singleLine = true
            )

            if (selectedPredicate == FilterPredicate.BETWEEN) {
                Text(" and ")
                OutlinedTextField(
                    secondText,
                    onValueChange = { secondText = it; },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = secondValue == null && secondText != "",
                    modifier = Modifier.onEnterPress(onConfirm),
                    singleLine = true
                )
            }
        }

        Row(
            Modifier.fillMaxWidth().padding(top = 12.dp),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(
                onClose,
                Modifier.padding(end = 8.dp)
            ) { Text("Cancel") }
            FilledTonalButton(onClick = onConfirm, enabled = canConfirm) { Text("Apply") }
        }
    }
}