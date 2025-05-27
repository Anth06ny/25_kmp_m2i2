package org.example.project

import androidx.compose.runtime.Composable
import org.example.project.ui.AppNavigation
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview()
@Composable
fun AppPreview() {
    App()
}

@Composable
fun App() {
        AppNavigation()
}