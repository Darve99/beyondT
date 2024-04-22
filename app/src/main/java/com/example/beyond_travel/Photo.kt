package com.example.beyond_travel

data class Photo(val photoUrl: String, val description: String) {
    val longitude: String
        get() {
            return "Long: $longitude"
        }
    val imageUrl: Any
        get() {
            return photoUrl
        }
    val latitude: String
        get() {
            return "Lat: $latitude"
        }
}
