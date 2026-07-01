package com.example.androidassignment4travelplannerapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val PremiumColorScheme = lightColorScheme(
    primary = BrandIndigo,
    onPrimary = Color.White,
    secondary = BrandAccent,
    onSecondary = Color.White,
    tertiary = BrandSuccess,
    background = BrandBg,
    surface = BrandSurface,
    onSurface = BrandTextPrimary,
    onSurfaceVariant = BrandTextSecondary,
    surfaceVariant = Color(0xFFF1F5F9), // Light grayish-blue for cards/placeholders
    outline = Color(0xFF94A3B8),
    outlineVariant = BrandBorder,
    error = BrandError
)

@Composable
fun AndroidAssignment4TravelPlannerAppTheme(
    darkTheme: Boolean = false, // Force premium light theme for consistent professional look
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = PremiumColorScheme,
        typography = Typography,
        content = content
    )
}
