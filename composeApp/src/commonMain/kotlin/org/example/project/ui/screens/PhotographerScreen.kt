package org.example.project.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.compose_multiplatform
import org.example.project.model.Photographer
import org.example.project.ui.PictureGallery
import org.example.project.ui.theme.AppTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun PhotographerScreenPreview() {
    AppTheme {
        val photograph = Photographer(
            id = 1,
            stageName = "Bob la Menace",
            photoUrl = "https://picsum.photos/789",
            story = "Ancien agent secret, Bob a troqué ses gadgets pour un appareil photo après une mission qui a mal tourné. Il traque désormais les instants volés plutôt que les espions.",
            portfolio = listOf(
                "https://picsum.photos/789",
                "https://example.com/photo2.jpg",
                "https://example.com/photo3.jpg"
            )
        )
        PhotographerScreen(photographer = photograph)
    }
}


@Composable
fun PhotographerScreen(
    modifier: Modifier = Modifier,
    photographer: Photographer
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Image de couverture
            AsyncImage(
                model = photographer.photoUrl,
                error = painterResource(Res.drawable.compose_multiplatform),
                contentDescription = "Photo principale",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Nom du photographe
                Text(
                    text = photographer.stageName,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 26.sp
                    ),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Histoire du photographe
                Text(
                    text = photographer.story,
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Galerie d'images
                Text(
                    text = "Galerie de photos",
                    style = MaterialTheme.typography.titleMedium.copy(fontSize = 20.sp),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                PictureGallery(photographer.portfolio)
            }
        }
    }
}

@Composable
fun ImageCard(imageUrl: String) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .width(160.dp)
            .height(160.dp)
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = "Image",
            placeholder = painterResource(Res.drawable.compose_multiplatform),
            onError = { println(it) },
            error = painterResource(Res.drawable.compose_multiplatform),
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}