import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)

    //kotlinxSerialization : kotlinversion
    kotlin("plugin.serialization") version "2.1.0"
    id("app.cash.sqldelight") version "2.0.2"
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }



    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    jvm("desktop")

    sourceSets {
        val desktopMain by getting

        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)

            //Client de requêtes spécifique à Android
            implementation("io.ktor:ktor-client-okhttp:3.0.0")

            //Si besoin du context
            implementation("io.insert-koin:koin-android:4.1.+")

            //Base de données
            implementation("app.cash.sqldelight:android-driver:2.0.2")

            //Location
            implementation("com.google.android.gms:play-services-location:21.3.0")
        }
        iosMain.dependencies {
            //Client de requêtes spécifique à iOS
            implementation("io.ktor:ktor-client-darwin:3.0.0")
            implementation("app.cash.sqldelight:native-driver:2.0.2")
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            // (les interfaces en gros)
            implementation("io.ktor:ktor-client-core:3.0.0")
            //Intégration avec la bibliothèque de serialisation, gestion des headers
            implementation("io.ktor:ktor-client-content-negotiation:3.0.0")
            //Serialisation JSON
            implementation("io.ktor:ktor-serialization-kotlinx-json:3.0.0")

            //Même librairie que pour Android sauf qu'il y a org.jetbrains. avant
            implementation("org.jetbrains.androidx.lifecycle:lifecycle-viewmodel-compose:2.9.0")

            implementation("org.jetbrains.compose.material:material-icons-extended:1.7.3")

            //ImageLoader
            implementation("io.coil-kt.coil3:coil-network-ktor3:3.0.0")
            implementation("io.coil-kt.coil3:coil-compose:3.0.0")

            implementation("org.jetbrains.androidx.navigation:navigation-compose:2.9.+")

            //Injection dépendance KOIN
            implementation("io.insert-koin:koin-compose:4.1.+")
            implementation("io.insert-koin:koin-compose-viewmodel:4.1.+")
            implementation("io.insert-koin:koin-compose-viewmodel-navigation:4.1.+")

            //Base de données
            implementation("app.cash.sqldelight:runtime:2.0.2")
            implementation("app.cash.sqldelight:coroutines-extensions:2.0.2")

            api("dev.icerock.moko:permissions:0.18.0")
            api("dev.icerock.moko:permissions-compose:0.18.0")
        }
        commonTest.dependencies {
            implementation("io.insert-koin:koin-test:4.0.0")
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.1")
            implementation(libs.kotlin.test)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)

            //Client de requêtes spécifique au bureau sur JVM donc même qu'Android
            implementation("io.ktor:ktor-client-okhttp:3.0.0")

            implementation("app.cash.sqldelight:sqlite-driver:2.0.2")
        }
    }
}

android {
    namespace = "org.example.project"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "org.example.project"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation("io.ktor:ktor-client-okhttp-jvm:3.0.0")
    debugImplementation(compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "org.example.project.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "org.example.project"
            packageVersion = "1.0.0"
        }
    }
}
//Faire une 1er synchronisation avant d'ajouter ce bloc, à mettre au niveau d'indentation 0
sqldelight {
    databases {
        create("MyDatabase") { //Nom de la classe qui sera généré pour représenter votre base
            //Ou il doit aller chercher les fichiers .sq
            packageName.set("org.example.project.db")
        }
    }
}
