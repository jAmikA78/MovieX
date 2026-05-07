package com.depi.moviex.cast_member.data.repo_impl

import com.depi.moviex.cast_member.data.mapper.CastMemberMapper
import com.depi.moviex.cast_member.data.remote.api.CastMemberApiService
import com.depi.moviex.cast_member.domain.models.CastMember
import com.depi.moviex.cast_member.domain.repository.CastMemberRepository
import com.depi.moviex.utils.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CastMemberRepositoryImpl @Inject constructor(
    private val apiService: CastMemberApiService,
    private val mapper: CastMemberMapper
) : CastMemberRepository {

    override fun fetchCastMember(personId: Int): Flow<Response<CastMember>> = flow {
        emit(Response.Loading())
        try {
            val dto = apiService.fetchCastMember(personId)
            val creditsDto = apiService.fetchCombinedCredits(personId)
            val knownFor = mapper.mapKnownFor(creditsDto.cast)
            val castMember = mapper.toDomain(dto, knownFor)
            emit(Response.Success(castMember))
        } catch (e: Exception) {
            emit(Response.Error(e))
        }
    }
}