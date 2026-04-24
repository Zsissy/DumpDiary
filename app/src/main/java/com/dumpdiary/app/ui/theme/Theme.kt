package com.dumpdiary.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val LightColors = lightColorScheme(
    primary = Color(0xFF705A52),
    onPrimary = Color(0xFFFFF7F5),
    primaryContainer = Color(0xFFFADCD2),
    onPrimaryContainer = Color(0xFF624D45),
    secondary = Color(0xFF8A685E),
    onSecondary = Color(0xFFFFF7F5),
    secondaryContainer = Color(0xFFF1D9D0),
    onSecondaryContainer = Color(0xFF5A433B),
    tertiary = Color(0xFF83524F),
    onTertiary = Color(0xFFFFF7F6),
    tertiaryContainer = Color(0xFFFDBCB8),
    onTertiaryContainer = Color(0xFF643735),
    background = Color(0xFFFDF8F6),
    onBackground = Color(0xFF343230),
    surface = Color(0xFFFDF8F6),
    onSurface = Color(0xFF343230),
    surfaceVariant = Color(0xFFE7E1DF),
    onSurfaceVariant = Color(0xFF615E5C),
    outline = Color(0xFF7D7A78),
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFFADCD2),
    onPrimary = Color(0xFF4E3B34),
    secondary = Color(0xFFE6C9BF),
    onSecondary = Color(0xFF4B362F),
    tertiary = Color(0xFFFDBCB8),
    onTertiary = Color(0xFF4D2423),
    background = Color(0xFF1C1817),
    onBackground = Color(0xFFECE7E4),
    surface = Color(0xFF1C1817),
    onSurface = Color(0xFFECE7E4),
    surfaceVariant = Color(0xFF494543),
    onSurfaceVariant = Color(0xFFCBC5C2),
)

private val AppShapes = Shapes(
    extraSmall = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
    small = androidx.compose.foundation.shape.RoundedCornerShape(18.dp),
    medium = androidx.compose.foundation.shape.RoundedCornerShape(24.dp),
    large = androidx.compose.foundation.shape.RoundedCornerShape(32.dp),
    extraLarge = androidx.compose.foundation.shape.RoundedCornerShape(40.dp),
)

@Composable
fun DumpDiaryTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        typography = Typography(),
        shapes = AppShapes,
        content = content,
    )
}
