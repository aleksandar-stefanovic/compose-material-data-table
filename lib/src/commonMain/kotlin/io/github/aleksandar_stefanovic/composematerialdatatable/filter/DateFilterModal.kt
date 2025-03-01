package io.github.aleksandar_stefanovic.composematerialdatatable.filter

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import io.github.aleksandar_stefanovic.composematerialdatatable.DateColumnSpec
import io.github.aleksandar_stefanovic.composematerialdatatable.DropdownPicker
import io.github.aleksandar_stefanovic.lib.generated.resources.Res
import io.github.aleksandar_stefanovic.lib.generated.resources.date_range
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.vectorResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun <T> DateFilterModal(
    columnSpec: DateColumnSpec<T>,
    onFilterConfirm: (DateFilter<T>) -> Unit,
    onClose: () -> Unit
    ) {
    val predicates = listOf(
        FilterPredicate.IS,
        FilterPredicate.LESS_THAN,
        FilterPredicate.GREATER_THAN
    )

    var  selectedPredicate by remember { mutableStateOf(predicates.first()) }

    Column {
        DropdownPicker(
            selectedPredicate,
            predicates,
            valueFormatter = { when (it) {
                FilterPredicate.IS -> "is"
                FilterPredicate.LESS_THAN -> "is before"
                FilterPredicate.GREATER_THAN -> "is after"
                else -> throw Error("Predicate $it not supported")
            } },
            onOptionPicked = { selectedPredicate = it }
        )

        var showDatePicker by remember { mutableStateOf(false) }
        val datePickerState = rememberDatePickerState()
        var selectedDate: LocalDate? by remember { mutableStateOf(null) }

        fun applyDateFromState() {
            selectedDate = datePickerState.selectedDateMillis?.let {
                Instant.fromEpochMilliseconds(it)
                    .toLocalDateTime(TimeZone.currentSystemDefault())
                    .date
            }
        }

        val selectedDateText = selectedDate?.let {
            columnSpec.dateFormat.format(it)
        } ?: ""

        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = selectedDateText,
                onValueChange = { },
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = !showDatePicker }) {
                        Image(vectorResource(Res.drawable.date_range), "Date picker")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
            )

            if (showDatePicker) {
                Popup(
                    onDismissRequest = { showDatePicker = false },
                    alignment = Alignment.TopStart
                ) {
                    Column(
                        modifier = Modifier
                            .shadow(elevation = 4.dp)
                            .background(MaterialTheme.colorScheme.surface)
                            .width(450.dp)
                            .padding(16.dp)
                    ) {
                        DatePicker(
                            state = datePickerState,
                            showModeToggle = false,
                            title = null,
                            headline = null

                        )
                        Button({ applyDateFromState(); showDatePicker = false}, Modifier.align(Alignment.End)) {
                            Text("Confirm")
                        }
                    }
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
            FilledTonalButton(onClick = {
                if (selectedDate != null) {
                    onFilterConfirm(DateFilter(columnSpec, selectedPredicate, selectedDate!!))
                }
            }) { Text("Apply") }
        }
    }
}