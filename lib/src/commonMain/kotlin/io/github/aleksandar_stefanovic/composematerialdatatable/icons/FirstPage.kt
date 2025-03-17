package io.github.aleksandar_stefanovic.composematerialdatatable.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

internal val DataTableIcons.FirstPage: ImageVector
    get() {
        if (_FirstPage != null) {
            return _FirstPage!!
        }
        _FirstPage = ImageVector.Builder(
            name = "FirstPage",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f,
            autoMirror = true
        ).apply {
            path(fill = SolidColor(Color(0xFF000000))) {
                moveTo(18.41f, 16.59f)
                lineTo(13.82f, 12f)
                lineToRelative(4.59f, -4.59f)
                lineTo(17f, 6f)
                lineToRelative(-6f, 6f)
                lineToRelative(6f, 6f)
                close()
                moveTo(6f, 6f)
                horizontalLineToRelative(2f)
                verticalLineToRelative(12f)
                horizontalLineTo(6f)
                close()
            }
        }.build()

        return _FirstPage!!
    }

@Suppress("ObjectPropertyName")
private var _FirstPage: ImageVector? = null
