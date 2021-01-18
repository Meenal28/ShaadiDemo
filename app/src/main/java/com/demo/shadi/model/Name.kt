package com.demo.shadi.model


data class Name(val title: String = "", val first: String = "", val last: String = "") {

    fun getFullName(): String {
        return "$title $first $last"
    }
}