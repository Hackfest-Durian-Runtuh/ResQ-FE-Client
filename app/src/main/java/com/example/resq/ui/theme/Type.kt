package com.example.resq.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.resq.R

// Set of Material typography styles to start with
val Typography = Typography(
    displayLarge = TextStyle(
        fontSize = 57.sp,
        lineHeight = 64.sp,
        fontFamily = FontFamily(Font(R.font.nunito)),
        fontWeight = FontWeight(400),
    ),
    displayMedium = TextStyle(
        fontSize = 45.sp,
        lineHeight = 52.sp,
        fontFamily = FontFamily(Font(R.font.nunito)),
        fontWeight = FontWeight(400),
    ),
    displaySmall = TextStyle(
        fontSize = 36.sp,
        lineHeight = 44.sp,
        fontFamily = FontFamily(Font(R.font.nunito)),
        fontWeight = FontWeight(400),
    ),
    headlineLarge = TextStyle(
        fontSize = 32.sp,
        lineHeight = 40.sp,
        fontFamily = FontFamily(Font(R.font.nunito)),
        fontWeight = FontWeight(400),
    ),
    headlineMedium = TextStyle(
        fontSize = 24.sp,
        lineHeight = 32.sp,
        fontFamily = FontFamily(Font(R.font.nunito)),
        fontWeight = FontWeight(700),
    ),
    headlineSmall = TextStyle(
        fontSize = 20.sp,
        lineHeight = 28.sp,
        fontFamily = FontFamily(Font(R.font.nunito)),
        fontWeight = FontWeight(700),
    ),
    titleLarge = TextStyle(
        fontSize = 18.sp,
        lineHeight = 22.sp,
        fontFamily = FontFamily(Font(R.font.nunito)),
        fontWeight = FontWeight(600)
    ),
    titleMedium = TextStyle(
        fontSize = 16.sp,
        lineHeight = 22.sp,
        fontFamily = FontFamily(Font(R.font.nunito)),
        fontWeight = FontWeight(700),
        color = Color(0xFF000000),
    ),
    titleSmall = TextStyle(
        fontSize = 14.sp,
        lineHeight = 20.sp,
        fontFamily = FontFamily(Font(R.font.nunito)),
        fontWeight = FontWeight(500),
        letterSpacing = 0.1.sp,
    ),
    bodyLarge = TextStyle(
        fontSize = 14.sp,
        lineHeight = 20.sp,
        fontFamily = FontFamily(Font(R.font.nunito)),
        fontWeight = FontWeight(500),
        letterSpacing = 0.1.sp,
    ),
    bodyMedium = TextStyle(
        fontSize = 12.sp,
        lineHeight = 20.sp,
        fontFamily = FontFamily(Font(R.font.nunito)),
        fontWeight = FontWeight(500),
        letterSpacing = 0.5.sp,
    ),
    bodySmall = TextStyle(
        fontSize = 11.sp,
        lineHeight = 16.sp,
        fontFamily = FontFamily(Font(R.font.nunito)),
        fontWeight = FontWeight(500),
        letterSpacing = 0.5.sp,
    ),
    labelLarge = TextStyle(
        fontSize = 16.sp,
        lineHeight = 24.sp,
        fontFamily = FontFamily(Font(R.font.nunito)),
        fontWeight = FontWeight(500),
        letterSpacing = 0.2.sp,
    ),
    labelMedium = TextStyle(
        fontSize = 14.sp,
        lineHeight = 20.sp,
        fontFamily = FontFamily(Font(R.font.nunito)),
        fontWeight = FontWeight(500),
        letterSpacing = 0.1.sp,
    ),
    labelSmall = TextStyle(
        fontSize = 12.sp,
        lineHeight = 16.sp,
        fontFamily = FontFamily(Font(R.font.nunito)),
        fontWeight = FontWeight(400),
    )
)