# TMDB API Integration

MovieX uses the [TMDB v3 REST API](https://developer.themoviedb.org/reference/intro/getting-started).

---

## 1. Obtain an API Key

1. Create a free account at [themoviedb.org](https://www.themoviedb.org/)
2. Go to **Settings → API**
3. Request an API key (choose **Developer**)
4. Copy your **API Key (v3 auth)**

> **Never commit your key.** Add it to `local.properties` (git-ignored):
> ```properties
> TMDB_API_KEY=your_key_here
> TMDB_BASE_URL=https://api.themoviedb.org/3/
> ```

---

## 2. Expose Key via BuildConfig

In `app/build.gradle.kts`:

```kotlin
android {
    defaultConfig {
        buildConfigField("String", "TMDB_API_KEY", "\"${project.findProperty("TMDB_API_KEY") ?: ""}\"")
        buildConfigField("String", "TMDB_BASE_URL", "\"https://api.themoviedb.org/3/\"")
    }
    buildFeatures { buildConfig = true }
}
```

Access in code: `BuildConfig.TMDB_API_KEY`

---

## 3. Authentication

All requests use an **API key query parameter**:

```
https://api.themoviedb.org/3/movie/popular?api_key=YOUR_KEY&language=en-US&page=1
```

An OkHttp interceptor handles this automatically:

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

TMDB images are not full URLs. Build them with:

```
https://image.tmdb.org/t/p/{size}/{poster_path}
```

Common sizes: `w92`, `w185`, `w342`, `w500`, `w780`, `original`

```kotlin
object ImageUrlBuilder {
    private const val BASE = "https://image.tmdb.org/t/p/"
    fun poster(path: String?, size: String = "w342") =
        if (path != null) "$BASE$size$path" else null
    fun backdrop(path: String?, size: String = "w780") =
        if (path != null) "$BASE$size$path" else null
}
```

---

## 5. Retrofit API Service

```kotlin
interface TmdbApiService {

    // --- Movies ---
    @GET("movie/trending/movie/week")
    suspend fun getTrendingMovies(@Query("page") page: Int = 1): MovieListResponse

    @GET("movie/{id}")
    suspend fun getMovieDetails(@Path("id") id: Int): MovieDetailDto

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("page") page: Int = 1
    ): MovieListResponse

    @GET("movie/popular")
    suspend fun getPopularMovies(@Query("page") page: Int = 1): MovieListResponse

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(@Query("page") page: Int = 1): MovieListResponse

    @GET("movie/{id}/credits")
    suspend fun getMovieCredits(@Path("id") id: Int): CreditsDto

    // --- TV Shows ---
    @GET("trending/tv/week")
    suspend fun getTrendingTv(@Query("page") page: Int = 1): TvListResponse

    @GET("tv/{id}")
    suspend fun getTvDetails(@Path("id") id: Int): TvDetailDto

    @GET("search/tv")
    suspend fun searchTv(
        @Query("query") query: String,
        @Query("page") page: Int = 1
    ): TvListResponse
}
```

---

## 6. Key Endpoints Reference

| Endpoint | Description |
|---|---|
| `GET /trending/movie/week` | Weekly trending movies |
| `GET /trending/tv/week` | Weekly trending TV shows |
| `GET /movie/popular` | Popular movies |
| `GET /movie/top_rated` | Top-rated movies |
| `GET /movie/{id}` | Movie details |
| `GET /movie/{id}/credits` | Cast & crew |
| `GET /search/movie?query=` | Search movies |
| `GET /search/tv?query=` | Search TV shows |
| `GET /genre/movie/list` | Movie genres |

Full reference: [TMDB API Docs](https://developer.themoviedb.org/reference)

---

## 7. Rate Limits

- Free tier: **~40 requests per 10 seconds** per account
- Implement debounce on search (300–500 ms)
- Use caching headers or Room cache to reduce redundant calls

---

## 8. Error Codes

| Code | Meaning |
|---|---|
| 401 | Invalid API key |
| 404 | Resource not found |
| 429 | Rate limit exceeded |
| 500 | TMDB server error |
