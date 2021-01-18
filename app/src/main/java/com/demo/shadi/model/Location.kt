package com.demo.shadi.model


data class Location(
    val city: String = "",
    val state: String = "",
    val country: String = ""
) {
    fun getFullAddress(): String {
        return "$city $state $country"
    }
}