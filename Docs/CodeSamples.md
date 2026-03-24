# Code Samples — MovieX

Minimal, production-ready code examples for each architectural layer.

---

## 1. Dependency Injection — Network Module

```kotlin
// di/NetworkModule.kt
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides @Singleton
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(ApiKeyInterceptor(BuildConfig.TMDB_API_KEY))
            .apply {
                if (BuildConfig.DEBUG) {
                    addInterceptor(HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    })
                }
            }
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

    @Provides @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.TMDB_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides @Singleton
    fun provideTmdbApiService(retrofit: Retrofit): TmdbApiService =
        retrofit.create(TmdbApiService::class.java)
}
```

---

## 2. Domain Model

```kotlin
// domain/model/Movie.kt
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

---

## 3. Repository Interface + Implementation

```kotlin
// domain/repository/MovieRepository.kt
interface MovieRepository {
    fun getTrendingMovies(page: Int = 1): Flow<NetworkResult<List<Movie>>>
    fun getMovieDetails(id: Int): Flow<NetworkResult<MovieDetail>>
    fun searchMovies(query: String, page: Int = 1): Flow<NetworkResult<List<Movie>>>
}

// data/repository/MovieRepositoryImpl.kt
@Singleton
class MovieRepositoryImpl @Inject constructor(
    private val api: TmdbApiService
) : MovieRepository {

    override fun getTrendingMovies(page: Int) = flow<NetworkResult<List<Movie>>> {
        emit(NetworkResult.Loading)
        try {
            val response = api.getTrendingMovies(page)
            emit(NetworkResult.Success(response.results.map { it.toDomain() }))
        } catch (e: HttpException) {
            emit(NetworkResult.Error(e.code(), e.message()))
        } catch (e: IOException) {
            emit(NetworkResult.Error(null, "No internet connection"))
        }
    }

    override fun searchMovies(query: String, page: Int) = flow<NetworkResult<List<Movie>>> {
        emit(NetworkResult.Loading)
        try {
            val response = api.searchMovies(query, page)
            emit(NetworkResult.Success(response.results.map { it.toDomain() }))
        } catch (e: Exception) {
            emit(NetworkResult.Error(null, e.message ?: "Unknown error"))
        }
    }

    override fun getMovieDetails(id: Int) = flow<NetworkResult<MovieDetail>> {
        emit(NetworkResult.Loading)
        try {
            val detail = api.getMovieDetails(id)
            emit(NetworkResult.Success(detail.toDomain()))
        } catch (e: HttpException) {
            emit(NetworkResult.Error(e.code(), e.message()))
        }
    }
}
```

---

## 4. Use Case

```kotlin
// domain/usecase/GetTrendingMoviesUseCase.kt
class GetTrendingMoviesUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    operator fun invoke(page: Int = 1): Flow<NetworkResult<List<Movie>>> =
        repository.getTrendingMovies(page)
}
```

---

## 5. ViewModel

```kotlin
// presentation/home/HomeViewModel.kt
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getTrendingMovies: GetTrendingMoviesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init { load() }

    fun load() {
        viewModelScope.launch {
            getTrendingMovies().collect { result ->
                _uiState.value = when (result) {
                    is NetworkResult.Loading -> HomeUiState.Loading
                    is NetworkResult.Success -> HomeUiState.Success(result.data)
                    is NetworkResult.Error   -> HomeUiState.Error(result.message)
                }
            }
        }
    }
}

sealed class HomeUiState {
    object Loading : HomeUiState()
    object Empty : HomeUiState()
    data class Success(val movies: List<Movie>) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}
```

---

## 6. Compose Screen

```kotlin
// presentation/home/HomeScreen.kt
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onMovieClick: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { HomeTopBar() },
        containerColor = BackgroundDark
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            when (val state = uiState) {
                is HomeUiState.Loading -> LoadingView()
                is HomeUiState.Error   -> ErrorView(
                    message = state.message,
                    onRetry = viewModel::load
                )
                is HomeUiState.Empty   -> EmptyView()
                is HomeUiState.Success -> MovieGrid(
                    movies = state.movies,
                    onMovieClick = onMovieClick
                )
            }
        }
    }
}

@Composable
fun MovieCard(movie: Movie, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(140.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        AsyncImage(
            model = ImageUrlBuilder.poster(movie.posterPath),
            contentDescription = movie.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth().height(210.dp)
        )
        Text(
            text = movie.title,
            style = MaterialTheme.typography.labelMedium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(8.dp)
        )
    }
}
```

---

## 7. DTO → Domain Mapper

```kotlin
// data/remote/dto/MovieDto.kt
data class MovieDto(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("overview") val overview: String,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("backdrop_path") val backdropPath: String?,
    @SerializedName("vote_average") val voteAverage: Double,
    @SerializedName("release_date") val releaseDate: String,
    @SerializedName("genre_ids") val genreIds: List<Int>
)

fun MovieDto.toDomain() = Movie(
    id = id,
    title = title,
    overview = overview,
    posterPath = posterPath,
    backdropPath = backdropPath,
    voteAverage = voteAverage,
    releaseDate = releaseDate,
    genreIds = genreIds
)
```
