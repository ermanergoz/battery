package com.erman.battery.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private const val TABULAR_FIGURES = "tnum"

val Typography = Typography(
    h3 = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 48.sp,
        fontFeatureSettings = TABULAR_FIGURES
    ),
    h5 = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 22.sp,
        fontFeatureSettings = TABULAR_FIGURES
    ),
    h6 = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp
    ),
    subtitle2 = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        letterSpacing = 0.1.sp
    ),
    body1 = TextStyle(
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    body2 = TextStyle(
        fontSize = 14.sp,
        lineHeight = 20.sp
    )
)
