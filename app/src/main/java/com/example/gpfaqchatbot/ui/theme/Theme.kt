    package com.example.gpfaqchatbot.ui.theme

    import androidx.compose.foundation.isSystemInDarkTheme
    import androidx.compose.material.*
    import androidx.compose.runtime.Composable
    import androidx.compose.ui.graphics.Color


    private val LightColors = lightColors(
        primary = Color(0xFF6200EE),
        primaryVariant = Color(0xFF3700B3),
        secondary = Color(0xFF03DAC6)
    )

    private val DarkColors = darkColors(
        primary = Color(0xFFBB86FC),
        primaryVariant = Color(0xFF3700B3),
        secondary = Color(0xFF03DAC6)
    )

    @Composable
    fun GPFaqChatBotTheme(
        darkTheme: Boolean = isSystemInDarkTheme(),
        content: @Composable () -> Unit
    ) {
        val colors = if (darkTheme) DarkColors else LightColors
        MaterialTheme(
            colors = colors,
            typography = Typography,
            shapes = Shapes,
            content = content
        )
    }
