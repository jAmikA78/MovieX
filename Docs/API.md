# API Integration

MovieX integrates with The Movie Database (TMDB) API v3 for fetching movie data, images, and cast information.

---

## Table of Contents

1. [Getting an API Key](#1-getting-an-api-key)
2. [Configuration](#2-configuration)
3. [Authentication](#3-authentication)
4. [Image URLs](#4-image-urls)
5. [API Endpoints](#5-api-endpoints)
6. [Rate Limits](#6-rate-limits)
7. [Error Handling](#7-error-handling)

---

## 1. Getting an API Key

1. Create a free account at [themoviedb.org](https://www.themoviedb.org/)
2. Navigate to **Settings → API**
3. Request an API key (select **Developer**)
4. Copy your **API Key (v3 auth)**

> **Security**: Never commit your API key. Add it to `local.properties` (git-ignored).

---

## 2. Configuration

### local.properties

```properties
TMDB_API_KEY=your_key_here
```

### BuildConfig Access

The API key is exposed via `BuildConfig.TMDB_API_KEY` in the app:

```kotlin
// In di/ modules
val apiKey = BuildConfig.TMDB_API_KEY
```

### Base URL

```
https://api.themoviedb.org/3/
```

Defined in `utils/K.kt`:

```kotlin
object K {
    const val BASE_URL = "https://api.themoviedb.org/3/"
    const val BASE_IMAGE_URL = "https://image.tmdb.org/t/p/"
}
```

---

## 3. Authentication

All requests include the API key via query parameter:

```
https://api.themoviedb.org/3/movie/popular?api_key=YOUR_KEY&language=en-US&page=1
```

This is handled automatically by an OkHttp interceptor:

```kotlin
class ApiKeyInterceptor(private val apiKey: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val url = chain.request().url.newBuilder()
            .addQueryParameter("api_key", apiKey)
            .addQueryParameter("language", "en-US")
            .build()
        return chain.proceed(chain.request().newBuilder().url(url).build())
    }
}
```

---

## 4. Image URLs

TMDB provides relative paths. Build full URLs:

```
https://image.tmdb.org/t/p/{size}/{path}
```

### Available Sizes

| Size | Width | Use Case |
|------|-------|----------|
| w92 | 92px | Thumbnails |
| w185 | 185px | Cards |
| w342 | 342px | Movie posters |
| w500 | 500px | Large posters |
| w780 | 780px | Backdrops |
| original | - | Full resolution |

### Usage in Code

```kotlin
// Via Coil in Compose
AsyncImage(
    model = ImageRequest.Builder(context)
        .data("https://image.tmdb.org/t/p/w342${movie.posterPath}")
        .crossfade(true)
        .build(),
    contentDescription = movie.title
)
```

---

## 5. API Endpoints

### Movies

| Endpoint | Description | Status |
|----------|-------------|--------|
| `GET /trending/movie/week` | Weekly trending movies | Done |
| `GET /movie/popular` | Popular movies | Done |
| `GET /movie/{id}` | Movie details | Done |
| `GET /movie/{id}/credits` | Cast and crew | Done |
| `GET /movie/{id}/reviews` | Movie reviews | Done |
| `GET /discover/movie` | Discover by genre | Done |
| `GET /search/movie` | Search movies | Planned |

### TV Shows

| Endpoint | Description | Status |
|----------|-------------|--------|
| `GET /trending/tv/week` | Weekly trending TV | Done |
| `GET /tv/popular` | Popular TV shows | Done |
| `GET /search/tv` | Search TV shows | Planned |

### Genres

| Endpoint | Description |
|----------|-------------|
| `GET /genre/movie/list` | Movie genre list |

### Service Interfaces

```kotlin
// MovieApiService
interface MovieApiService {
    @GET("discover/movie")
    suspend fun getDiscoverMovies(@Query("page") page: Int = 1): MovieDto

    @GET("trending/movie/week")
    suspend fun getTrendingMovies(@Query("page") page: Int = 1): MovieDto

    @GET("trending/tv/week")
    suspend fun getTrendingTvShows(@Query("page") page: Int = 1): MovieDto

    @GET("discover/movie")
    suspend fun getMoviesByGenre(
        @Query("with_genres") genreId: Int,
        @Query("page") page: Int = 1
    ): MovieDto
}

// MovieDetailApiService
interface MovieDetailApiService {
    @GET("movie/{id}")
    suspend fun getMovieDetail(@Path("id") movieId: Int): MovieDetailDto

    @GET("movie/{id}/credits")
    suspend fun getMovieCredits(@Path("id") movieId: Int): CreditsDto

    @GET("movie/{id}/reviews")
    suspend fun getMovieReviews(@Path("id") movieId: Int): ReviewsDto
}
```

---

## 6. Rate Limits

| Tier | Limit |
|------|-------|
| Free | ~40 requests per 10 seconds |

### Best Practices

- Implement debounce on search (300-500ms)
- Cache responses locally
- Use pagination to spread requests

---

## 7. Error Handling

### HTTP Status Codes

| Code | Meaning | Action |
|------|---------|--------|
| 200 | Success | Process response |
| 401 | Invalid API key | Check key in local.properties |
| 404 | Resource not found | Show "Not found" message |
| 429 | Rate limit | Show "Try again later" message |
| 500 | Server error | Show generic error |

### Error Handling in Code

```kotlin
sealed class Response<out T> {
    data class Success<T>(val data: T) : Response<T>()
    data class Error(val message: String, val code: Int? = null) : Response<T>()
    object Loading : Response<Nothing>()
}

// Usage in ViewModel
repository.fetchMovieDetail(movieId).collectAndHandle(
    onError = { error ->
        _state.update { it.copy(error = error?.message) }
    },
    onLoading = {
        _state.update { it.copy(isLoading = true) }
    },
    onSuccess = { detail ->
        _state.update { it.copy(movieDetail = detail) }
    }
)
```

---

## API Reference

Full documentation: [TMDB API Docs](https://developer.themoviedb.org/reference)