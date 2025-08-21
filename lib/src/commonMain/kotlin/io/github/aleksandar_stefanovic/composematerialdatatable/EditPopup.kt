package io.github.aleksandar_stefanovic.composematerialdatatable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties

internal data class EditPopupState(
    val visible: Boolean = false,
    val position: Offset = Offset.Zero,
    val text: String = "",
    val onConfirm: (String) -> Unit = {},
    val validator: (String) -> Boolean = { true }
) {
    fun show(position: Offset, text: String, onConfirm: (String) -> Unit, validator: (String) -> Boolean = { true }): EditPopupState {
        return this.copy(visible = true, position = position, text = text, onConfirm = onConfirm, validator = validator)
    }

    fun hide() = this.copy(visible = false)
}

@Composable
internal fun rememberEditPopupState(): MutableState<EditPopupState> {
    return remember { mutableStateOf(EditPopupState()) }
}

@Composable
internal fun EditPopup(state: MutableState<EditPopupState>) {
    val (visible, position, text, onConfirm) = state.value

    var textFieldValue by remember(text, visible) { mutableStateOf(text) }

    if (visible) {

        Popup(
            offset = IntOffset(
                position.x.toInt(),
                position.y.toInt() + with(LocalDensity.current) { 32.dp.roundToPx() }
            ),
            properties = PopupProperties(focusable = true),
            onDismissRequest = { state.value = state.value.hide() }
        ) {
            Card(Modifier.background(Color.White), border = BorderStroke(1.dp, Color(0x1f000000))) {
                Column(Modifier.padding(12.dp)) {
                    OutlinedTextField(textFieldValue, onValueChange = { textFieldValue = it })
                    Row(
                        Modifier.padding(top = 12.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton({ state.value = state.value.hide() }) {
                            Text("Cancel")
                        }
                        TextButton({ onConfirm(textFieldValue) }) {
                            Text("Confirm")
                        }
                    }
                }


            }

        }
    }
}