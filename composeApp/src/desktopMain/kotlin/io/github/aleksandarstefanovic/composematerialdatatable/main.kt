package io.github.aleksandarstefanovic.composematerialdatatable

import androidx.compose.foundation.HorizontalScrollbar
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Compose Material Data Table",
    ) {
        Column {
            val scrollState = rememberScrollState()
            App(Modifier.fillMaxWidth().horizontalScroll(scrollState))
            HorizontalScrollbar(adapter = rememberScrollbarAdapter(scrollState))

        }
    }
}