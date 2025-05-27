package org.example.project.model

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

//Dans commonMain/model
suspend fun main() {
   // println(PhotographerAPI.loadPhotographers().joinToString(separator = "\n\n"))
    println(PhotographerAPI.loadPhotographers())
}

object PhotographerAPI {
    private const val API_URL =
        "https://www.amonteiro.fr/api/photographers"

    //Déclaration du client
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true }, contentType = ContentType.Any)
        }

//        engine {
//            proxy = ProxyBuilder.http("monproxy:1234")
//        }
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

