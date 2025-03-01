package io.github.aleksandar_stefanovic.composematerialdatatable.filter

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.aleksandar_stefanovic.composematerialdatatable.DropdownColumnSpec
import io.github.aleksandar_stefanovic.composematerialdatatable.DropdownPicker

@Composable
internal fun <T, S : Comparable<S>> DropdownFilterModal(
    columnSpec: DropdownColumnSpec<T, S>,
    onFilterConfirm: (DropdownFilter<T, S>) -> Unit,
    onClose: () -> Unit
) {

    val predicates = listOf(
        FilterPredicate.IS_ANY_OF,
        FilterPredicate.IS_NONE_OF
    )

    var options: List<Pair<S, Boolean>> by remember {
        mutableStateOf(columnSpec.choices.map { it to false })
    }

    var selectedPredicate by remember { mutableStateOf(predicates.first()) }

    Column {
        DropdownPicker(
            selectedPredicate,
            predicates,
            valueFormatter = { it.verb },
            onOptionPicked = { selectedPredicate = it }
        )

        LazyColumn(Modifier.width(200.dp).height((52 * options.size.coerceAtMost(8)).dp)) {
            items(options.size) { index ->
                val option = options[index]

                Row(Modifier.clickable {
                    options = options.map {
                        if (it == option) {
                            Pair(it.first, !it.second)
                        } else {
                            it
                        }
                    }
                }) {
                    Checkbox(option.second, onCheckedChange = {
                        options = options.map {
                            if (it == option) {
                                Pair(it.first, !it.second)
                            } else {
                                it
                            }
                        }
                    })
                    Text(columnSpec.valueFormatter(option.first), Modifier.align(Alignment.CenterVertically))
                }
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
            FilledTonalButton(enabled = predicates.isNotEmpty(), onClick = {
                val selectedOptions = options.filter { it.second }.map { it.first }
                onFilterConfirm(DropdownFilter(columnSpec, selectedPredicate, selectedOptions))
            }, ) {
                Text("Apply")
            }
        }
    }
}