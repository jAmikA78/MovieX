# Code Samples

Production-ready code examples demonstrating Well-Architected design patterns in MovieX.

---

## 1. Domain Models

### Movie Model
```kotlin
// movie/domain/models/Movie.kt
data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    val posterPath: String?,
    val backdropPath: String?,
    val voteAverage: Double,
    val releaseDate: String,
    val genreIds: List<Int>
)
```

### MovieDetail Model
```kotlin
// movie_detail/domain/models/MovieDetail.kt
data class MovieDetail(
    val backdropPath: String,
    val genreIds: List<String>,
    val id: Int,
    val originalLanguage: String,
    val originalTitle: String,
    val overview: String,
    val popularity: Double,
    val posterPath: String,
    val releaseDate: String,
    val title: String,
    val voteAverage: Double,
    val voteCount: Int,
    val video: Boolean,
    val cast: List<Cast>,
    val language: List<String>,
    val productionCountry: List<String>,
    val reviews: List<Review>,
    val runTime: String
)

data class Cast(
    val id: Int,
    val name: String,
    val genderRole: String,
    val character: String,
    val profilePath: String?,
) {
    val firstName: String get() = name.split(" ", limit = 2)[0]
    val lastName: String get() = name.split(" ", limit = 2).getOrNull(1) ?: ""
}
```

---

## 2. Repository Interface

```kotlin
// movie/domain/repository/MovieRepository.kt
interface MovieRepository {
    fun fetchDiscoverMovies(): Flow<Response<List<Movie>>>
    fun fetchTrendingMovies(): Flow<Response<List<Movie>>>
    fun fetchTvShows(): Flow<Response<List<Movie>>>
    fun fetchActionMovies(): Flow<Response<List<Movie>>>
    fun fetchDramaMovies(): Flow<Response<List<Movie>>>
    fun fetchComedyMovies(): Flow<Response<List<Movie>>>
}
```

---

## 3. Response Wrapper

```kotlin
// utils/Response.kt
sealed class Response<out T> {
    data class Success<T>(val data: T) : Response<T>()
    data class Error(val message: String, val code: Int? = null) : Response<T>()
    object Loading : Response<Nothing>()
}

// utils/Ext.kt
fun <T> Flow<Response<T>>.collectAndHandle(
    onError: (Throwable?) -> Unit = {},
    onLoading: () -> Unit = {},
    onSuccess: (T) -> Unit
) = this.collect { response ->
    when (response) {
        is Response.Success -> onSuccess(response.data)
        is Response.Error -> onError(Throwable(response.message))
        is Response.Loading -> onLoading()
    }
}
```

---

## 4. API Service

```kotlin
// movie/data/remote/api/MovieApiService.kt
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
```

---

## 5. Repository Implementation

```kotlin
// movie/data/repository_impl/MovieRepositoryImpl.kt
@Singleton
class MovieRepositoryImpl @Inject constructor(
    private val apiService: MovieApiService,
    private val mapper: ApiMapper<List<Movie>, MovieDto>
) : MovieRepository {

    override fun fetchDiscoverMovies(): Flow<Response<List<Movie>>> = flow {
        emit(Response.Loading)
        try {
            val response = apiService.getDiscoverMovies()
            val movies = mapper.map(response)
            emit(Response.Success(movies))
        } catch (e: Exception) {
            emit(Response.Error(e.message ?: "Unknown error"))
        }
    }

    override fun fetchTrendingMovies(): Flow<Response<List<Movie>>> = flow {
        emit(Response.Loading)
        try {
            val response = apiService.getTrendingMovies()
            val movies = mapper.map(response)
            emit(Response.Success(movies))
        } catch (e: Exception) {
            emit(Response.Error(e.message ?: "Unknown error"))
        }
    }

    // Similar implementations for other methods...
}
```

---

## 6. Hilt Module

```kotlin
// di/MovieModule.kt
@Module
@InstallIn(SingletonComponent::class)
object MovieModule {

    @Provides
    @Singleton
    fun provideMovieRepository(
        apiService: MovieApiService,
        mapper: ApiMapper<List<Movie>, MovieDto>
    ): MovieRepository = MovieRepositoryImpl(apiService, mapper)

    @Provides
    @Singleton
    fun provideMovieApiService(): MovieApiService {
        val json = Json { ignoreUnknownKeys = true }
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl(K.BASE_URL)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
            .create(MovieApiService::class.java)
    }
}
```

---

## 7. ViewModel

```kotlin
// ui/theme/screens/home/HomeViewModel.kt
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    private val _homeState = MutableStateFlow(HomeState())
    val homeState = _homeState.asStateFlow()

    init {
        fetchDiscoverMovie()
        fetchTrendingMovie()
        fetchTvShows()
        fetchActionMovies()
        fetchDramaMovies()
        fetchComedyMovies()
    }

    private fun fetchDiscoverMovie() = viewModelScope.launch {
        repository.fetchDiscoverMovies().collectAndHandle(
            onError = { error ->
                _homeState.update { it.copy(isLoading = false, error = error?.message) }
            },
            onLoading = {
                _homeState.update { it.copy(isLoading = true, error = null) }
            }
        ) { movie ->
            _homeState.update { it.copy(isLoading = false, error = null, discoverMovies = movie) }
        }
    }
    // ... other fetch methods
}

data class HomeState(
    val message: String = "Welcome to MovieX",
    val discoverMovies: List<Movie> = emptyList(),
    val trendingMovies: List<Movie> = emptyList(),
    val tvShows: List<Movie> = emptyList(),
    val actionMovies: List<Movie> = emptyList(),
    val dramaMovies: List<Movie> = emptyList(),
    val comedyMovies: List<Movie> = emptyList(),
    val error: String? = null,
    val isLoading: Boolean = false
)
```

---

## 8. Compose Screen

```kotlin
// ui/theme/screens/home/HomeScreen.kt
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = hiltViewModel(),
    onMovieClick: (id: Int) -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
    val state by homeViewModel.homeState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .verticalScroll(rememberScrollState())
    ) {
        // Header with settings
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 50.dp, start = 16.dp, end = 8.dp)
        ) {
            HeaderSection(message = state.message)
            // Settings button...
        }

        SearchBar()

        when {
            state.isLoading -> LoadingIndicator()
            state.error != null -> ErrorSection(error = state.error!!)
            else -> CategoryRows(categories = listOf(...), onMovieClick = onMovieClick)
        }
    }
}
```

---

## 9. Navigation

```kotlin
// MainActivity.kt
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") { SplashScreen(...) }
        composable("home") {
            HomeScreen(
                onMovieClick = { movieId ->
                    navController.navigate("movie_detail/$movieId")
                }
            )
        }
        composable(
            route = "movie_detail/{movieId}",
            arguments = listOf(navArgument("movieId") { type = NavType.IntType })
        ) {
            MovieDetailScreen(onBackClick = { navController.popBackStack() })
        }
    }
}
```

---

## 10. Image Loading

```kotlin
// Using Coil with Compose
@Composable
fun MovieCoverImage(movie: Movie, onMovieClick: (Int) -> Unit) {
    val imgRequest = ImageRequest.Builder(LocalContext.current)
        .data("${K.BASE_IMAGE_URL}${movie.posterPath}")
        .crossfade(true)
        .build()

    Box(
        modifier = Modifier
            .size(width = 150.dp, height = 250.dp)
            .clickable { onMovieClick(movie.id) }
    ) {
        AsyncImage(
            model = imgRequest,
            contentDescription = movie.title,
            modifier = Modifier.clip(MaterialTheme.shapes.medium),
            contentScale = ContentScale.Crop
        )
    }
}
```