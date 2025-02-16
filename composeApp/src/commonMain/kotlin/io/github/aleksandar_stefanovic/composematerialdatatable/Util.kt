package io.github.aleksandar_stefanovic.composematerialdatatable

import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent

internal inline fun Modifier.onEnterPress(crossinline action: () -> Unit): Modifier {
    return this.onKeyEvent { event ->
        if (event.key == Key.Enter) {
            action()
            return@onKeyEvent true
        } else {
            return@onKeyEvent false
        }
    }
}