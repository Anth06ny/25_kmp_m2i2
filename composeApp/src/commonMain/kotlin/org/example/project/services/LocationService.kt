package org.example.project.services

data class Location(val latitude: Double, val longitude: Double)

expect class LocationService() {
    fun getCurrentLocation(gotLocation: (Location?) -> Unit)
}