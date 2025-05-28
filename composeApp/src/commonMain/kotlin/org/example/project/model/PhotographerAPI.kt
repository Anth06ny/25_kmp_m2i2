package org.example.project.model

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.Serializable
import org.example.project.di.initKoin
import org.koin.mp.KoinPlatform

//Dans commonMain/model
suspend fun main() {
   // println(PhotographerAPI.loadPhotographers().joinToString(separator = "\n\n"))

    initKoin()
    val photographerAPI = KoinPlatform.getKoin().get<PhotographerAPI>()

    println(photographerAPI.loadPhotographers())
}

class PhotographerAPI(val  client: HttpClient) {



    companion object {
        private const val API_URL =
            "https://www.amonteiro.fr/api/photographers"
    }

    //GET
    suspend fun loadPhotographers(): List<Photographer> {
        return client.get(API_URL) {
//            headers {
//                append("Authorization", "Bearer YOUR_TOKEN")
//                append("Custom-Header", "CustomValue")
//            }
        }.body()
    }

    fun loadPhotographersFlow() = flow<List<Photographer>> {
        emit(client.get(API_URL).body())
    }
}

@Serializable
data class Photographer(
    val id: Int,
    val stageName: String,
    val photoUrl: String,
    val story: String,
    val portfolio: List<String>
)

