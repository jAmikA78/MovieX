package com.depi.moviex.cast_member.domain.repository

import com.depi.moviex.cast_member.domain.models.CastMember
import com.depi.moviex.utils.Response
import kotlinx.coroutines.flow.Flow

interface CastMemberRepository {
    fun fetchCastMember(personId: Int): Flow<Response<CastMember>>
}