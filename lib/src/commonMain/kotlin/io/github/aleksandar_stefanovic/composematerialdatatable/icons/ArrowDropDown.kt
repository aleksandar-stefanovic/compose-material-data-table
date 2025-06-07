package io.github.aleksandar_stefanovic.composematerialdatatable.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

internal val ArrowDropDown: ImageVector
    get() {
        if (_ArrowDropDown != null) {
            return _ArrowDropDown!!
        }
        _ArrowDropDown = ImageVector.Builder(
            name = "ArrowDropDown",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(fill = SolidColor(Color(0xFF000000))) {
                moveTo(480f, 600f)
                lineTo(280f, 400f)
                horizontalLineToRelative(400f)
                lineTo(480f, 600f)
                close()
            }
        }.build()

        return _ArrowDropDown!!
    }

@Suppress("ObjectPropertyName")
private var _ArrowDropDown: ImageVector? = null
