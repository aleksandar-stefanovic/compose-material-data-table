package io.github.aleksandar_stefanovic.composematerialdatatable.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

internal val DataTableIcons.Close: ImageVector
    get() {
        if (_Close != null) {
            return _Close!!
        }
        _Close = ImageVector.Builder(
            name = "Close",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            group(translationY = 960f) {
                path(fill = SolidColor(Color(0xFF5F6368))) {
                    moveTo(256f, -200f)
                    lineToRelative(-56f, -56f)
                    lineToRelative(224f, -224f)
                    lineToRelative(-224f, -224f)
                    lineToRelative(56f, -56f)
                    lineToRelative(224f, 224f)
                    lineToRelative(224f, -224f)
                    lineToRelative(56f, 56f)
                    lineToRelative(-224f, 224f)
                    lineToRelative(224f, 224f)
                    lineToRelative(-56f, 56f)
                    lineToRelative(-224f, -224f)
                    lineToRelative(-224f, 224f)
                    close()
                }
            }
        }.build()

        return _Close!!
    }

@Suppress("ObjectPropertyName")
private var _Close: ImageVector? = null
