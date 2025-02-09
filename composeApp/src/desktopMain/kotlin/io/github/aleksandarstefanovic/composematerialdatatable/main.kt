package io.github.aleksandarstefanovic.composematerialdatatable

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Compose Material Data Table",
    ) {
        App()
    }
}