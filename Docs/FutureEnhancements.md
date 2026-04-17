# Future Enhancements

Planned improvements and scalability additions for MovieX.

---

## Near-Term

### Search Feature (Done)
- [x] Implement search screen with debounce input
- [x] Connect to `GET /search/movie` and `GET /search/tv` endpoints
- [x] Display search results in a list
- [x] Add empty and loading states

### Favorites Feature
- Set up Room database
- Create FavoriteEntity and DAO
- Implement FavoritesRepository
- Add toggle favorite functionality on movie cards
- Create dedicated Favorites screen

### Onboarding Persistence
- Store "onboarding seen" flag in DataStore
- On launch: check flag and route to appropriate screen

---

## Medium-Term

### Offline Caching
- Cache API responses in Room
- Serve cached data when offline
- Implement cache invalidation

### Genre Filtering
- Load genre list from `GET /genre/movie/list`
- Add genre filter chips to home screen
- Filter movies by selected genre

### TV Shows Enhancement
- Separate TV shows section
- TV detail screen with seasons/episodes
- Integrated TV search

---

## Long-Term

### Cast Screen
- Actor detail page with biography
- Known for movies section
- Filmography

### Dark/Light Mode
- Theme toggle in settings
- Persist preference in DataStore
- Dynamic color support (Android 12+)

### Profile Screen
- User profile display
- Account settings
- Theme and language preferences

### Multi-language Support
- String resources for multiple languages
- RTL layout support
- Language selection in settings

### User Authentication
- TMDB account authentication
- Synced watchlist across devices
- User profile management

### Push Notifications
- FCM integration for new release alerts
- Customizable notification preferences

### Internationalization
- Support multiple languages
- RTL layout support
- Localized strings

### Modularization

Split into Gradle modules:
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

### CI/CD Pipeline
- GitHub Actions for PR checks
- Automated Docker builds
- Release automation with Fastlane

---

## Performance

| Area | Optimization |
|------|---------------|
| Images | Coil disk + memory caching |
| Compose | `remember`, `derivedStateOf`, stable data classes |
| Network | OkHttp response caching |
| Startup | App Startup, lazy injection |

---

## Security

| Concern | Mitigation |
|---------|------------|
| API Key | BuildConfig + local.properties (not committed) |
| Deep Links | Verify intent filters, use https scheme |
| Release | Enable ProGuard/R8 |
| Certificates | OkHttp CertificatePinner for TMDB |