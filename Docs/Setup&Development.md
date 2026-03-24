# Install & Run on Physical Device

After building the debug APK (via Docker or Android Studio), install it on a real Android device using `adb`.

## Prerequisites

- Enable **Developer options** → **USB debugging** on your phone  
  (Settings → About phone → tap Build number 7×)
- Install ADB (via Android Studio or standalone platform-tools):  
  <https://developer.android.com/tools/releases/platform-tools>
- Add `platform-tools` to your PATH (or run commands from that folder)

## Build Debug APK (Docker)

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

→ APK: `app/build/outputs/apk/debug/app-debug.apk`

## Install via USB

1. Connect phone via USB and allow debugging
2. Verify connection:

   ```bash
   adb devices
   ```

3. Setup on android device:

   ```bash
   # Uninstall previous version
   adb uninstall com.depi.moviex
   # Install new version
   adb install app/build/outputs/apk/debug/app-debug.apk
   # (Optional) Launch
   adb shell am start -n "com.depi.moviex/.MainActivity"
   ```


## Wireless ADB (Android 11+)

1. Connect via USB once, then run:

   ```bash
   adb tcpip 5555
   ```

2. Disconnect USB
3. Find phone’s IP (Settings → About phone → Status / Wi-Fi)
4. Connect wirelessly:

   ```bash
   adb connect 192.168.1.xxx:5555    # ← replace with your IP
   ```

5. Now install wirelessly (same command as above)

**Troubleshooting tip**: "device unauthorized"? → Re-allow USB debugging, or run `adb kill-server && adb start-server`.

## Other Docker Builds

```bash
# Release AAB (Google Play)
docker run --rm -v $(pwd):/project mingc/android-build-box:latest \
  bash -c 'cd /project && ./gradlew bundleRelease'
# → app/build/outputs/bundle/release/app-release.aab

# Interactive shell (run any Gradle task)
docker run -it --rm -v $(pwd):/project mingc/android-build-box:latest bash -l
# Then inside: ./gradlew tasks / assembleRelease / lint / test / etc.
```

## (Optional) Faster repeated builds – Gradle cache

```bash
mkdir -p ~/.gradle-docker-cache

# Example (macOS/Linux)
docker run --rm -v $(pwd):/project \
  -v ~/.gradle-docker-cache:/root/.gradle \
  mingc/android-build-box:latest \
  bash -c 'cd /project && ./gradlew assembleDebug --no-daemon'
```

This keeps builds reproducible while making installation straightforward using standard `adb`.
