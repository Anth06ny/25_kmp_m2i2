package org.example.project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import org.example.project.ui.AppNavigation
import org.example.project.ui.screens.PhotographerPreview

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            AppNavigation()
        }
    }

    override fun onStart() {
        super.onStart()
    }
}

@Preview(
    showBackground = true,
    widthDp = 320,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES or android.content.res.Configuration.UI_MODE_TYPE_NORMAL,
    name = "Dark",
    showSystemUi = true
)
@PreviewLightDark
@Composable
fun AppAndroidPreview() {
    AppNavigation()
}

@PreviewLightDark
@Composable
fun Photographer() {
    PhotographerPreview()
}