package com.app.supercompras.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.app.supercompras.R

// Set of Material typography styles to start with
val kronaFontFamily = Font(R.font.krona).toFontFamily()
val numansFontFamily = Font(R.font.numans).toFontFamily()

val Typography = Typography(
    headlineLarge = TextStyle(
        fontFamily = kronaFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp,
        color = Coral,
        lineHeight = 32.sp,
        letterSpacing = 0.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = numansFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = numansFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        color = Marinho,
        lineHeight = 24.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = numansFontFamily,
        fontWeight = FontWeight.Normal,
        color = Marinho,
        textAlign = TextAlign.Start,
        fontSize = 12.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.sp
    )
)