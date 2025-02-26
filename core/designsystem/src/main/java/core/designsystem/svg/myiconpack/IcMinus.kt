package core.designsystem.svg.myiconpack

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import core.designsystem.svg.MyIconPack

public val MyIconPack.IcMinus: ImageVector
    get() {
        if (_icMinus != null) {
            return _icMinus!!
        }
        _icMinus = Builder(name = "IcMinus", defaultWidth = 24.0.dp, defaultHeight = 24.0.dp,
                viewportWidth = 24.0f, viewportHeight = 24.0f).apply {
            path(fill = SolidColor(Color(0xFF2E2E34)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(11.0f, 11.0f)
                horizontalLineTo(19.75f)
                curveTo(20.1642f, 11.0f, 20.5f, 11.3358f, 20.5f, 11.75f)
                curveTo(20.5f, 12.1642f, 20.1642f, 12.5f, 19.75f, 12.5f)
                horizontalLineTo(11.0f)
                horizontalLineTo(3.75f)
                curveTo(3.3358f, 12.5f, 3.0f, 12.1642f, 3.0f, 11.75f)
                curveTo(3.0f, 11.3358f, 3.3358f, 11.0f, 3.75f, 11.0f)
                horizontalLineTo(11.0f)
                close()
            }
        }
        .build()
        return _icMinus!!
    }

private var _icMinus: ImageVector? = null
