package com.depi.moviex.common

enum class MediaType(val value: String) {
    MOVIE("movie"),
    TV("tv");

    companion object {
        fun fromValue(value: String): MediaType = entries.find { it.value == value } ?: MOVIE
    }
}
