package io.github.aleksandar_stefanovic.composematerialdatatable.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

internal val DataTableIcons.FilterList: ImageVector
    get() {
        if (_FilterList != null) {
            return _FilterList!!
        }
        _FilterList = ImageVector.Builder(
            name = "FilterList",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            group(translationY = 960f) {
                path(fill = SolidColor(Color(0xFF5F6368))) {
                    moveTo(400f, -240f)
                    verticalLineToRelative(-80f)
                    horizontalLineToRelative(160f)
                    verticalLineToRelative(80f)
                    horizontalLineTo(400f)
                    close()
                    moveTo(240f, -440f)
                    verticalLineToRelative(-80f)
                    horizontalLineToRelative(480f)
                    verticalLineToRelative(80f)
                    horizontalLineTo(240f)
                    close()
                    moveTo(120f, -640f)
                    verticalLineToRelative(-80f)
                    horizontalLineToRelative(720f)
                    verticalLineToRelative(80f)
                    horizontalLineTo(120f)
                    close()
                }
            }
        }.build()

        return _FilterList!!
    }

@Suppress("ObjectPropertyName")
private var _FilterList: ImageVector? = null
