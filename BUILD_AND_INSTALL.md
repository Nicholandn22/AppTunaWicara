# Build and Install APK to Phone

## Option 1: Build APK via Command Line

```bash
# Navigate to project directory
cd "c:\Download Browser\Skripsi PDF\AppTunaWicara"

# Build debug APK
.\gradlew assembleDebug

# APK will be created at:
# app\build\outputs\apk\debug\app-debug.apk
```

## Option 2: Install using ADB

```bash
# Make sure phone is connected and USB debugging is enabled

# Install the APK
adb install app\build\outputs\apk\debug\app-debug.apk

# Or if already installed, use -r to reinstall
adb install -r app\build\outputs\apk\debug\app-debug.apk
```

## Option 3: Transfer APK Manually

1. Build the APK (see Option 1)
2. Copy `app-debug.apk` to your phone via USB
3. On your phone:
   - Open **Files** app
   - Navigate to the APK file
   - Tap to install
   - Enable "Install from unknown sources" if prompted
   - Tap **Install**

## Check if Phone is Connected

```bash
# List connected devices
adb devices

# Should show something like:
# List of devices attached
# ABC123DEF456    device
```

## Troubleshooting

### Device Not Detected
- Ensure USB debugging is enabled
- Try a different USB cable (some cables are charge-only)
- Install phone manufacturer's USB drivers

### Build Errors
- First time: Run `gradle wrapper` to generate gradle-wrapper.jar
- Ensure you have JDK 17 installed
- In Android Studio: File → Invalidate Caches → Invalidate and Restart
