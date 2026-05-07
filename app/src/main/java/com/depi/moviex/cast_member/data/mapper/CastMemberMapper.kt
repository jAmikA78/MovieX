package com.depi.moviex.cast_member.data.mapper

import com.depi.moviex.cast_member.data.remote.models.CastMemberDto
import com.depi.moviex.cast_member.data.remote.models.CreditDto
import com.depi.moviex.cast_member.domain.models.CastMember
import com.depi.moviex.cast_member.domain.models.KnownForMovie

class CastMemberMapper {

    fun toDomain(dto: CastMemberDto, knownForCredits: List<KnownForMovie> = emptyList()): CastMember {
        return CastMember(
            id = dto.id,
            name = dto.name,
            biography = dto.biography ?: "",
            birthday = dto.birthday ?: "Unknown",
            deathday = dto.deathday,
            placeOfBirth = dto.placeOfBirth ?: "Unknown",
            profilePath = dto.profilePath,
            knownForDepartment = dto.knownForDepartment ?: "Unknown",
            popularity = dto.popularity,
            knownFor = knownForCredits
        )
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