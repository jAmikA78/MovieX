package com.depi.moviex.utils

object K {
    const val BASE_URL = "https://api.themoviedb.org/3/"
    const val BASE_IMAGE_URL = "https://image.tmdb.org/t/p/w500/"
    const val MOVIE_ENDPOINT ="discover/movie"
    const val MOVIE_DETAIL_ENDPOINT ="movie"
    const val MOVIE_ACTOR_ENDPOINT ="person"
    const val TRENDING_MOVIE_ENDPOINT ="trending/movie/week"
    const val MOVIE_ID ="id"
    const val ACTOR_ID ="id"

    // Auth endpoints
    const val AUTH_TOKEN_ENDPOINT = "authentication/token/new"
    const val AUTH_TOKEN_VALIDATE_ENDPOINT = "authentication/token/validate_with_login"
    const val AUTH_SESSION_ENDPOINT = "authentication/session/new"

    // Genre endpoints
    const val TV_SHOW_ENDPOINT = "discover/tv"
    const val ACTION_ENDPOINT = "discover/movie"
    const val DRAMA_ENDPOINT = "discover/movie"
    const val COMEDY_ENDPOINT = "discover/movie"

    // Genre IDs
    const val GENRE_ACTION = 28
    const val GENRE_ADVENTURE = 12
    const val GENRE_ANIMATION = 16
    const val GENRE_COMEDY = 35
    const val GENRE_CRIME = 80
    const val GENRE_DOCUMENTARY = 99
    const val GENRE_DRAMA = 18
    const val GENRE_FAMILY = 10751
    const val GENRE_FANTASY = 14
    const val GENRE_HISTORY = 36
    const val GENRE_HORROR = 27
    const val GENRE_MUSIC = 10402
    const val GENRE_MYSTERY = 9648
    const val GENRE_ROMANCE = 10749
    const val GENRE_SCIENCE_FICTION = 878
    const val GENRE_THRILLER = 53
    const val GENRE_WAR = 10752
    const val GENRE_WESTERN = 37

    // TV Show Genre IDs
    const val GENRE_TV_ACTION_ADVENTURE = 10759
    const val GENRE_TV_ANIMATION = 16
    const val GENRE_TV_COMEDY = 35
    const val GENRE_TV_DOCUMENTARY = 99
    const val GENRE_TV_DRAMA = 18
    const val GENRE_TV_FAMILY = 10751
    const val GENRE_TV_KIDS = 10762
    const val GENRE_TV_MYSTERY = 9648
    const val GENRE_TV_NEWS = 10763
    const val GENRE_TV_REALTHY = 10764
    const val GENRE_TV_SCIFI_FANTASY = 10765
    const val GENRE_TV_TALK = 10766
    const val GENRE_TV_WAR_POLITICS = 10768
}