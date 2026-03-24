# Features — MovieX

Each feature maps to a `presentation/<feature>/` package with its own screen, ViewModel, and UI state.

---

## Feature Map

| Feature | Status | Screen | ViewModel |
|---|---|---|---|
| Splash | ✅ Done | `SplashScreen` | — |
| Onboarding | ✅ Done | `OnboardingScreen` | — |
| Trending / Home | 🚧 In Progress | `HomeScreen` | `HomeViewModel` |
| Search | 📋 Planned | `SearchScreen` | `SearchViewModel` |
| Movie Detail | 📋 Planned | `DetailScreen` | `DetailViewModel` |
| Favorites | 📋 Planned | `FavoritesScreen` | `FavoritesViewModel` |

---

## 1. Splash Screen ✅

**Purpose**: Brand intro → navigates to Onboarding (first launch) or Home.

**UI Components**:
- App logo (`moviex_logo`)
- App name "MovieX" in brand red
- Tagline text

**Logic**:
- 1.5-second delay via `LaunchedEffect` + `kotlinx.coroutines.delay`
- Navigates and clears back stack

**Data Sources**: none

---

## 2. Onboarding Screen ✅

**Purpose**: First-time user education (4 slides).

**UI Components**:
- `HorizontalPager` with 4 pages
- Icon, title, description per page
- Dot indicator
- Skip / Next / Start buttons

**Slides**:
1. Find your next movie (search icon)
2. Upcoming releases (calendar icon)
3. Watchlist & history (watchlist icon)
4. Ready to explore? (play icon)

**Logic**:
- State: pager position via `rememberPagerState`
- On last page: calls `onFinish` → navigate to Home

**Data Sources**: none (static content)

**Persistence** `[TBD]`:
- Store "onboarding seen" flag in `DataStore` so it only shows once

---

## 3. Home / Trending Screen 🚧

**Purpose**: Main feed showing trending & popular content.

**UI Components**:
- `TopAppBar` with logo + search icon
- Featured movie banner (large card)
- Horizontal scroll rows: "Trending", "Popular", "Top Rated"
- Movie card (poster, title, rating)

**ViewModel: `HomeViewModel`**

```kotlin
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getTrendingMoviesUseCase: GetTrendingMoviesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init { loadTrending() }

    fun loadTrending() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            getTrendingMoviesUseCase().collect { result ->
                _uiState.value = when (result) {
                    is NetworkResult.Success -> HomeUiState.Success(result.data)
                    is NetworkResult.Error   -> HomeUiState.Error(result.message)
                    else -> HomeUiState.Loading
                }
            }
        }
    }
}
```

**Use Case**: `GetTrendingMoviesUseCase` → `MovieRepository` → `TmdbApiService`

**Data Sources**:
- `GET /trending/movie/week`
- `GET /movie/popular`

---

## 4. Search Screen 📋

**Purpose**: Real-time search across movies and TV shows.

**UI Components**:
- Search `TextField` with debounce
- Results `LazyColumn` with movie cards
- Empty state illustration + text
- Loading skeleton

**ViewModel: `SearchViewModel`**

```kotlin
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchMoviesUseCase: SearchMoviesUseCase
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    val results: StateFlow<SearchUiState> = _query
        .debounce(400)
        .filter { it.length >= 2 }
        .flatMapLatest { q -> searchMoviesUseCase(q) }
        .map { SearchUiState.Success(it) }
        .catch { emit(SearchUiState.Error(it.message ?: "Unknown error")) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SearchUiState.Idle)

    fun onQueryChange(q: String) { _query.value = q }
}
```

**Data Sources**: `GET /search/movie?query=`, `GET /search/tv?query=`

---

## 5. Movie Detail Screen 📋

**Purpose**: Full movie information page.

**UI Components**:
- Backdrop image (full width, parallax scroll)
- Poster thumbnail
- Title, tagline, rating (star chips)
- Overview text
- Genre chips
- Cast horizontal scroll row
- "Add to Favorites" button

**ViewModel: `DetailViewModel`**

```kotlin
@HiltViewModel
class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase
) : ViewModel() {
    private val movieId: Int = checkNotNull(savedStateHandle["movieId"])

    private val _uiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    init { loadDetails() }

    private fun loadDetails() {
        viewModelScope.launch {
            _uiState.value = DetailUiState.Loading
            getMovieDetailsUseCase(movieId).collect { result ->
                _uiState.value = when (result) {
                    is NetworkResult.Success -> DetailUiState.Success(result.data)
                    is NetworkResult.Error -> DetailUiState.Error(result.message)
                    else -> DetailUiState.Loading
                }
            }
        }
    }
}
```

**Data Sources**: `GET /movie/{id}`, `GET /movie/{id}/credits`

---

## 6. Favorites Screen 📋

> [TBD: Favorites Feature]
>
> - **Description**: Allow users to save movies locally and view them offline
> - **Expected behavior**: Heart icon on any movie card toggles favorite state; Favorites tab shows saved list
> - **Required APIs**: Local Room database only (no network call)
> - **UI notes**: Empty state with CTA to browse
> - **Implementation notes**: Room Entity + DAO + FavoritesRepository; `FavoritesViewModel` with `Flow<List<Movie>>` from DB

---

## Error & Loading States (All Screens)

| State | UI Treatment |
|---|---|
| Loading | `CircularProgressIndicator` centred |
| Network error | Illustrated error card + "Retry" button |
| Empty results | Illustration + contextual message |
| Rate limited (429) | Toast / Snackbar "Too many requests, try again shortly" |
| Offline | Banner "No internet connection" |
