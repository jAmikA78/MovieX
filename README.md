# MovieX 🎬

A modern Android application for browsing, searching, and exploring movies and TV shows, powered by the [TMDB API](https://www.themoviedb.org/documentation/api).

> **UI Design**: [View on Figma](https://www.figma.com/design/72CIrzrnoh78pxNbQDgXRM/Filmo?node-id=0-1&p=f&t=4oYrxJi2oQ9WjhND-0) · inspired by [Filmo](https://www.figma.com/community/file/1006119758184707289)

---

## Screenshots

> [TBD: Screenshots] — Add screenshots of Splash, Onboarding, Home, Search, and Detail screens here.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Kotlin |
| UI | Jetpack Compose |
| Architecture | MVVM + Clean Architecture |
| Networking | Retrofit 2 + OkHttp |
| DI | Hilt |
| Async | Coroutines + Flow |
| Image Loading | Coil |
| Local Storage | Room *(planned)* |
| CI Build | Docker + `mingc/android-build-box` |

---

## Features

- 🎬 **Trending** — Browse trending movies and TV shows
- 🔍 **Search** — Find any movie or series by title
- 📄 **Details** — Full movie info, cast, ratings
- ❤️ **Favorites** — Save movies locally *(planned)*
- 🌐 **Onboarding** — Smooth intro flow on first launch

---

## Prerequisites

| Tool | Notes |
|---|---|
| Git | Any recent version |
| Android Studio Meerkat (2024.3.1+) | Recommended IDE |
| JDK 11+ | Bundled with Android Studio |
| Android SDK API 36 | `compileSdk` target |
| Docker | Optional — for CI/headless builds |

---

## Quick Start

```bash
# 1. Clone
git clone https://github.com/your-org/MovieX.git
cd MovieX

# 2. Add your TMDB API key
echo "TMDB_API_KEY=your_key_here" >> local.properties

# 3. Build & run
./gradlew assembleDebug
```

Or open in **Android Studio** → *Run → Run 'app'*.

---

## Documentation

| Document | Description |
|---|---|
| [Setup & Development](Docs/Setup%26Development.md) | Full developer guide: environment, build, ADB, Docker |
| [Architecture](Docs/Architecture.md) | MVVM layers, data flow, scalability |
| [Features](Docs/Features.md) | Feature breakdown, screens, ViewModels |
| [API Integration](Docs/API.md) | TMDB setup, endpoints, auth |

---

## Project Structure

```
app/src/main/java/com/depi/moviex/
├── core/           # Base classes, constants, extensions
├── data/           # API services, DTOs, repositories (impl)
├── di/             # Hilt modules
├── domain/         # Use cases, domain models, repo interfaces
├── presentation/   # UI screens, ViewModels, navigation
└── utils/          # Helpers, formatters, mappers
```

---

## Contributing

1. Fork the repo
2. Create a feature branch: `git checkout -b feature/your-feature`
3. Commit: `git commit -m 'feat: add your feature'`
4. Push: `git push origin feature/your-feature`
5. Open a Pull Request

Please follow [Conventional Commits](https://www.conventionalcommits.org/).

---

## License

[MIT License](LICENSE)
