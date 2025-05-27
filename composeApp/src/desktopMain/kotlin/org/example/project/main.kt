package org.example.project

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "KotlinProject",
    ) {

        App()
//        val photograph = Photographer(
//            id = 1,
//            stageName = "Bob la Menace",
//            photoUrl = "https://picsum.photos/789",
//            story = "Ancien agent secret, Bob a troqué ses gadgets pour un appareil photo après une mission qui a mal tourné. Il traque désormais les instants volés plutôt que les espions.",
//            portfolio = listOf(
//                "https://picsum.photos/789",
//                "https://example.com/photo2.jpg",
//                "https://example.com/photo3.jpg"
//            )
//        )
//        PhotographerScreen(photographer= photograph)
    }
}