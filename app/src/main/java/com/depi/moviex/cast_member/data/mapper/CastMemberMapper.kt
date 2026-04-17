package com.depi.moviex.cast_member.data.mapper

import com.depi.moviex.cast_member.data.remote.models.CastMemberDto
import com.depi.moviex.cast_member.domain.models.CastMember
import com.depi.moviex.cast_member.domain.models.KnownForMovie

class CastMemberMapper {

    fun toDomain(dto: CastMemberDto): CastMember {
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
            knownFor = dto.knownFor.map { knownForDto ->
                KnownForMovie(
                    id = knownForDto.id,
                    title = knownForDto.title ?: knownForDto.name ?: "Unknown",
                    posterPath = knownForDto.posterPath,
                    backdropPath = knownForDto.backdropPath,
                    voteAverage = knownForDto.voteAverage,
                    releaseDate = knownForDto.releaseDate ?: knownForDto.firstAirDate ?: "",
                    mediaType = knownForDto.mediaType
                )
            }
        )
    }
}