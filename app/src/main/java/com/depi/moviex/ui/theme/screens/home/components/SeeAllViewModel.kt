package com.depi.moviex.ui.theme.screens.home.components

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.depi.moviex.movie.domain.models.Movie
import com.depi.moviex.movie.domain.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


@HiltViewModel
class SeeAllViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {


    fun getPagedMovies(category: String): Flow<PagingData<Movie>> {
        return repository.fetchTrendingMoviesPaged(category)
            .cachedIn(viewModelScope)
    }
}