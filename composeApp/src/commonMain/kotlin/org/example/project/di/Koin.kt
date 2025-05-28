package org.example.project.di

import io.ktor.client.HttpClient
import org.example.project.model.PhotographerAPI
import org.example.project.services.LocationService
import org.example.project.viewmodel.MainViewModel
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

//Si besoin du contexte, pour le passer en paramètre
fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration()
        modules(apiModule, databaseModule(), LocationModule, viewModelModule)
    }

// Version pour iOS et Desktop
fun initKoin() = initKoin {}

expect fun getHttpClient() : HttpClient
expect fun databaseModule(): Module

val apiModule = module {
    //Création d'un singleton pour le client HTTP
    single {
        getHttpClient()
    }

    //Création d'un singleton pour les repository. Get injectera les instances
    //single { PhotographerAPI(get()) }

    //Nouvelle version avec injection automatique des instances
    //D'abord faire l'import de PhotographerAPI, sinon singleOf n'en voudra pas
    singleOf(::PhotographerAPI)
}

//Version spécifique au ViewModel
val viewModelModule = module {
    //D'abord faire l'import de MainViewModel, sinon viewModelOf n'en voudra pas
    viewModelOf(::MainViewModel)
}

val LocationModule = module {
    singleOf(::LocationService)
}