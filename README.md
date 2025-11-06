# Senate Way Guesthouse - Final Project Submission

[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/EhCpHm4J)

A comprehensive guesthouse management application suite consisting of both a native Android mobile application and a web application, developed for the Senate Way Guesthouse located in Kimberley, Northern Cape, South Africa.

## 📋 Table of Contents

- [Overview](#overview)
- [Project Structure](#project-structure)
- [Features](#features)
- [Technologies](#technologies)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Android App Setup](#android-app-setup)
  - [Web Application Setup](#web-application-setup)
- [Configuration](#configuration)
- [Testing](#testing)
- [Project Documentation](#project-documentation)
- [Contributors](#contributors)
- [License](#license)

## 🎯 Overview

This repository contains the complete implementation of the Senate Way Guesthouse booking and management system, featuring:

- **Native Android Application** (`SenateWayApp/`): A fully-featured Kotlin-based mobile app with Material Design 3
- **Web Application** (`SenateWayGuesthouse-main/`): A React/TypeScript web application with comprehensive features
- **Comprehensive Testing Suite**: Unit tests, integration tests, UI tests, and performance tests
- **Documentation**: Project documentation, diagrams, and deployment guides

## 📁 Project Structure

```
├── SenateWayApp/                    # Native Android Application
│   ├── app/
│   │   ├── src/
│   │   │   ├── main/               # Main application code
│   │   │   ├── test/               # Unit tests
│   │   │   └── androidTest/         # Instrumented tests
│   │   ├── build.gradle            # App module dependencies
│   │   └── google-services.json    # Firebase configuration
│   ├── build.gradle                # Project-level build configuration
│   ├── README.md                   # Android app specific documentation
│   ├── TESTING_GUIDE.md            # Comprehensive testing guide
│   ├── QUICK_TEST_START.md         # Quick test execution guide
│   └── run_tests.ps1/sh            # Test execution scripts
│
├── SenateWayGuesthouse-main/        # Web Application (React/TypeScript)
│   └── SenateWayGuesthouse-main/
│       ├── pages/                  # React components
│       ├── hooks/                  # Custom React hooks
│       ├── __tests__/              # Test suites
│       ├── package.json
│       └── README.md               # Web app documentation
│
├── diagramnew.drawio               # System architecture diagram
├── INSY7315 POE.pdf                # Project documentation
└── README.md                       # This file
```

## ✨ Features

### Android Application Features

- 🏠 **Home Screen**: Hero section with features showcase and about section
- 🛏️ **Rooms**: Browse available rooms with filtering capabilities
- 📸 **Gallery**: Photo gallery with image viewer functionality
- ⭐ **Reviews**: Guest reviews and ratings display
- 📍 **Location**: Interactive Google Maps integration
- 📧 **Contact & Booking**: Contact form with Firebase integration
- 🤖 **AI Chatbot**: AI-powered assistant for guest inquiries
- 👨‍💼 **Admin Dashboard**: Admin login and booking management
- 🌓 **Dark Mode**: Theme switching support
- 🔔 **Real-time Updates**: Firebase Realtime Database integration
- 📱 **Material Design 3**: Modern, responsive UI

### Web Application Features

- Responsive design with Tailwind CSS
- Firebase integration for data management
- Weather API integration
- Admin dashboard with analytics
- Booking management system
- Review and rating system
- Interactive map integration

## 🛠️ Technologies

### Android Application

- **Language**: Kotlin 2.1.0
- **Minimum SDK**: API 24 (Android 7.0)
- **Target SDK**: API 34 (Android 14)
- **UI Framework**: Material Design 3
- **Architecture**: MVVM with Android Architecture Components
- **Backend**: Firebase (Realtime Database, Authentication, Analytics)
- **Key Libraries**:
  - AndroidX Navigation Component
  - Lifecycle Components
  - Glide (Image Loading)
  - Coil (SVG support)
  - Google Maps SDK
  - Coroutines
  - Retrofit/OkHttp

### Web Application

- **Framework**: React 18
- **Language**: TypeScript
- **Styling**: Tailwind CSS
- **Build Tool**: Vite
- **Backend**: Firebase
- **Deployment**: Netlify

## 🚀 Getting Started

### Prerequisites

#### For Android Development
- Android Studio (latest version recommended)
- JDK 17 or higher
- Android SDK (API 24+)
- Firebase account
- Google Maps API key
- AccuWeather API key (optional, for weather features)
- Gemini API key (optional, for chatbot features)

#### For Web Development
- Node.js 18+ and npm
- Firebase account

### Android App Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/IIEWFL/insy7315-final-project-submission-dimexl.git
   cd insy7315-final-project-submission-dimexl/SenateWayApp
   ```

2. **Configure Firebase**
   - Create a Firebase project at [Firebase Console](https://console.firebase.google.com/)
   - Download `google-services.json` and place it in `app/` directory
   - Enable Firebase Realtime Database and Authentication in Firebase Console

3. **Configure API Keys**
   - Open `app/src/main/res/values/strings.xml`
   - Replace the placeholder API keys:
     ```xml
     <string name="google_maps_key">YOUR_GOOGLE_MAPS_API_KEY</string>
     <string name="accuweather_api_key">YOUR_ACCUWEATHER_API_KEY</string>
     <string name="gemini_api_key">YOUR_GEMINI_API_KEY</string>
     ```

4. **Build and Run**
   ```bash
   # Windows
   gradlew.bat build
   gradlew.bat installDebug
   
   # Linux/Mac
   ./gradlew build
   ./gradlew installDebug
   ```

   Or open the project in Android Studio and click "Run".

### Web Application Setup

1. **Navigate to web application directory**
   ```bash
   cd SenateWayGuesthouse-main/SenateWayGuesthouse-main
   ```

2. **Install dependencies**
   ```bash
   npm install
   ```

3. **Configure Firebase**
   - Update `firebaseConfig.ts` with your Firebase configuration
   - Set up Firebase project with Realtime Database

4. **Run development server**
   ```bash
   npm run dev
   ```

5. **Build for production**
   ```bash
   npm run build
   ```

See `SenateWayGuesthouse-main/SenateWayGuesthouse-main/README.md` for detailed web application setup.

## ⚙️ Configuration

### Android App Configuration

#### API Keys Setup
All API keys are stored in `app/src/main/res/values/strings.xml`. Replace placeholders with your actual keys:

- **Google Maps API Key**: Required for map functionality
- **AccuWeather API Key**: Optional, for weather information
- **Gemini API Key**: Optional, for AI chatbot features
- **EmailJS Keys**: For email notifications

#### Firebase Setup
1. Create a Firebase project
2. Add Android app to Firebase project
3. Download `google-services.json` and place in `app/` directory
4. Enable required Firebase services:
   - Realtime Database
   - Authentication
   - Analytics (optional)

### Environment Variables

For security, consider using environment variables or a configuration file for sensitive data in production builds.

## 🧪 Testing

### Android Application Testing

The Android app includes comprehensive test suites:

#### Quick Test Execution

**Windows:**
```powershell
.\run_tests.ps1
```

**Linux/Mac:**
```bash
./run_tests.sh
```

#### Test Categories

| Test Type | Location | Command |
|-----------|----------|---------|
| **Functional** | `test/functional/` | `./gradlew test --tests *.functional.*` |
| **Performance** | `test/performance/` | `./gradlew test --tests *.performance.*` |
| **Security** | `test/security/` | `./gradlew test --tests *.security.*` |
| **Integration** | `test/integration/` | `./gradlew test --tests *.integration.*` |
| **API** | `test/api/` | `./gradlew test --tests *.api.*` |
| **Usability** | `androidTest/usability/` | `./gradlew connectedAndroidTest --tests *.usability.*` |
| **Compatibility** | `androidTest/compatibility/` | `./gradlew connectedAndroidTest --tests *.compatibility.*` |
| **End-to-End** | `androidTest/e2e/` | `./gradlew connectedAndroidTest --tests *.e2e.*` |
| **Regression** | `androidTest/regression/` | `./gradlew connectedAndroidTest --tests *.regression.*` |

#### Test Reports

After running tests, view detailed reports:
- **Unit Tests**: `app/build/reports/tests/test/index.html`
- **Instrumented Tests**: `app/build/reports/androidTests/connected/index.html`

For detailed testing documentation, see:
- `SenateWayApp/QUICK_TEST_START.md` - Quick reference guide
- `SenateWayApp/TESTING_GUIDE.md` - Comprehensive testing documentation
- `SenateWayApp/RUN_TESTS_IN_STUDIO.md` - Android Studio testing guide

### Web Application Testing

See `SenateWayGuesthouse-main/SenateWayGuesthouse-main/README_TESTING.md` for web application testing instructions.

## 📚 Project Documentation

- **Android App README**: `SenateWayApp/README.md` - Detailed Android app documentation
- **Testing Guide**: `SenateWayApp/TESTING_GUIDE.md` - Comprehensive testing documentation
- **Quick Test Start**: `SenateWayApp/QUICK_TEST_START.md` - Quick testing reference
- **Web App README**: `SenateWayGuesthouse-main/SenateWayGuesthouse-main/README.md`
- **Web App Testing**: `SenateWayGuesthouse-main/SenateWayGuesthouse-main/README_TESTING.md`
- **Migration Summary**: `SenateWayGuesthouse-main/SenateWayGuesthouse-main/MIGRATION_SUMMARY.md`
- **Deployment Guide**: `SenateWayGuesthouse-main/SenateWayGuesthouse-main/NETLIFY_DEPLOYMENT.md`
- **Architecture Diagram**: `diagramnew.drawio` - System architecture visualization
- **Project Documentation**: `INSY7315 POE.pdf` - Complete project documentation

## 🔒 Security Notes

⚠️ **Important**: This repository contains placeholder API keys. Before deploying:

1. Replace all placeholder API keys in `strings.xml` with your actual keys
2. Never commit real API keys to version control
3. Consider using environment variables or secure storage for production
4. Review and update Firebase security rules
5. Enable API key restrictions in Google Cloud Console

## 👥 Contributors

- **@ST10356407** (Sahil) - Project development
- **@Veeasha** - Project development

## 📄 License

This project is part of an academic submission for INSY7315. All rights reserved.

## 📞 Contact Information

**Senate Way Guesthouse**
- 📍 Address: 10 Senate Way, 8345 Kimberley, Northern Cape, South Africa
- 📞 Phone: +27 82 927 8907
- 📧 Email: vanessa141169@yahoo.com

## 🙏 Acknowledgments

- Material Design 3 components and guidelines
- Firebase for backend services
- Google Maps Platform
- All open-source libraries and frameworks used in this project

---

**Note**: This is a final project submission for academic purposes. For production deployment, ensure proper security measures, API key management, and comprehensive testing are performed.
