package com.depi.moviex.ui.theme.screens.cast_member

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.depi.moviex.cast_member.domain.models.CastMember
import com.depi.moviex.cast_member.domain.repository.CastMemberRepository
import com.depi.moviex.utils.Response
import com.depi.moviex.utils.collectAndHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CastMemberState(
    val castMember: CastMember? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class CastMemberViewModel @Inject constructor(
    private val repository: CastMemberRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val personId: Int = checkNotNull(savedStateHandle["personId"])

    private val _castMemberState = MutableStateFlow(CastMemberState())
    val castMemberState = _castMemberState.asStateFlow()

    init {
        fetchCastMember(personId)
    }

    private fun fetchCastMember(personId: Int) = viewModelScope.launch {
        repository.fetchCastMember(personId).collectAndHandle(
            onError = { error ->
                _castMemberState.update {
                    it.copy(isLoading = false, error = error?.message)
                }
            },
            onLoading = {
                _castMemberState.update {
                    it.copy(isLoading = true, error = null)
                }
            }
        ) { castMember ->
            _castMemberState.update {
                it.copy(isLoading = false, error = null, castMember = castMember)
            }
        }
    }
}