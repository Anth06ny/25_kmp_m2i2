package org.example.project.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import dev.icerock.moko.permissions.PermissionState
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.compose_multiplatform
import org.example.project.di.initKoin
import org.example.project.model.Photographer
import org.example.project.ui.LocationPermissionButton
import org.example.project.ui.Routes
import org.example.project.ui.theme.AppTheme
import org.example.project.viewmodel.MainViewModel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.mp.KoinPlatform


@Preview()
@Composable
fun PhotographerPreview() {
    initKoin()
    AppTheme {
        val mainViewModel = KoinPlatform.getKoin().get<MainViewModel>()
        mainViewModel.loadFakeData(true, "Une erreur est survenue")
        PhotographersScreen(mainViewModel = mainViewModel)
    }
}

@Composable
fun PhotographersScreen(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel,
    navHostController: NavHostController? = null
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {

        val list = mainViewModel.dataList.collectAsStateWithLifecycle().value
        val runInProgress = mainViewModel.runInProgress.collectAsStateWithLifecycle().value
        val errorMessage = mainViewModel.errorMessage.collectAsStateWithLifecycle().value
        val location by mainViewModel.location.collectAsStateWithLifecycle()


        var permissionState by remember { mutableStateOf<PermissionState?>(null) }

        LocationPermissionButton {
            println("callback $it")
            permissionState = it
            if(it == PermissionState.Granted) {
                mainViewModel.getCurrentLocation()
            }
        }

        AnimatedVisibility(visible = runInProgress) {
            CircularProgressIndicator()
        }

        AnimatedVisibility(visible = errorMessage.isNotBlank()) {
            Text(
                text = errorMessage,
                modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.errorContainer)
            )
        }

        Text(
            text = "${permissionState?.toString() ?: "-"} : $location",
            color = MaterialTheme.colorScheme.onTertiaryContainer,
            modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.tertiaryContainer)
        )

        LazyColumn(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(list.size) {
                PhotographerItem(
                    list[it],
                    onItemClick = {
                        navHostController?.navigate(Routes.PhotographerRoute(it.id))
                    }
                )
            }
        }

    }
}

@Composable
fun PhotographerItem(photographer: Photographer, onItemClick: (Photographer) -> Unit = {}) {

    var expended by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onItemClick(photographer) },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = photographer.photoUrl,
                contentDescription = photographer.stageName,
                contentScale = ContentScale.Crop,
                placeholder = painterResource(Res.drawable.compose_multiplatform),
                error = painterResource(Res.drawable.compose_multiplatform),
                onError = { println(it) },
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = photographer.stageName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = if (expended) photographer.story else (photographer.story.take(20) + "..."),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.clickable { expended = !expended }.animateContentSize()
                )
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Voir les photos",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}