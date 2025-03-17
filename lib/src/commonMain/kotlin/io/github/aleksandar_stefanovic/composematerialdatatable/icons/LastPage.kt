package io.github.aleksandar_stefanovic.composematerialdatatable.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

internal val DataTableIcons.LastPage: ImageVector
    get() {
        if (_LastPage != null) {
            return _LastPage!!
        }
        _LastPage = ImageVector.Builder(
            name = "LastPage",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f,
            autoMirror = true
        ).apply {
            path(fill = SolidColor(Color(0xFF000000))) {
                moveToRelative(280f, 720f)
                lineToRelative(-56f, -56f)
                lineToRelative(184f, -184f)
                lineToRelative(-184f, -184f)
                lineToRelative(56f, -56f)
                lineToRelative(240f, 240f)
                lineToRelative(-240f, 240f)
                close()
                moveTo(640f, 720f)
                verticalLineToRelative(-480f)
                horizontalLineToRelative(80f)
                verticalLineToRelative(480f)
                horizontalLineToRelative(-80f)
                close()
            }
        }.build()

        return _LastPage!!
    }

@Suppress("ObjectPropertyName")
private var _LastPage: ImageVector? = null
