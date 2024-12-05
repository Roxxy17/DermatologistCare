package com.example.dermatologistcare.ui.theme

import androidx.compose.foundation.MutatePriority
import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.dermatologistcare.R

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)

val coolveticaFontFamily = FontFamily(
    Font(R.font.coolvetica_rg, FontWeight.Normal),       // Regular (Rg.otf)
    Font(R.font.coolvetica_rg_cond, FontWeight.Light),   // Condensed (Rg Cond.otf)
    Font(R.font.coolvetica_hv_comp, FontWeight.Bold),    // Heavy Compact (Hv Comp.otf)
    Font(R.font.coolvetica_rg_cram, FontWeight.ExtraBold), // Crammed (Rg Cram.otf)
    Font(R.font.coolvetica_rg_it, FontWeight.Medium)     // Italic (Rg It.otf)
)
