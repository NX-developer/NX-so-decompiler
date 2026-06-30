package com.kyhsgeekcode.disassembler.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColors = darkColorScheme(
    primary = NxTeal,
    onPrimary = Color.White,
    secondary = NxAccent,
    onSecondary = Color.White,
    background = NxNight,
    onBackground = Color(0xFFE2E8F0),
    surface = NxSlate,
    onSurface = Color(0xFFE2E8F0),
)

private val LightColors = lightColorScheme(
    primary = NxTealDark,
    onPrimary = Color.White,
    secondary = NxAccent,
    onSecondary = Color.White,
    background = NxSurfaceLight,
    onBackground = NxNight,
    surface = Color.White,
    onSurface = NxNight,
)

/**
 * Material3 theme for NX so decompiler.
 *
 * Uses Android 12+ dynamic (Material You) colors when available, otherwise falls
 * back to the NX brand palette. Follows the system light/dark setting by default.
 */
@Composable
fun NxTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColors
        else -> LightColors
    }
    MaterialTheme(
        colorScheme = colorScheme,
        content = content,
    )
}
