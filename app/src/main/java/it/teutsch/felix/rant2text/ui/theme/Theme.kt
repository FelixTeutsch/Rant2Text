package it.teutsch.felix.rant2text.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Primary80,
    onPrimary = Primary20,
    secondary = Secondary80,
    onSecondary = Secondary20,
    tertiary = Tertiary80,
    onTertiary = Tertiary20,
    error = Error80,
    onError = Error20,
    primaryContainer = Primary30,
    onPrimaryContainer = Primary90,
    secondaryContainer = Secondary30,
    onSecondaryContainer = Secondary90,
    tertiaryContainer = Tertiary30,
    onTertiaryContainer = Tertiary90,
    errorContainer = Error30,
    onErrorContainer = Error90,
    surface = Neutral6,
    onSurface = Neutral90,
    onSurfaceVariant = NeutralVariant80,
    outline = NeutralVariant60,
    outlineVariant = NeutralVariant20,
    inverseSurface = Neutral90,
    inverseOnSurface = Neutral20,
    inversePrimary = Primary40,
    scrim = Neutral0,
    background = BackgroundDark,
    onBackground = OnBackgroundDark,
    surfaceTint = SurfaceTintDark,
    surfaceVariant = SurfaceVariantDark,
)

private val LightColorScheme = lightColorScheme(
    primary = Primary40,
    onPrimary = Primary100,
    secondary = Secondary40,
    onSecondary = Secondary100,
    tertiary = Tertiary40,
    onTertiary = Tertiary100,
    error = Error40,
    onError = Error100,
    primaryContainer = Primary90,
    onPrimaryContainer = Primary10,
    secondaryContainer = Secondary90,
    onSecondaryContainer = Secondary10,
    tertiaryContainer = Tertiary90,
    onTertiaryContainer = Tertiary10,
    errorContainer = Error90,
    onErrorContainer = Error10,
    surface = Neutral98,
    onSurface = Neutral10,
    onSurfaceVariant = NeutralVariant30,
    outline = NeutralVariant50,
    outlineVariant = NeutralVariant80,
    inverseSurface = Neutral20,
    inverseOnSurface = Neutral95,
    inversePrimary = Primary80,
    scrim = Neutral0,
    background = BackgroundLight,
    onBackground = OnBackgroundLight,
    surfaceTint = SurfaceTintLight,
    surfaceVariant = SurfaceVariantLight,
)

@Composable
fun Rant2TextTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        /*dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }*/

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}