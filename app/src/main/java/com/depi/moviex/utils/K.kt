package com.depi.moviex.utils

import java.util.Locale

object K {
    const val BASE_URL = "https://api.themoviedb.org/3/"
    const val BASE_IMAGE_URL = "https://image.tmdb.org/t/p/w500/"
    const val MOVIE_DETAIL_ENDPOINT = "movie"
    const val MOVIE_ACTOR_ENDPOINT = "person"

    // Auth endpoints
    const val AUTH_TOKEN_ENDPOINT = "authentication/token/new"
    const val AUTH_TOKEN_VALIDATE_ENDPOINT = "authentication/token/validate_with_login"
    const val AUTH_SESSION_ENDPOINT = "authentication/session/new"

    fun getLanguageCode(): String {
        val lang = Locale.getDefault().language
        return if (lang == "ar") "ar-SA" else "en-US"
    }
}
