package com.example.fitforge.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val FitForgeTypography = Typography(
    headlineLarge = TextStyle(fontWeight = FontWeight.ExtraBold, fontSize = 28.sp, color = FitTextPrimary),
    headlineMedium = TextStyle(fontWeight = FontWeight.Bold, fontSize = 22.sp, color = FitTextPrimary),
    headlineSmall = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp, color = FitTextPrimary),
    bodyLarge = TextStyle(fontWeight = FontWeight.Normal, fontSize = 16.sp, color = FitTextPrimary),
    bodyMedium = TextStyle(fontWeight = FontWeight.Normal, fontSize = 14.sp, color = FitTextPrimary),
    bodySmall = TextStyle(fontWeight = FontWeight.Normal, fontSize = 12.sp, color = FitTextSecondary),
    labelLarge = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = FitTextPrimary),
    labelMedium = TextStyle(fontWeight = FontWeight.Medium, fontSize = 12.sp, color = FitTextSecondary),
    labelSmall = TextStyle(fontWeight = FontWeight.Medium, fontSize = 10.sp, color = FitTextSecondary)
)