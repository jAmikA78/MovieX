# Setup & Development Guide — MovieX

Complete guide for setting up, building, and running MovieX locally.

---

## Table of Contents

1. [Environment Setup](#1-environment-setup)
2. [Installation](#2-installation)
3. [API Key Configuration](#3-api-key-configuration)
4. [Build Variants](#4-build-variants)
5. [Running the App](#5-running-the-app)
6. [Install on Physical Device (ADB)](#6-install-on-physical-device-adb)
7. [Docker Build Support](#7-docker-build-support)
8. [Code Navigation Guide](#8-code-navigation-guide)
9. [Debugging](#9-debugging)
10. [Troubleshooting](#10-troubleshooting)

---

## 1. Environment Setup

### Required Tools

| Tool | Minimum Version | Notes |
|---|---|---|
| Android Studio | Meerkat (2024.3.1) | [Download](https://developer.android.com/studio) |
| JDK | 11 (bundled) | Use Studio's bundled JDK |
| Android SDK | API 36 (compileSdk) | Install via SDK Manager |
| Android SDK min | API 24 | Covers 99%+ devices |
| Git | Any modern | |
| Docker | 20+ | Optional — for CI/headless builds |

### Recommended Android Studio Plugins

- **Kotlin** (bundled)
- **Compose Multiplatform** (for Compose preview performance)
- **GitToolBox** (inline git blame)

### SDK Manager Setup

Open Android Studio → **SDK Manager** → install:
- Android 14 (API 35) SDK Platform
- Android 15 (API 36) SDK Platform *(compileSdk target)*
- Android SDK Build-Tools 36+
- Android Emulator
- Android SDK Platform-Tools

---

## 2. Installation

```bash
# Clone the repository
git clone https://github.com/your-org/MovieX.git
cd MovieX
```

Open in Android Studio: **File → Open → select `MovieX/` folder**.

Wait for Gradle sync to complete (first time downloads dependencies).

---

## 3. API Key Configuration

> **Important**: Never commit secrets. `local.properties` is git-ignored.

Create or edit `local.properties` in the project root:

```properties
# Android SDK path (auto-generated — do not share)
sdk.dir=/path/to/your/Android/Sdk

# TMDB API credentials
TMDB_API_KEY=your_tmdb_api_key_here
TMDB_BASE_URL=https://api.themoviedb.org/3/
```

Get a free TMDB key: [themoviedb.org → Settings → API](https://www.themoviedb.org/settings/api)

### Shared Team Setup (without committing keys)

Each developer adds their own `local.properties`. For CI environments, pass as environment variables:

```bash
export TMDB_API_KEY="your_key"
./gradlew assembleDebug -PTMDB_API_KEY="$TMDB_API_KEY"
```

---

## 4. Build Variants

| Variant | Description | Output |
|---|---|---|
| `debug` | Development build, debuggable, logging on | `app-debug.apk` |
| `release` | Production build, minified, signed | `app-release.aab` |

```bash
# Debug APK
./gradlew assembleDebug

# Release APK (requires signing config)
./gradlew assembleRelease

# Release AAB (Google Play)
./gradlew bundleRelease

# Run all unit tests
./gradlew test

# Run lint
./gradlew lint
```

---

## 5. Running the App

### Android Studio

1. Select a device/emulator from the toolbar dropdown
2. Click **Run ▶** or press `Shift+F10`

### Emulator Setup

**Android Studio → Device Manager → Create Device**:

Recommended config:
- **Device**: Pixel 7 or Pixel 8
- **System image**: API 35 (Android 14, x86_64)
- **RAM**: 2 GB+
- **Storage**: 4 GB+

### CLI

```bash
# Build and install to connected device
./gradlew installDebug

# Build only
./gradlew assembleDebug
# → app/build/outputs/apk/debug/app-debug.apk
```

---

## 6. Install on Physical Device (ADB)

### Prerequisites

- Enable **Developer Options** on your phone:  
  Settings → About phone → tap **Build number** 7 times
- Enable **USB Debugging** in Developer Options
- Install ADB: [platform-tools](https://developer.android.com/tools/releases/platform-tools)
- Add `platform-tools/` to your `PATH`

### Connect & Install via USB

```bash
# 1. Verify connection
adb devices

# 2. Uninstall previous version (if any)
adb uninstall com.depi.moviex

# 3. Install new build
adb install app/build/outputs/apk/debug/app-debug.apk

# 4. (Optional) Launch immediately
adb shell am start -n "com.depi.moviex/.MainActivity"
```

### Wireless ADB (Android 11+)

```bash
# 1. Connect via USB once and enable TCP
adb tcpip 5555

# 2. Disconnect USB
# 3. Find your device IP (Settings → About → Status → IP address)

# 4. Connect wirelessly
adb connect 192.168.1.XXX:5555

# 5. Install wirelessly (same install command as above)
```

**Troubleshooting**: If you see `device unauthorized`, re-accept the RSA key on your phone or:
```bash
adb kill-server && adb start-server
```

---

## 7. Docker Build Support

Use Docker to build without installing Android SDK locally. This is ideal for CI pipelines and teammates without Android Studio.

### Build Debug APK

```bash
# macOS / Linux
docker run --rm -v $(pwd):/project mingc/android-build-box:latest \
  bash -c 'cd /project && ./gradlew assembleDebug'

# Windows (PowerShell)
docker run --rm -v ${PWD}:/project mingc/android-build-box:latest \
  bash -c 'cd /project && ./gradlew assembleDebug'

# Windows (Command Prompt)
docker run --rm -v "%cd%:/project" mingc/android-build-box:latest \
  bash -c 'cd /project && ./gradlew assembleDebug'
```

Output: `app/build/outputs/apk/debug/app-debug.apk`

### Other Docker Build Commands

```bash
# Release AAB (Google Play)
docker run --rm -v $(pwd):/project mingc/android-build-box:latest \
  bash -c 'cd /project && ./gradlew bundleRelease'

# Run tests inside Docker
docker run --rm -v $(pwd):/project mingc/android-build-box:latest \
  bash -c 'cd /project && ./gradlew test'

# Interactive shell (inspect or run any Gradle task)
docker run -it --rm -v $(pwd):/project mingc/android-build-box:latest bash -l
```

### Faster Repeated Builds — Gradle Cache

```bash
# Create a directory for the Gradle cache (For first time only)
mkdir -p ~/.gradle-docker-cache

# Build the app
docker run --rm \
  -v $(pwd):/project \
  -v ~/.gradle-docker-cache:/root/.gradle \
  mingc/android-build-box:latest \
  bash -c 'cd /project && ./gradlew assembleDebug --no-daemon'
# Uninstall and install the app
adb uninstall com.depi.moviex
# Install the app
adb install app/build/outputs/apk/debug/app-debug.apk
# Run the app
adb shell am start -n "com.depi.moviex/.MainActivity"
```

### Docker Limitations

| Limitation | Notes |
|---|---|
| Emulator | Android emulator **cannot run inside Docker** (requires KVM/hardware virtualization) |
| UI preview | No Compose preview inside Docker |
| Workaround | Use Docker only for builds & tests; run/debug via Android Studio or ADB |

adb uninstall com.depi.moviex
adb install app/build/outputs/apk/debug/app-debug.apk

adb run-as com.depi.moviex

---

## 8. Code Navigation Guide

| You want to... | Go to... |
|---|---|
| Change a screen's UI | `presentation/<feature>/` |
| Add/modify API call | `data/remote/api/TmdbApiService.kt` |
| Add business logic | `domain/usecase/<Feature>UseCase.kt` |
| Change Hilt bindings | `di/` modules |
| Modify theme/colors | `presentation/theme/` |
| Update Gradle deps | `gradle/libs.versions.toml` |
---

## 9. Debugging

### Logcat Filters

```bash
# All MovieX logs
adb logcat -s MovieX

# Retrofit request/response
adb logcat | grep OkHttp
```

Enable HTTP logging in `NetworkModule.kt` (debug builds only):

```kotlin
if (BuildConfig.DEBUG) {
    addInterceptor(HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    })
}
```

### Compose Layout Inspector

Android Studio → **View → Tool Windows → Layout Inspector** — works with running Compose apps for live inspection.

---

## 10. Troubleshooting

| Problem | Solution |
|---|---|
| Gradle sync fails | Delete `.gradle/` folder, re-sync |
| `TMDB_API_KEY` empty | Verify `local.properties` has key; rebuild project |
| Device not listed in ADB | Enable USB debugging, accept RSA key on device |
| Compose previews not loading | Invalidate caches: **File → Invalidate Caches → Restart** |
| `401 Unauthorized` from API | TMDB key is wrong or expired — regenerate at TMDB settings |
| Docker build fails | Ensure Docker daemon is running; check volume mount path |
