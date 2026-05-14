package com.depi.moviex.ui.theme.screens.home

import com.depi.moviex.movie.domain.models.Movie

data class CategoryItem(
    val name: String,
    val key: String,
    val movies: List<Movie>
)
