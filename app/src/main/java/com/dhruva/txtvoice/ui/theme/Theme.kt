package com.dhruva.txtvoice.ui.theme

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

private val DarkColorScheme = darkColorScheme(
    primary = YellowPrimary,
    onPrimary = Mustard,
    secondary = DarkSurfaceVariant,
    onSecondary = Color.White,
    background = DarkBackground,
    onBackground = Color.White,
    surface = DarkSurface,
    onSurface = Color.White,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = LightGray
)

private val LightColorScheme = lightColorScheme(
    primary = YellowPrimary,
    onPrimary = Mustard,
    secondary = DarkSurfaceVariant,
    onSecondary = Color.White,
    background = Color.White,
    onBackground = DarkBackground,
    surface = Color.White,
    onSurface = DarkBackground,
    surfaceVariant = LightGray,
    onSurfaceVariant = DarkBackground
)

@Composable
fun TxtVoiceTheme(
    darkTheme: Boolean = true,//isSystemInDarkTheme(), as of now, we're not providing support for light theme
    // Dynamic color is available on Android 12+
    // Disabled by default to maintain the specific brand identity from the image
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
