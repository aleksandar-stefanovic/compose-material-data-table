package io.github.aleksandar_stefanovic.composematerialdatatable

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp

@Composable
public fun VerticalScrollbar(scrollState: ScrollState, modifier: Modifier = Modifier) {
    val vSize = scrollState.viewportSize

    val scrollbarHeight = vSize * vSize / (vSize + scrollState.maxValue)
    val scrollbarHeightDp = with(LocalDensity.current) { scrollbarHeight.toDp() }

    val offset = lerp(
        0,
        vSize - scrollbarHeight,
        scrollState.value.toFloat() / scrollState.maxValue
    )
    val offsetDp = with(LocalDensity.current) { offset.toDp() }

    var scrollDelta by remember { mutableStateOf(0f) }

    LaunchedEffect(scrollDelta) {
        if (scrollDelta != 0f) {
            scrollState.scrollTo(scrollState.value + scrollDelta.toInt())
            scrollDelta = 0f
        }
    }

    Box(
        modifier
            .offset(y = offsetDp)
            .size(10.dp, scrollbarHeightDp)
            .background(Color.DarkGray)
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    scrollDelta += dragAmount.y * 2.5f // Empirically-chosen magical constant that ensures 1:1 scrolling
                }
            }
    )
}