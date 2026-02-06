package com.example.pethelper

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

class Constants {
    companion object {
        val GRADIENT_BRUSH = Brush.verticalGradient(
            colors = listOf(
                Color(color = 0xFFFFF8F0),
                Color(color = 0xFFFFE0B2),
                Color(color = 0xFFFFF8F0)
            )
        )
    }
}