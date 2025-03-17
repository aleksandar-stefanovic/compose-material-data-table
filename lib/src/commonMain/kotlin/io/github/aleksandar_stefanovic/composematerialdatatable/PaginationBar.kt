package io.github.aleksandar_stefanovic.composematerialdatatable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import io.github.aleksandar_stefanovic.composematerialdatatable.icons.ChevronLeft
import io.github.aleksandar_stefanovic.composematerialdatatable.icons.ChevronRight
import io.github.aleksandar_stefanovic.composematerialdatatable.icons.DataTableIcons
import io.github.aleksandar_stefanovic.composematerialdatatable.icons.FirstPage
import io.github.aleksandar_stefanovic.composematerialdatatable.icons.LastPage

@Composable
internal fun PaginationBar(
    modifier: Modifier = Modifier,
    dataSize: Int,
    pageSizeOptions: List<Int>,
    defaultPageSize: Int,
    onPaginationChanged: (offset: Int, count: Int) -> Unit
) {

    var pageSize by remember { mutableStateOf(defaultPageSize) }
    var pageIndex by remember { mutableStateOf(0) }
    val pageCount by remember(pageSize, dataSize) {
        derivedStateOf {
            if (dataSize % pageSize == 0) {
                dataSize / pageSize
            } else {
                dataSize / pageSize + 1
            }
        }
    }

    LaunchedEffect(pageIndex, pageSize) {
        onPaginationChanged(
            pageIndex * pageSize,
            pageSize
        )
    }

    BoxWithConstraints(modifier) {
        val showPageSize = maxWidth >= 500.dp
        Row(Modifier.fillMaxWidth(), Arrangement.End, Alignment.CenterVertically) {
            if (showPageSize) {
                Text("Rows per page", Modifier.padding(end = 4.dp))
                DropdownPicker(pageSize, pageSizeOptions, onOptionPicked = { pageSize = it })
            }
            val firstIndex = pageIndex * pageSize + 1
            val lastIndex = ((pageIndex + 1) * pageSize).coerceAtMost(dataSize)
            Text("$firstIndex-$lastIndex of $dataSize", Modifier.padding(horizontal = 16.dp))
            IconButton(
                onClick = {
                    pageIndex = 0
                },
                Modifier.padding(end = 4.dp).alpha(if (pageIndex > 0) 1f else 0.38f),
                enabled = pageIndex > 0
            ) { Image(imageVector = DataTableIcons.FirstPage, contentDescription = "Jump to start") }
            IconButton(
                onClick = {
                    pageIndex--
                },
                Modifier.padding(end = 4.dp).alpha(if (pageIndex > 0) 1f else 0.38f),
                enabled = pageIndex > 0
            ) { Image(imageVector = DataTableIcons.ChevronLeft, contentDescription = "Previous page") }
            IconButton(
                onClick = {
                    pageIndex++
                },
                Modifier.padding(end = 4.dp).alpha(if (pageIndex < pageCount - 1) 1f else 0.38f),
                enabled = pageIndex < pageCount - 1
            ) { Image(imageVector = DataTableIcons.ChevronRight, contentDescription = "Next page") }
            IconButton(
                onClick = {
                    pageIndex = pageCount - 1
                },
                Modifier.padding(end = 4.dp).alpha(if (pageIndex < pageCount - 1) 1f else 0.38f),
                enabled = pageIndex < pageCount - 1
            ) { Image(imageVector = DataTableIcons.LastPage, contentDescription = "Jump to end") }
        }
    }
}

