package io.github.aleksandar_stefanovic.composematerialdatatable.filter

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import composematerialdatatable.composeapp.generated.resources.Res
import composematerialdatatable.composeapp.generated.resources.close
import composematerialdatatable.composeapp.generated.resources.filter_list
import io.github.aleksandar_stefanovic.composematerialdatatable.CheckboxColumnSpec
import io.github.aleksandar_stefanovic.composematerialdatatable.ColumnSpec
import io.github.aleksandar_stefanovic.composematerialdatatable.DateColumnSpec
import io.github.aleksandar_stefanovic.composematerialdatatable.DoubleColumnSpec
import io.github.aleksandar_stefanovic.composematerialdatatable.DropdownColumnSpec
import io.github.aleksandar_stefanovic.composematerialdatatable.IntColumnSpec
import io.github.aleksandar_stefanovic.composematerialdatatable.TextColumnSpec
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
    var selectedColumnSpec: ColumnSpec<T, *>? by remember { mutableStateOf(null) }

    Row(modifier) {
        IconButton(onClick = { showFilterPicker = !showFilterPicker }) {
            Image(vectorResource(Res.drawable.filter_list), "Filter")
        }
        DropdownMenu(showFilterPicker, onDismissRequest = { showFilterPicker = false }) {
            columnSpecs.forEach { columnSpec ->
                DropdownMenuItem({
                    selectedColumnSpec = columnSpec; showFilterPicker = false; showFilterSetup =
                    true
                }) {
                    Text(columnSpec.headerName)
                }
            }
        }
        // Dropdown menu used as a Popup because it's simpler to use and has animations
        DropdownMenu(
            showFilterSetup,
            modifier = Modifier.padding(16.dp),
            onDismissRequest = { showFilterSetup = false }) {
            Text(selectedColumnSpec?.headerName ?: "", style = MaterialTheme.typography.h6)

            when (selectedColumnSpec!!) {
                is TextColumnSpec -> TextFilterModal(
                    (selectedColumnSpec as TextColumnSpec<T>),
                    onFilterConfirm,
                    onClose = { showFilterSetup = false }
                )

                is IntColumnSpec -> IntFilterModal(
                    (selectedColumnSpec as IntColumnSpec<T>),
                    onFilterConfirm,
                    onClose = { showFilterSetup = false }
                )

                is DoubleColumnSpec -> DoubleFilterModal(
                    (selectedColumnSpec as DoubleColumnSpec<T>),
                    onFilterConfirm,
                    onClose = { showFilterSetup = false }
                )

                is CheckboxColumnSpec -> CheckboxFilterModal(
                    (selectedColumnSpec as CheckboxColumnSpec<T>),
                    onFilterConfirm,
                    onClose = { showFilterSetup = false }
                )

                is DateColumnSpec -> TODO()
                is DropdownColumnSpec -> TODO()
            }
        }

        filters.forEach {
            InputChip(
                true,
                modifier = Modifier.padding(end = 8.dp),
                onClick = { onRemoveFilter(it) },
                label = { Text(it.label) },
                colors = InputChipDefaults.inputChipColors(
                    containerColor = Color.Transparent,
                    selectedContainerColor = Color.Transparent,
                    labelColor = MaterialTheme.colors.onSurface,
                    selectedLabelColor = MaterialTheme.colors.onSurface
                ),
                border = InputChipDefaults.inputChipBorder(
                    borderColor = Color(0x1f000000),
                    borderWidth = 1.dp,
                    selected = false,
                    enabled = true
                ),
                trailingIcon = {
                    IconButton(
                        modifier = Modifier.size(18.dp),
                        onClick = { onRemoveFilter(it) }
                    ) {
                        Image(vectorResource(Res.drawable.close), "Filter")
                    }
                }

            )
        }
    }
}