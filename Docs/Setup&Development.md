# Setup & Development

1. Clone the repository

   ```bash
   git clone https://github.com/jamika78/moviex.git
   cd moviex
   ```

2. (Recommended) Open the project in **Android Studio**

   - Open → select the `moviex` folder
   - Let Gradle sync (it will download dependencies)
   - You can now edit code, see Compose previews, run on emulator/device normally

## Build with Docker (CI / Reproducible builds)

This project uses the popular `mingc/android-build-box` Docker image — it contains a complete Android SDK + Gradle environment.

### 1. Build **debug** APK

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

→ APK location:  
`app/build/outputs/apk/debug/app-debug.apk`

Here’s a clean **new section** you can add to your existing `README.md` file.  
It explains how to get the built debug APK onto your physical Android device (via USB or wirelessly) — since direct installation from inside Docker is tricky/unreliable.

#### Install & Run on Physical Device (USB Debugging)

After building the debug APK with Docker, you can install it on a real Android phone/tablet for testing.  
**You do not** run `./gradlew installDebug` inside the Docker container (USB device passthrough is complex and often unreliable, especially on Windows/macOS).

Instead: build once with Docker → install using normal `adb` on your computer.

##### Install & Run Prerequisites

- Enable **Developer options** on your phone  
  (Settings → About phone → tap Build number 7 times)
- Enable **USB debugging** (Developer options → USB debugging)
- Install ADB on your computer (easiest via Android Studio or standalone platform-tools):
  - Download: <https://developer.android.com/tools/releases/platform-tools>
  - Add the `platform-tools` folder to your PATH (or run commands from inside that folder)

##### Step 1: Build the debug APK (using Docker)

```bash
# macOS / Linux
docker run --rm -v $(pwd):/project mingc/android-build-box:latest \
  bash -c 'cd /project && ./gradlew assembleDebug'

# Windows PowerShell
docker run --rm -v ${PWD}:/project mingc/android-build-box:latest \
  bash -c 'cd /project && ./gradlew assembleDebug'
```

APK path: `app/build/outputs/apk/debug/app-debug.apk`

##### Step 2: Connect your device and install

1. Connect phone via USB cable  
2. Allow USB debugging when the popup appears on your phone

3. Check connection (run on your computer, **not** inside Docker):

   ```bash
   adb devices
   ```

   → You should see something like:  
   `List of devices attached`  
   `1234567890abcdef    device`

4. Install the APK:

   ```bash
   adb install app/build/outputs/apk/debug/app-debug.apk
   ```

   → Output should end with: `Success`

5. (Optional) Launch the app automatically:

   ```bash
   adb shell am start -n "com.depi.moviex/.MainActivity"
   ```

##### Wireless ADB (no cable needed after setup)

Works great on Android 11+ (most devices in 2026)

1. Connect phone via USB **once**
2. Run:

   ```bash
   adb tcpip 5555
   ```

3. Disconnect USB cable
4. Find your phone's IP address (Settings → About phone → Status or Wi-Fi settings)
5. Connect wirelessly:

   ```bash
   adb connect 192.168.1.100:5555    # ← replace with real IP
   ```

6. Now `adb install ...` works without cable (as long as phone & computer are on same Wi-Fi)

   ```bash
   adb install app/build/outputs/apk/debug/app-debug.apk
   ```

**Tip**: If you get "device unauthorized" → unplug/replug USB, re-allow debugging, or restart adb with `adb kill-server && adb start-server`.

This workflow keeps builds clean/reproducible in Docker while using standard tools for device installation & debugging.

### 2. Build **release** AAB (Google Play ready)

```bash
docker run --rm -v $(pwd):/project mingc/android-build-box:latest \
  bash -c 'cd /project && ./gradlew bundleRelease'
```

→ AAB location:  
`app/build/outputs/bundle/release/app-release.aab`

### 3. Run any Gradle task interactively

```bash
docker run -it --rm -v $(pwd):/project mingc/android-build-box:latest bash -l
```

Then inside the container:

```bash
./gradlew tasks                # see all tasks
./gradlew assembleRelease      # build release
./gradlew lint                 # run lint
./gradlew test                 # run unit tests
exit                           # leave container
```

## Faster repeated builds (optional)

Add Gradle caching by mounting a local cache folder:

```bash
mkdir -p ~/.gradle-docker-cache

# macOS / Linux example
docker run --rm -v $(pwd):/project \
  -v ~/.gradle-docker-cache:/root/.gradle \
  mingc/android-build-box:latest \
  bash -c 'cd /project && ./gradlew assembleDebug --no-daemon'
```

### Recommended Docker tag (for stability)

Instead of `:latest`, you can pin a version (check latest tags here: <https://hub.docker.com/r/mingc/android-build-box/tags>):

```dockerfile
mingc/android-build-box:1.29.0    # example — update when needed
```