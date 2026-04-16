# Setup & Development Guide

This guide covers everything needed to set up, build, and run MovieX locally.

---

## Table of Contents

1. [Environment Requirements](#1-environment-requirements)
2. [Installation](#2-installation)
3. [API Key Setup](#3-api-key-setup)
4. [Building the App](#4-building-the-app)
5. [Running the App](#5-running-the-app)
6. [ADB Installation](#6-adb-installation)
7. [Docker Build](#7-docker-build)
8. [Code Navigation](#8-code-navigation)
9. [Debugging](#9-debugging)
10. [Troubleshooting](#10-troubleshooting)

---

## 1. Environment Requirements

### Required Tools

| Tool | Version | Notes |
|------|---------|-------|
| Android Studio | Meerkat (2024.3.1+) | Recommended IDE |
| JDK | 11+ | Bundled with Android Studio |
| Android SDK | API 36 | `compileSdk` target |
| Android SDK Min | API 24 | Covers 99%+ devices |
| Gradle | 8.2+ | Included in wrapper |
| Git | Any recent | |

### SDK Setup

In Android Studio's **SDK Manager**, install:

- Android 14 (API 35) SDK Platform
- Android 15 (API 36) SDK Platform
- Android SDK Build-Tools 36+
- Android Emulator
- Android SDK Platform-Tools

### Recommended Plugins

- Kotlin (bundled)
- Compose Multiplatform
- GitToolBox

---

## 2. Installation

```bash
# Clone the repository
git clone https://github.com/jAmikA78/MovieX.git
cd MovieX

# Open in Android Studio
# File → Open → select MovieX folder
```

Wait for Gradle sync to complete (first run downloads dependencies).

---

## 3. API Key Setup

> **Important**: Never commit API keys. The `local.properties` file is git-ignored.

Add your TMDB API key to `local.properties` in the project root:

```properties
# Android SDK path (auto-generated)
sdk.dir=/path/to/your/Android/Sdk

# TMDB API credentials
TMDB_API_KEY=your_tmdb_api_key_here
```

Get a free key at [themoviedb.org → Settings → API](https://www.themoviedb.org/settings/api).

### CI/Environment Variable Approach

```bash
export TMDB_API_KEY="your_key"
./gradlew assembleDebug -PTMDB_API_KEY="$TMDB_API_KEY"
```

---

## 4. Building the App

### Build Commands

```bash
# Debug APK
./gradlew assembleDebug

# Release APK (requires signing)
./gradlew assembleRelease

# Release AAB (Google Play)
./gradlew bundleRelease

# Run tests
./gradlew test

# Run lint
./gradlew lint
```

### Build Variants

| Variant | Purpose | Output |
|---------|---------|--------|
| debug | Development, debuggable | `app-debug.apk` |
| release | Production, minified | `app-release.aab` |

### Output Location

```
app/build/outputs/apk/debug/app-debug.apk
```

---

## 5. Running the App

### Android Studio

1. Select a device/emulator from the toolbar
2. Click **Run** or press `Shift+F10`

### Emulator Setup

**Device Manager → Create Device**:

- Device: Pixel 7 or Pixel 8
- System Image: API 35 (Android 14, x86_64)
- RAM: 2 GB+
- Storage: 4 GB+

### Command Line

```bash
# Install to connected device
./gradlew installDebug

# Or manually install
adb install app/build/outputs/apk/debug/app-debug.apk
```

---

## 6. ADB Installation

### Prerequisites

1. Enable **Developer Options** on device: Settings → About phone → tap Build number 7 times
2. Enable **USB Debugging** in Developer Options
3. Install [platform-tools](https://developer.android.com/tools/releases/platform-tools)
4. Add to PATH

### USB Installation

```bash
# Verify connection
adb devices

# Uninstall previous version
adb uninstall com.depi.moviex

# Install new build
adb install app/build/outputs/apk/debug/app-debug.apk

# Launch
adb shell am start -n "com.depi.moviex/.MainActivity"
```

### Wireless ADB (Android 11+)

```bash
# Enable TCP mode
adb tcpip 5555

# Find device IP: Settings → About → Status → IP address

# Connect wirelessly
adb connect 192.168.1.XXX:5555

# Install
adb install app/build/outputs/apk/debug/app-debug.apk
```

### Troubleshooting

- Device unauthorized: Re-accept RSA key on device
- Issues: Run `adb kill-server && adb start-server`

---

## 7. Docker Build

Build without local Android SDK using Docker.

### Basic Build

```bash
# macOS/Linux
docker run --rm -v $(pwd):/project mingc/android-build-box:latest \
  bash -c 'cd /project && ./gradlew assembleDebug'

# Windows PowerShell
docker run --rm -v ${PWD}:/project mingc/android-build-box:latest \
  bash -c 'cd /project && ./gradlew assembleDebug'
```

### With Gradle Cache (Faster)

```bash
# Create cache directory
mkdir -p ~/.gradle-docker-cache

# Build with cache
docker run --rm \
  -v $(pwd):/project \
  -v ~/.gradle-docker-cache:/root/.gradle \
  mingc/android-build-box:latest \
  bash -c 'cd /project && ./gradlew assembleDebug --no-daemon'
```

### Other Commands

```bash
# Release AAB
docker run --rm -v $(pwd):/project mingc/android-build-box:latest \
  bash -c 'cd /project && ./gradlew bundleRelease'

# Run tests
docker run --rm -v $(pwd):/project mingc/android-build-box:latest \
  bash -c 'cd /project && ./gradlew test'

# Interactive shell
docker run -it --rm -v $(pwd):/project mingc/android-build-box:latest bash -l
```

### Limitations

| Limitation | Note |
|------------|------|
| Emulator | Cannot run inside Docker (requires KVM) |
| Preview | No Compose preview in Docker |
| Use | Use for builds/tests only |

---

## 8. Code Navigation

| Task | Location |
|------|----------|
| Change screen UI | `ui/theme/screens/<feature>/` |
| Modify API call | `data/remote/api/` |
| Add business logic | `domain/usecase/` |
| Update DI bindings | `di/` |
| Change theme/colors | `ui/theme/` |
| Update dependencies | `gradle/libs.versions.toml` |

---

## 9. Debugging

### Logcat

```bash
# All MovieX logs
adb logcat -s MovieX

# Network logs
adb logcat | grep OkHttp
```

### Enable HTTP Logging

In `di/` module (debug builds only):

```kotlin
if (BuildConfig.DEBUG) {
    addInterceptor(HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    })
}
```

### Layout Inspector

Android Studio → **View → Tool Windows → Layout Inspector**

---

## 10. Troubleshooting

| Problem | Solution |
|---------|----------|
| Gradle sync fails | Delete `.gradle/` folder, re-sync |
| API key not found | Verify `local.properties`, rebuild |
| Device not in ADB | Enable USB debugging, accept RSA key |
| Preview not loading | File → Invalidate Caches → Restart |
| 401 Unauthorized | Check TMDB key validity |
| Docker fails | Ensure daemon running, check volume path |