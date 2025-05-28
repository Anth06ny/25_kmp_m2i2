package org.example.project.di

import app.cash.sqldelight.driver.native.NativeSqliteDriver
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.example.project.db.MyDatabase
import org.koin.dsl.module

actual fun getHttpClient() =
    HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true }, contentType = ContentType.Any)
        }

        //        engine {
//            proxy = ProxyBuilder.http("monproxy:1234")
//        }
    }

actual fun databaseModule() = module {
    single {
        val driver = NativeSqliteDriver(MyDatabase.Schema, "test.db")
        MyDatabase(driver)
    }
}