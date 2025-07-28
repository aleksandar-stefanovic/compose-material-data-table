package io.github.aleksandar_stefanovic.composematerialdatatable

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Compose Material Data Table",
        state = rememberWindowState(WindowPlacement.Floating, false, WindowPosition.PlatformDefault, 1000.dp)
    ) {

        App()
    }
}