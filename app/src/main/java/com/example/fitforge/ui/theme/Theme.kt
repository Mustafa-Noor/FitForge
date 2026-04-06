package com.example.fitforge.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val FitForgeDarkColorScheme = darkColorScheme(
    primary = FitOrange,
    onPrimary = Color.White,
    primaryContainer = FitOrangeDark,
    secondary = FitSurface2,
    onSecondary = FitTextPrimary,
    background = FitBg,
    onBackground = FitTextPrimary,
    surface = FitSurface,
    onSurface = FitTextPrimary,
    surfaceVariant = FitSurface2,
    onSurfaceVariant = FitTextSecondary,
    outline = FitBorder,
    error = FitError,
    onError = Color.White
)

@Composable
fun FitForgeTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = FitForgeDarkColorScheme,
        typography = FitForgeTypography,
        content = content
    )
}