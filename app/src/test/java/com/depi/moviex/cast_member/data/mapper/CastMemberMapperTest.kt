package com.depi.moviex.cast_member.data.mapper

import com.depi.moviex.cast_member.data.remote.models.CastMemberDto
import com.depi.moviex.cast_member.data.remote.models.CreditDto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class CastMemberMapperTest {

    private val mapper = CastMemberMapper()

    @Test
    fun `mapToDomain maps all fields correctly`() {
        val dto = CastMemberDto(
            id = 1,
            name = "Jane Doe",
            biography = "An actress",
            birthday = "1990-05-15",
            placeOfBirth = "Los Angeles",
            profilePath = "/profile.jpg",
            knownForDepartment = "Acting",
            popularity = 95.0
        )

        val result = mapper.mapToDomain(dto)

        assertEquals(1, result.id)
        assertEquals("Jane Doe", result.name)
        assertEquals("An actress", result.biography)
        assertEquals("1990-05-15", result.birthday)
        assertEquals("Los Angeles", result.placeOfBirth)
        assertEquals("/profile.jpg", result.profilePath)
        assertEquals("Acting", result.knownForDepartment)
        assertEquals(95.0, result.popularity, 0.001)
        assertNull(result.deathday)
        assertTrue(result.knownFor.isEmpty())
    }

    @Test
    fun `mapToDomain handles null optional fields`() {
        val dto = CastMemberDto(
            id = 2,
            name = "John Smith",
            biography = null,
            birthday = null,
            deathday = "2020-01-01",
            placeOfBirth = null,
            knownForDepartment = null
        )

        val result = mapper.mapToDomain(dto)

        assertEquals("", result.biography)
        assertEquals("Unknown", result.birthday)
        assertEquals("2020-01-01", result.deathday)
        assertEquals("Unknown", result.placeOfBirth)
        assertEquals("Unknown", result.knownForDepartment)
    }

    @Test
    fun `toDomain includes knownForCredits`() {
        val dto = CastMemberDto(id = 3, name = "Test Actor")
        val creditDtos = listOf(
            CreditDto(
                id = 101,
                title = "Movie A",
                voteAverage = 7.5,
                releaseDate = "2023-01-01",
                posterPath = "/posterA.jpg",
                backdropPath = "/backdropA.jpg",
                mediaType = "movie"
            )
        )
        val knownForMovies = mapper.mapKnownFor(creditDtos)
        val result = mapper.toDomain(dto, knownForMovies)

        assertEquals(1, result.knownFor.size)
        assertEquals(101, result.knownFor[0].id)
        assertEquals("Movie A", result.knownFor[0].title)
        assertEquals(7.5, result.knownFor[0].voteAverage, 0.001)
        assertEquals("2023-01-01", result.knownFor[0].releaseDate)
        assertEquals("/posterA.jpg", result.knownFor[0].posterPath)
        assertEquals("/backdropA.jpg", result.knownFor[0].backdropPath)
        assertEquals("movie", result.knownFor[0].mediaType)
    }

    @Test
    fun `mapKnownFor maps credits correctly`() {
        val credits = listOf(
            CreditDto(id = 1, title = "Title", name = null, mediaType = "movie"),
            CreditDto(id = 2, title = null, name = "TV Name", mediaType = "tv"),
            CreditDto(id = 3, title = null, name = null, mediaType = "unknown")
        )

        val result = mapper.mapKnownFor(credits)

        assertEquals(3, result.size)
        assertEquals("Title", result[0].title)
        assertEquals("TV Name", result[1].title)
        assertEquals("Unknown", result[2].title)
    }

    @Test
    fun `mapKnownFor uses firstAirDate when releaseDate is null`() {
        val credits = listOf(
            CreditDto(id = 1, title = "Show", releaseDate = null, firstAirDate = "2022-05-10")
        )

        val result = mapper.mapKnownFor(credits)

        assertEquals("2022-05-10", result[0].releaseDate)
    }
}
