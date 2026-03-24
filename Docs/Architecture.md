# Architecture — MovieX

MovieX follows **MVVM + Clean Architecture**, separating concerns into three layers with strict dependency rules.

---

## Layer Overview

```
┌────────────────────────────────────────────┐
│           Presentation Layer               │
│  Compose Screens · ViewModels · UI State   │
├────────────────────────────────────────────┤
│             Domain Layer                   │
│    Use Cases · Repository Interfaces       │
│       Domain Models (pure Kotlin)          │
├────────────────────────────────────────────┤
│              Data Layer                    │
│  Repository Impl · Retrofit · Room · DTOs  │
└────────────────────────────────────────────┘
```

**Dependency rule**: outer layers depend inward. `Presentation` → `Domain` ← `Data`.  
`Domain` knows nothing about Android or Retrofit.

---

## Data Flow

```
User Action
    │
    ▼
Compose Screen
    │  calls
    ▼
ViewModel  ──────────────── emits UiState (StateFlow)
    │  calls
    ▼
UseCase (domain)
    │  calls
    ▼
Repository Interface (domain)
    │  implemented by
    ▼
RepositoryImpl (data)
    │  calls
    ├──▶ TMDB API (Retrofit)  ──▶ DTO  ──▶ Domain Model
    └──▶ Room Database         ──▶ Entity ──▶ Domain Model
```

---

## Package Structure

```
com.depi.moviex/
│
├── core/
│   ├── base/           # BaseViewModel, BaseUseCase
│   ├── network/        # OkHttp client, interceptors, NetworkResult sealed class
│   └── extensions/     # Kotlin extension functions
│
├── data/
│   ├── remote/
│   │   ├── api/        # TmdbApiService (Retrofit interface)
│   │   └── dto/        # Data Transfer Objects (MovieDto, etc.)
│   ├── local/
│   │   ├── dao/        # Room DAOs
│   │   └── entity/     # Room entities
│   └── repository/     # RepositoryImpl classes
│
├── domain/
│   ├── model/          # Domain models (Movie, TvShow, etc.)
│   ├── repository/     # Repository interfaces
│   └── usecase/        # Use case classes (one action per class)
│
├── presentation/
│   ├── navigation/     # NavHost, routes, AppNavigation.kt
│   ├── splash/
│   ├── onboarding/
│   ├── home/           # HomeScreen, HomeViewModel
│   ├── search/         # SearchScreen, SearchViewModel
│   ├── detail/         # DetailScreen, DetailViewModel
│   └── favorites/      # FavoritesScreen, FavoritesViewModel [TBD]
│
├── di/
│   ├── NetworkModule.kt
│   ├── DatabaseModule.kt
│   └── RepositoryModule.kt
│
└── utils/
    ├── DateFormatter.kt
    ├── ImageUrlBuilder.kt
    └── Constants.kt
```

---

## UI State Pattern

Each screen has a sealed `UiState`:

```kotlin
sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(val movies: List<Movie>) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
    object Empty : HomeUiState()
}
```

ViewModel exposes:

```kotlin
private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
```

---

## NetworkResult Wrapper

All repository calls return a `NetworkResult<T>`:

```kotlin
sealed class NetworkResult<out T> {
    data class Success<T>(val data: T) : NetworkResult<T>()
    data class Error(val code: Int?, val message: String) : NetworkResult<Nothing>()
    object Loading : NetworkResult<Nothing>()
}
```

---

## Scalability Considerations

| Concern | Approach |
|---|---|
| Feature growth | Split into Gradle modules per feature |
| State complexity | Adopt MVI with `Orbit` or `MVI Kotlin` |
| Offline | Room + `RemoteMediator` (Paging 3) |
| Multi-module | `:core`, `:feature:home`, `:feature:search`, `:data` |
| Testing | Use cases are plain Kotlin → easily unit tested |
