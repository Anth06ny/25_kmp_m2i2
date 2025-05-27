package org.example.project

import androidx.compose.runtime.Composable
import org.example.project.ui.screens.PhotographerScreen
import org.example.project.ui.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview()
@Composable
fun AppPreview() {
    App()
}

@Composable
fun App() {
    AppTheme {
        PhotographerScreen()
    }
}