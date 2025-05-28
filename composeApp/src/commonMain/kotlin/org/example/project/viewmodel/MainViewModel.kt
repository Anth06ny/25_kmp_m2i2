package org.example.project.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.example.project.db.MyDatabase
import org.example.project.di.initKoin
import org.example.project.model.Photographer
import org.example.project.model.PhotographerAPI
import org.example.project.services.Location
import org.example.project.services.LocationService
import org.koin.mp.KoinPlatform

fun main() {

    initKoin()
    val viewModel = KoinPlatform.getKoin().get<MainViewModel>()

    //Affichage de la liste ou du message d'erreur
    println("List : ${viewModel.dataList.value}")
}

class MainViewModel(
    val photographerAPI: PhotographerAPI,
    val myDatabase: MyDatabase,
    val locationService: LocationService
    ) : ViewModel() {

    private val _dataList = MutableStateFlow(emptyList<Photographer>())
    private val jsonParser = Json { prettyPrint = true }
    val dataList = _dataList.asStateFlow()

    private val _runInProgress = MutableStateFlow(false)
    val runInProgress = _runInProgress.asStateFlow()

    private val _errorMessage = MutableStateFlow("")
    val errorMessage = _errorMessage.asStateFlow()

    private val photographerQueries = myDatabase.photographerStorageQueries

    private val _location = MutableStateFlow<Location?>(null)
    val location = _location.asStateFlow()

    init {
        loadPhotographers()
    }



    private fun loadPhotographers() {

        _runInProgress.value = true

        viewModelScope.launch {
            try {

                val photographers = photographerAPI.loadPhotographers()
                //affichage
                _dataList.value = photographers

                //sauvegarde
                launch {
                    photographerQueries.transaction {

                        photographers.forEach { photographer ->
                            photographerQueries.insertOrReplacePhotographer(
                                photographer.id.toLong(),
                                photographer.stageName,
                                photographer.photoUrl,
                                photographer.story,
                                jsonParser.encodeToString(photographer.portfolio)
                            )
                        }
                    }
                }
            }
            catch (e: Exception) {
                e.printStackTrace()
                _errorMessage.value = e.message ?: "Une erreur est survenue"

                try {
                    val photographers = photographerQueries.selectAllPhotographers().executeAsList().map {
                        Photographer(
                            it.id.toInt(),
                            it.stageName,
                            it.photoUrl,
                            it.story,
                            jsonParser.decodeFromString(it.portfolio)
                        )
                    }
                    println("photographers=$photographers")
                    _dataList.value = photographers
                }
                catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            finally {
                _runInProgress.value = false
            }
        }
    }

    //Pour la Preview
    fun loadFakeData(runInProgress: Boolean = false, errorMessage: String = "") {
        _runInProgress.value = runInProgress
        _errorMessage.value = errorMessage
        _dataList.value = listOf(
            Photographer(
                id = 1,
                stageName = "Bob la Menace",
                photoUrl = "https://www.amonteiro.fr/img/fakedata.com/bob.jpg",
                story = "Ancien agent secret, Bob a troqué ses gadgets pour un appareil photo après une mission qui a mal tourné. Il traque désormais les instants volés plutôt que les espions.",
                portfolio = listOf(
                    "https://example.com/photo1.jpg",
                    "https://example.com/photo2.jpg",
                    "https://example.com/photo3.jpg"
                )
            ),
            Photographer(
                id = 2,
                stageName = "Jean-Claude Flash",
                photoUrl = "https://www.amonteiro.fr/img/fakedata.com/jc.jpg",
                story = "Ancien champion de rodéo, il s’est reconverti en photographe après une chute mémorable. Maintenant, il dompte la lumière comme un vrai cow-boy.",
                portfolio = listOf(
                    "https://example.com/photo4.jpg",
                    "https://example.com/photo5.jpg",
                    "https://example.com/photo6.jpg"
                )
            )
        )
    }

    fun getCurrentLocation() {
        locationService.getCurrentLocation {
            _location.value = it
        }
    }
}
