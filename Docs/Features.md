# Features

MovieX includes multiple features organized by functionality. Each feature follows Clean Architecture with its own screen, ViewModel, and data layer.

---

## Feature Overview

| Feature | Status | Screen | ViewModel |
|---------|--------|--------|-----------|
| Splash | Done | `SplashScreen` | - |
| Onboarding | Done | `OnboardingScreen` | - |
| Authentication | Done | `LoginScreen`, `SignUpScreen` | `LoginViewModel`, `SignUpViewModel` |
| Home | Done | `HomeScreen` | `HomeViewModel` |
| Movie Detail | Done | `MovieDetailScreen` | `MovieDetailViewModel` |
| Settings | Done | `SettingsScreen` | - |
| Search | Done | `SearchScreen` | `SearchViewModel` |
| Favorites | Planned | `FavoritesScreen` | `FavoritesViewModel` |
| Cast Screen | Planned | `CastScreen` | `CastViewModel` |
| Dark/Light Mode | Planned | Theme toggle | - |
| Profile Screen | Planned | `ProfileScreen` | `ProfileViewModel` |
| Multi-language | Planned | i18n support | - |

---

## 1. Splash Screen

**Status**: Done

**Purpose**: Display app branding on launch and navigate to the appropriate screen based on user state.

**Components**:
- App logo
- App name in brand color
- Tagline text
- 2-second delay using `kotlinx.coroutines.delay`

**Navigation**:
- First launch → Onboarding
- Returning user → Login

**File**: `ui/theme/screens/splash/SplashScreen.kt`

---

## 2. Onboarding Screen

**Status**: Done

**Purpose**: Guide first-time users through the app's main features.

**Components**:
- `HorizontalPager` with 4 pages
- Page indicator dots
- Skip, Next, and Get Started buttons

**Pages**:
1. Find your next movie (search icon)
2. Explore trending content (trending icon)
3. View details and reviews (info icon)
4. Start exploring (play icon)

**State Management**:
- Uses `rememberPagerState` for page tracking
- `LaunchedEffect` for animation timing

**Persistence** [Planned]: Store onboarding completion flag in DataStore.

**File**: `ui/theme/screens/onboarding/OnboardingScreen.kt`

---

## 3. Authentication

**Status**: Done

**Screens**:
- `LoginScreen` - Email/password login
- `SignUpScreen` - New user registration

**Components**:
- Email input field
- Password input field with visibility toggle
- Login/Signup button
- Guest login option
- Navigation between screens

**ViewModel**: `LoginViewModel`, `SignUpViewModel`

**State**:
- Loading state
- Error handling
- Navigation on success

**Files**:
- `ui/theme/screens/auth/LoginScreen.kt`
- `ui/theme/screens/auth/SignUpScreen.kt`
- `ui/theme/screens/auth/viewModel/LoginViewModel.kt`
- `ui/theme/screens/auth/viewModel/SignUpViewModel.kt`

---

## 4. Home Screen

**Status**: Done

**Purpose**: Main feed displaying categorized movie content.

**Components**:
- Header with welcome message
- Settings icon button
- Search bar
- Category rows with horizontal scrolling:
  - Trending Now
  - Most Watched
  - TV Shows
  - Action
  - Drama
  - Comedy

**ViewModel**: `HomeViewModel`

**Data Fetched**:
- Discover movies
- Trending movies
- TV shows
- Action movies
- Drama movies
- Comedy movies

**State**:
```kotlin
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

**Files**:
- `ui/theme/screens/home/HomeScreen.kt`
- `ui/theme/screens/home/HomeViewModel.kt`
- `ui/theme/screens/home/components/CategoryRows.kt`
- `ui/theme/screens/home/components/MovieCoverImage.kt`

---

## 5. Movie Detail Screen

**Status**: Done

**Purpose**: Display comprehensive information about a selected movie.

**Components**:
- Backdrop image with gradient overlay
- Back button (top-left)
- Title
- Rating with star icon
- Release year
- Runtime
- Genre chips
- Overview section
- Cast horizontal scroll list
- Reviews section

**ViewModel**: `MovieDetailViewModel`

**State**:
```kotlin
data class MovieDetailState(
    val movieDetail: MovieDetail? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
```

**Data Model**:
```kotlin
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
```

**Files**:
- `ui/theme/screens/moviedetail/MovieDetailScreen.kt`
- `ui/theme/screens/moviedetail/MovieDetailViewModel.kt`

---

## 6. Settings Screen

**Status**: Done

**Purpose**: Provide user settings and sign out functionality.

**Components**:
- Settings options
- Sign out button

**Navigation**: Returns to login screen on sign out.

**File**: `ui/theme/screens/settings/SettingsScreen.kt`

---

## 7. Search Screen

**Status**: Done

**Purpose**: Search for movies and TV shows by title.

**Components**:
- Search text field with debounce
- Results list with movie cards
- Empty state
- Loading state

**ViewModel**: `SearchViewModel`

**Data Source**: `GET /search/movie?query=`

---

## 8. Favorites Screen

**Status**: Planned

**Purpose**: Save and manage favorite movies locally.

**Components**:
- Favorite movies list
- Empty state with browse CTA
- Remove from favorites functionality

**Implementation**:
- Room database for local storage
- FavoritesRepository
- FavoritesViewModel with Flow

---

## 9. Cast Screen

**Status**: Planned

**Purpose**: Display detailed information about cast members.

**Components**:
- Cast member profile images
- Actor/actress names
- Character names
- Biography
- Known for movies

**Data Source**: `GET /person/{person_id}`

---

## 10. Dark/Light Mode

**Status**: Planned

**Purpose**: Provide user preference for theme.

**Implementation**:
- Theme toggle in Settings
- Persist preference in DataStore
- Dynamic color support (Android 12+)

---

## 11. Profile Screen

**Status**: Planned

**Purpose**: Display user profile and settings.

**Components**:
- User avatar
- Username/email
- Account settings
- Theme preference
- Language preference
- Sign out option

---

## 12. Multi-language Support

**Status**: Planned

**Purpose**: Support multiple languages for global users.

**Implementation**:
- String resources externalized
- RTL layout support
- Language selection in settings
- TMDB API language parameter

---

## Error Handling

| State | UI Treatment |
|-------|---------------|
| Loading | `CircularProgressIndicator` centered |
| Network Error | Error card with message and retry option |
| Empty Results | Illustration with contextual message |
| API Error (429) | Snackbar with rate limit message |

---

## Loading States

All screens follow consistent loading patterns:
- Initial load shows loading indicator
- Subsequent loads may show inline loading
- Error states are displayed with retry options
- Empty states have appropriate messaging