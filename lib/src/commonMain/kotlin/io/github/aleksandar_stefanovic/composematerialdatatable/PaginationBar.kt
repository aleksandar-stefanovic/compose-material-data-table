package io.github.aleksandar_stefanovic.composematerialdatatable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
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
import io.github.aleksandar_stefanovic.lib.generated.resources.Res
import io.github.aleksandar_stefanovic.lib.generated.resources.chevron_left
import io.github.aleksandar_stefanovic.lib.generated.resources.chevron_right
import io.github.aleksandar_stefanovic.lib.generated.resources.first_page
import io.github.aleksandar_stefanovic.lib.generated.resources.last_page
import org.jetbrains.compose.resources.vectorResource

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

    Row(modifier, Arrangement.End, Alignment.CenterVertically) {
        Text("Rows per page", Modifier.padding(end = 4.dp))
        DropdownPicker(pageSize, pageSizeOptions, onOptionPicked = { pageSize = it })
        val firstIndex = pageIndex * pageSize + 1
        val lastIndex = ((pageIndex + 1) * pageSize).coerceAtMost(dataSize)
        Text("$firstIndex-$lastIndex of $dataSize", Modifier.padding(horizontal = 16.dp))
        IconButton(
            onClick = {
                pageIndex = 0
            },
            Modifier.padding(end = 4.dp).alpha(if (pageIndex > 0) 1f else 0.38f),
            enabled = pageIndex > 0
        ) { Image(vectorResource(Res.drawable.first_page), "Jump to start") }
        IconButton(
            onClick = {
                pageIndex--
            },
            Modifier.padding(end = 4.dp).alpha(if (pageIndex > 0) 1f else 0.38f),
            enabled = pageIndex > 0
        ) { Image(vectorResource(Res.drawable.chevron_left), "Previous page") }
        IconButton(
            onClick = {
                pageIndex++
            },
            Modifier.padding(end = 4.dp).alpha(if (pageIndex < pageCount - 1) 1f else 0.38f),
            enabled = pageIndex < pageCount - 1
        ) { Image(vectorResource(Res.drawable.chevron_right), "Next page") }
        IconButton(
            onClick = {
                pageIndex = pageCount - 1
            },
            Modifier.padding(end = 4.dp).alpha(if (pageIndex < pageCount - 1) 1f else 0.38f),
            enabled = pageIndex < pageCount - 1
        ) { Image(vectorResource(Res.drawable.last_page), "Jump to end") }
    }
}

