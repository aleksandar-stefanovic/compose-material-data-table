package io.github.aleksandar_stefanovic.composematerialdatatable

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import io.github.aleksandar_stefanovic.composematerialdatatable.icons.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun <T> DropdownPicker(
    value: T,
    options: Iterable<T>,
    valueFormatter: (T) -> String = { it.toString() },
    onOptionPicked: (T) -> Unit
) {

    var showOptions by remember { mutableStateOf(false) }

    Box {
        Row(Modifier.height(48.dp).clickable { showOptions = true }, verticalAlignment = Alignment.CenterVertically) {
            Text(valueFormatter(value))
            Image(ArrowDropDown, "Dropdown")
        }

        DropdownMenu(showOptions, onDismissRequest = { showOptions = false }) {
            options.forEach { item ->
                DropdownMenuItem({ showOptions = false; onOptionPicked(item) }) {
                    Text(valueFormatter(item))
                }
            }
        }
    }
}