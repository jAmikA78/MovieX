package com.depi.moviex.cast_member.data.mapper

import com.depi.moviex.cast_member.data.remote.models.CastMemberDto
import com.depi.moviex.cast_member.data.remote.models.CreditDto
import com.depi.moviex.cast_member.domain.models.CastMember
import com.depi.moviex.cast_member.domain.models.KnownForMovie
import com.depi.moviex.common.data.ApiMapper

class CastMemberMapper : ApiMapper<CastMember, CastMemberDto> {

    override fun mapToDomain(entity: CastMemberDto): CastMember {
        return CastMember(
            id = entity.id,
            name = entity.name,
            biography = entity.biography ?: "",
            birthday = entity.birthday ?: "Unknown",
            deathday = entity.deathday,
            placeOfBirth = entity.placeOfBirth ?: "Unknown",
            profilePath = entity.profilePath,
            knownForDepartment = entity.knownForDepartment ?: "Unknown",
            popularity = entity.popularity,
            knownFor = emptyList()
        )
    }

    fun toDomain(dto: CastMemberDto, knownForCredits: List<KnownForMovie> = emptyList()): CastMember {
        return mapToDomain(dto).copy(knownFor = knownForCredits)
    }

    fun mapKnownFor(credits: List<CreditDto>): List<KnownForMovie> {
        return credits.map { credit ->
            KnownForMovie(
                id = credit.id,
                title = credit.title ?: credit.name ?: "Unknown",
                posterPath = credit.posterPath,
                backdropPath = credit.backdropPath,
                voteAverage = credit.voteAverage,
                releaseDate = credit.releaseDate ?: credit.firstAirDate ?: "",
                mediaType = credit.mediaType
            )
        }
    }
}
