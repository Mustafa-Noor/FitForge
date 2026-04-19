# FitForge - Your Personal Fitness Coach

A modern Android fitness tracking application built with Kotlin and Jetpack libraries. FitForge helps users log workouts, track their fitness journey, and stay motivated with personalized notifications.

## 📱 Features

- **Workout Logging**: Easily log your workouts with exercise details
- **Workout History**: Track your fitness progress over time
- **Exercise Library**: Browse a comprehensive library of exercises
- **User Profile**: Manage your personal fitness information
- **Smart Notifications**: Get motivational reminders and notifications
- **Persistent Alarms**: Receive notifications even after device restart
- **Offline-First**: Works seamlessly without internet connection
- **Material Design**: Modern, intuitive user interface

## 🛠️ Tech Stack

### Android & Framework
- **Minimum SDK**: Android 7.0 (API 24)
- **Target SDK**: Android 15 (API 35)
- **Language**: Kotlin 2.0.21
- **Build System**: Gradle 8.13.2

### Key Libraries
- **Jetpack Components**:
  - AndroidX Core KTX for Kotlin extensions
  - AppCompat for backward compatibility
  - Lifecycle (Runtime, ViewModel) for lifecycle-aware components
  - ConstraintLayout for flexible UI layouts
  - RecyclerView for efficient list/grid displays
  - CardView for card-based UI components

- **Coroutines**: For asynchronous and non-blocking operations
- **Serialization**: Kotlinx Serialization JSON for data persistence
- **Material Design**: Material Design 3 components
- **Testing**: JUnit 4, AndroidX JUnit, Espresso

## 📋 Project Structure

```
app/
├── src/
│   ├── main/
│   │   ├── java/com/example/fitforge/
│   │   │   ├── MainActivity.kt              # Entry point & splash screen
│   │   │   ├── activities/                  # All screen activities
│   │   │   │   ├── WelcomeActivity.kt       # Onboarding flow
│   │   │   │   ├── HomeActivity.kt          # Dashboard
│   │   │   │   ├── LogWorkoutActivity.kt    # Workout logging
│   │   │   │   ├── HistoryActivity.kt       # Workout history
│   │   │   │   ├── ExerciseLibraryActivity.kt
│   │   │   │   ├── ProfileActivity.kt       # User profile
│   │   │   │   └── SettingsActivity.kt
│   │   │   ├── data/                        # Data layer
│   │   │   │   ├── ExerciseData.kt
│   │   │   │   ├── SharedPreferencesManager.kt
│   │   │   │   └── models/                  # Data models
│   │   │   ├── adapters/                    # RecyclerView adapters
│   │   │   ├── utils/                       # Utility functions
│   │   │   └── notifications/               # Notification handling
│   │   │       ├── FitNotificationManager.kt
│   │   │       ├── RoastReceiver.kt
│   │   │       └── BootReceiver.kt
│   │   └── res/                             # Resources (layouts, strings, etc.)
│   ├── androidTest/                         # Instrumented tests
│   └── test/                                # Unit tests
├── build.gradle.kts                         # App-level build config
└── AndroidManifest.xml                      # App manifest
```

## 🔐 Permissions

- `POST_NOTIFICATIONS` - Send push notifications to users
- `RECEIVE_BOOT_COMPLETED` - Launch reminders after device restart
- `SCHEDULE_EXACT_ALARM` - Schedule precise workout reminders

## 🚀 Getting Started

### Prerequisites
- Android Studio (latest version)
- Java 11 or higher
- Android SDK (API level 24+)
- Gradle 8.13.2

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd FitForge
   ```

2. **Open in Android Studio**
   - Open Android Studio
   - Select "Open an Existing Project"
   - Navigate to the FitForge directory

3. **Build the project**
   ```bash
   ./gradlew build
   ```

4. **Run on emulator or device**
   ```bash
   ./gradlew installDebug
   ```

### Building for Release

```bash
./gradlew assembleRelease
```

The APK will be generated in `app/build/outputs/apk/release/`

## 📦 Dependencies

### Version Catalog
Dependencies are managed through `gradle/libs.versions.toml` for centralized version management.

**Core Dependencies:**
- `androidx-core-ktx:1.13.1`
- `androidx-appcompat:1.7.0`
- `androidx-material:1.12.0`
- `androidx-lifecycle-runtime-ktx:2.8.7`
- `kotlinx-coroutines-android:1.9.0`
- `kotlinx-serialization-json:1.7.3`

**UI Components:**
- `androidx-constraintlayout:2.1.4`
- `androidx-recyclerview:1.3.2`
- `androidx-cardview:1.0.0`

**Testing:**
- `junit:4.13.2`
- `androidx-junit:1.2.1`
- `androidx-espresso-core:3.6.1`

## 🎯 App Flow

1. **Launch** → MainActivity shows splash screen (2-second delay)
2. **First Launch** → WelcomeActivity (onboarding)
3. **Returning User** → HomeActivity (dashboard)
4. **Navigation** → Users can access:
   - Log Workout
   - View History
   - Browse Exercises
   - Edit Profile
   - Configure Settings

## 🔔 Notifications System

- **FitNotificationManager**: Creates and manages notification channels
- **RoastReceiver**: Handles scheduled notifications
- **BootReceiver**: Ensures alarms are reset after device restart

## 💾 Data Management

- **SharedPreferencesManager**: Handles local data persistence
- **ExerciseData**: Manages exercise database and models
- Uses Kotlinx Serialization for JSON serialization/deserialization

## 🧪 Testing

### Run Unit Tests
```bash
./gradlew test
```

### Run Instrumented Tests
```bash
./gradlew connectedAndroidTest
```

## 🔧 Build Configuration

- **Compile SDK**: 35
- **Min SDK**: 24
- **Target SDK**: 35
- **Java Version**: 11
- **Kotlin Compiler**: JVM 11

### Build Types
- **Debug**: Development builds with no optimization
- **Release**: Optimized builds with ProGuard rules enabled

## 📝 ProGuard Rules

Configured in `app/proguard-rules.pro` for code shrinking and obfuscation in release builds.

## 🤝 Contributing

1. Create a feature branch
2. Make your changes
3. Run tests to ensure functionality
4. Submit a pull request



## 🐛 Troubleshooting

### Build Issues
- Clean and rebuild: `./gradlew clean build`
- Update Gradle: `./gradlew wrapper --gradle-version 8.13.2`
- Invalidate cache in Android Studio: File → Invalidate Caches

### Runtime Issues
- Check minimum SDK compatibility (API 24+)
- Verify notification permissions on Android 13+
- Ensure POST_NOTIFICATIONS permission is granted

## 📚 Resources

- [Android Documentation](https://developer.android.com/)
- [Kotlin Documentation](https://kotlinlang.org/)
- [Jetpack Libraries](https://developer.android.com/jetpack)
- [Material Design](https://material.io/design)
