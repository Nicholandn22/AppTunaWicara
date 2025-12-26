# Tuna Wicara - Speech Therapy App

An Android application for speech therapy exercises built with Jetpack Compose and Clean Architecture.

## 🏗️ Architecture

This project follows **Clean Architecture** principles with three distinct layers:

### Domain Layer (`domain/`)
- **Models**: Core business entities (`Exercise`, `Progress`)
- **Repositories**: Interface definitions for data operations
- **Use Cases**: Business logic encapsulation
- ✅ No Android dependencies

### Data Layer (`data/`)
- **Models**: Data entities for database/network
- **Repository Implementations**: Concrete implementations of repository interfaces
- **Mappers**: Convert between domain and data models

### Presentation Layer (`presentation/`)
- **UI**: Jetpack Compose screens and components
- **ViewModels**: State management and UI logic
- **Navigation**: Navigation graph and routes
- **Theme**: Material Design 3 theming

## 🛠️ Tech Stack

- **Language**: Kotlin
- **UI**: Jetpack Compose with Material Design 3
- **Architecture**: Clean Architecture + MVVM
- **Dependency Injection**: Hilt
- **Navigation**: Navigation Compose
- **Async**: Kotlin Coroutines & Flow
- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 34 (Android 14)

## 📦 Project Structure

```
app/
├── src/main/java/com/tunawicara/app/
│   ├── domain/              # Business logic layer
│   │   ├── model/           # Domain entities
│   │   ├── repository/      # Repository interfaces
│   │   └── usecase/         # Use cases
│   ├── data/                # Data layer
│   │   ├── model/           # Data entities
│   │   ├── repository/      # Repository implementations
│   │   └── mapper/          # Data↔Domain mappers
│   ├── presentation/        # UI layer
│   │   ├── home/            # Home screen
│   │   ├── navigation/      # Navigation setup
│   │   └── theme/           # Material 3 theme
│   ├── di/                  # Dependency injection modules
│   ├── MainActivity.kt      # Main entry point
│   └── SpeechTherapyApplication.kt  # Application class
```

## 🚀 Getting Started

### Prerequisites
- Android Studio Hedgehog | 2023.1.1 or newer
- JDK 17 or newer
- Android SDK with API 34

### Setup

1. **Clone or open the project in Android Studio**

2. **Download Gradle Wrapper JAR** (first time only):
   ```bash
   # The gradle-wrapper.jar file needs to be downloaded
   # You can do this by running:
   gradle wrapper
   ```
   Or download it manually from the Gradle website and place it in `gradle/wrapper/`

3. **Sync Gradle**:
   - Open the project in Android Studio
   - Click "Sync Project with Gradle Files"

4. **Run the app**:
   - Connect an Android device or start an emulator
   - Click the Run button or press Shift+F10

## 📱 Features

### Current Features
- ✅ Home screen with exercise list
- ✅ Sample speech therapy exercises (Indonesian content)
- ✅ Category-based exercise organization
- ✅ Difficulty levels (Beginner, Intermediate, Advanced)
- ✅ Material Design 3 theming with dynamic colors

### Exercise Categories
- **Artikulasi** (Articulation) - Speech sound production
- **Fonasi** (Phonation) - Voice production
- **Resonansi** (Resonance) - Voice quality
- **Pernapasan** (Breathing) - Breathing exercises
- **Kosakata** (Vocabulary) - Word and language exercises

## 🔧 Build & Run

### Debug Build
```bash
./gradlew assembleDebug
```

### Release Build
```bash
./gradlew assembleRelease
```

### Run Tests
```bash
./gradlew test
```

## 📝 License

This project is part of a thesis/research project (Skripsi).

## 🤝 Contributing

This is an educational project. Feel free to fork and modify for your own learning purposes.

---

**Package**: `com.tunawicara.app`  
**App Name**: Tuna Wicara  
**Version**: 1.0
