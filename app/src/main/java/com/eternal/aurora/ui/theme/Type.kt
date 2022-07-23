package com.eternal.aurora.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.eternal.aurora.R

private val fontFamily = FontFamily(
    Font(R.font.rubik_regular),
    Font(R.font.rubik_medium, FontWeight.W500),
    Font(R.font.rubik_bold, FontWeight.Bold)
)

val typography = typographyFromDefault(
    displayLarge = TextStyle(fontFamily = fontFamily),
    displayMedium = TextStyle(fontFamily = fontFamily),
    displaySmall = TextStyle(fontFamily = fontFamily),
    headlineLarge = TextStyle(fontFamily = fontFamily),
    headlineMedium = TextStyle(fontFamily = fontFamily),
    headlineSmall = TextStyle(fontFamily = fontFamily),
    titleLarge = TextStyle(fontFamily = fontFamily),
    titleMedium = TextStyle(fontFamily = fontFamily),
    titleSmall = TextStyle(fontFamily = fontFamily),
    bodyLarge = TextStyle(fontFamily = fontFamily),
    bodyMedium = TextStyle(fontFamily = fontFamily),
    bodySmall = TextStyle(fontFamily = fontFamily),
    labelLarge = TextStyle(fontFamily = fontFamily),
    labelMedium = TextStyle(fontFamily = fontFamily),
    labelSmall = TextStyle(fontFamily = fontFamily),
)


private fun typographyFromDefault(
   displayLarge: TextStyle,
   displayMedium: TextStyle,
   displaySmall: TextStyle,
   headlineLarge: TextStyle,
   headlineMedium: TextStyle,
   headlineSmall: TextStyle,
   titleLarge: TextStyle,
   titleMedium: TextStyle,
   titleSmall: TextStyle,
   bodyLarge: TextStyle,
   bodyMedium: TextStyle,
   bodySmall: TextStyle,
   labelLarge: TextStyle,
   labelMedium: TextStyle,
   labelSmall: TextStyle
): Typography{
    val default = Typography()
    return Typography(
        displayLarge = default.displayLarge.merge(displayLarge),
        displayMedium = default.displayMedium.merge(displayMedium),
        displaySmall = default.displaySmall.merge(displaySmall),
        headlineLarge = default.headlineLarge.merge(headlineLarge),
        headlineMedium = default.headlineMedium.merge(headlineMedium),
        headlineSmall = default.headlineSmall.merge(headlineSmall),
        titleLarge = default.titleLarge.merge(titleLarge),
        titleMedium = default.titleMedium.merge(titleMedium),
        titleSmall = default.titleSmall.merge(titleSmall),
        bodyLarge = default.bodyLarge.merge(bodyLarge),
        bodyMedium = default.bodyMedium.merge(bodyMedium),
        bodySmall = default.bodySmall.merge(bodySmall),
        labelLarge = default.labelLarge.merge(labelLarge),
        labelMedium = default.labelMedium.merge(labelMedium),
        labelSmall = default.labelSmall.merge(labelSmall)
    )
}