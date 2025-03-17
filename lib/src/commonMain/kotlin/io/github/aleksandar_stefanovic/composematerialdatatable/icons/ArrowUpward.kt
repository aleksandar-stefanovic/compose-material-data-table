package io.github.aleksandar_stefanovic.composematerialdatatable.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

internal val DataTableIcons.ArrowUpward: ImageVector
    get() {
        if (_ArrowUpward != null) {
            return _ArrowUpward!!
        }
        _ArrowUpward = ImageVector.Builder(
            name = "ArrowUpward",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f,
            autoMirror = true
        ).apply {
            path(fill = SolidColor(Color(0xFF000000))) {
                moveTo(440f, 800f)
                verticalLineToRelative(-487f)
                lineTo(216f, 537f)
                lineToRelative(-56f, -57f)
                lineToRelative(320f, -320f)
                lineToRelative(320f, 320f)
                lineToRelative(-56f, 57f)
                lineToRelative(-224f, -224f)
                verticalLineToRelative(487f)
                horizontalLineToRelative(-80f)
                close()
            }
        }.build()

        return _ArrowUpward!!
    }

@Suppress("ObjectPropertyName")
private var _ArrowUpward: ImageVector? = null
