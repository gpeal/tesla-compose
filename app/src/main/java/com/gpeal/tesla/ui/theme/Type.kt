package com.gpeal.tesla.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.gpeal.tesla.R

val Lato = FontFamily(
    Font(R.font.lato_black, weight = FontWeight.Black),
    Font(R.font.lato_blackitalic, weight = FontWeight.Black, style = FontStyle.Italic),
    Font(R.font.lato_bold, weight = FontWeight.Bold),
    Font(R.font.lato_bolditalic, weight = FontWeight.Bold, style = FontStyle.Italic),
    Font(R.font.lato_light, weight = FontWeight.Light),
    Font(R.font.lato_lightitalic, weight = FontWeight.Light, style = FontStyle.Italic),
    Font(R.font.lato_regular, weight = FontWeight.Normal),
    Font(R.font.lato_thin, weight = FontWeight.Thin),
    Font(R.font.lato_thinitalic, weight = FontWeight.Thin, style = FontStyle.Italic),
)

// Set of Material typography styles to start with
val typography = Typography(
    body1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    )
    /* Other default text styles to override
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
    */
)