package com.example.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

private val DarkColors = darkColorScheme(
    primary = CyberGreen,
    secondary = CyberBlue,
    background = CyberDark,
    surface = CyberSurface,
    error = CyberRed,
    onPrimary = CyberDark,
    onSecondary = CyberDark,
    onBackground = Purple80,
    onSurface = Purple80,
    surfaceVariant = CyberCard
)

@Composable
fun AdminLoaderTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColors,
        typography = Typography,
        content = content
    )
}
