# Future Enhancements — MovieX

Structured backlog of planned improvements and scalability additions.

---

## Near-Term (Next Sprint)

### Pagination with Paging 3

- Use `Pager` + `PagingSource` for seamless infinite scroll on movie lists
- `RemoteMediator` to combine network + Room cache
- Replace `List<Movie>` in ViewModels with `PagingData<Movie>`

### Favorites Feature

- Room entity `FavoriteEntity`, DAO, and `FavoritesRepository`
- Toggle favorite from any movie card or detail screen
- Dedicated Favorites tab in bottom navigation

### Splash → Routing Logic

- Persist "onboarding seen" flag in `DataStore<Preferences>`
- On launch: check flag → route to Home (returning user) or Onboarding (new user)

---

## Medium-Term

### Offline Caching

- Cache trending/popular responses in Room
- Serve cached data when offline
- Use `RemoteMediator` to handle cache invalidation

### Genres & Filters

- Load genre list from `GET /genre/movie/list`
- Allow filtering home feed by genre chips
- Persist selected genre in ViewModel `SavedStateHandle`

### TV Shows Tab

- Separate tab for trending TV shows
- TV detail screen with seasons/episodes overview
- TV search integrated into the Search screen

### Ratings & User Reviews

- `GET /movie/{id}/reviews` endpoint
- Reviews list in Detail screen
- Average rating breakdown

---

## Long-Term

### User Authentication (TMDB Account)

- TMDB v3 auth token flow (Request Token → Session ID)
- Login screen
- Synced watchlist & favorites across devices

### Push Notifications

- FCM integration for new release alerts
- User opt-in per movie

### Multi-Language Support (i18n)

- Pass `language` query param from device locale
- String resources for Arabic, English (and others)
- RTL layout support (already partially covered by `supportsRtl=true`)

### Modularization

Split into Gradle modules for scalability:

```
:app
:core:network
:core:ui
:core:database
:feature:home
:feature:search
:feature:detail
:feature:favorites
:data
:domain
```

Benefits: faster incremental builds, parallel feature development, strict dependency enforcement.

### CI/CD Pipeline

- GitHub Actions workflow for:
  - PR checks: `./gradlew test lint`
  - Main branch: Docker build + artifact upload
  - Release: signed AAB → Google Play via Fastlane

---

## Performance Improvements

| Optimization | Approach |
|---|---|
| Image caching | Coil disk cache + memory cache sizing |
| Compose recomposition | `remember`, `derivedStateOf`, stable data classes |
| Network | OkHttp response caching on `Cache-Control` |
| Startup time | App Startup library, lazy Hilt injection |

---

## Security

| Concern | Mitigation |
|---|---|
| API key exposure | `BuildConfig` + `local.properties` (never committed) |
| Deep links | Verify intent filters, use `https://` scheme |
| ProGuard/R8 | Enable for release builds |
| Certificate pinning | `OkHttp CertificatePinner` for TMDB domain |
