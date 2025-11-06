# Senate Way Guesthouse - Final Project Submission

[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/EhCpHm4J)

A comprehensive guesthouse management application suite consisting of both a native Android mobile application and a web application, developed for the Senate Way Guesthouse located in Kimberley, Northern Cape, South Africa.

## Table of Contents

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

## Overview

This repository contains the complete implementation of the Senate Way Guesthouse booking and management system, featuring:

- **Native Android Application** (`SenateWayApp/`): A fully-featured Kotlin-based mobile app with Material Design 3
- **Web Application** (`SenateWayGuesthouse-main/`): A React/TypeScript web application with comprehensive features
- **Comprehensive Testing Suite**: Unit tests, integration tests, UI tests, and performance tests
- **Documentation**: Project documentation, diagrams, and deployment guides

## Project Structure

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

## Features

### Android Application Features

- **Home Screen**: Hero section with features showcase and about section
- **Rooms**: Browse available rooms with filtering capabilities
- **Gallery**: Photo gallery with image viewer functionality
- **Reviews**: Guest reviews and ratings display
- **Location**: Interactive Google Maps integration
- **Contact & Booking**: Contact form with Firebase integration
- **AI Chatbot**: AI-powered assistant for guest inquiries
- **Admin Dashboard**: Admin login and booking management
- **Dark Mode**: Theme switching support
- **Real-time Updates**: Firebase Realtime Database integration
- **Material Design 3**: Modern, responsive UI

### Web Application Features

- Responsive design with Tailwind CSS
- Firebase integration for data management
- Weather API integration
- Admin dashboard with analytics
- Booking management system
- Review and rating system
- Interactive map integration

## Technologies

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

## Getting Started

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

## Configuration

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

## Testing

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

## Complete Testing Guide

This document provides a comprehensive overview of all testing implemented in the Senate Way Guesthouse application.

### Testing Overview

The project implements a comprehensive testing strategy covering:

- **Unit Testing** - Individual component and function tests
- **Integration Testing** - Component interaction tests
- **Functional Testing** - User story validation
- **Regression Testing** - Critical path verification
- **Performance Testing** - Web vitals and performance metrics
- **Security Testing** - Vulnerability scanning and validation
- **Accessibility Testing** - WCAG compliance
- **CI/CD Testing** - Automated testing in GitHub Actions

### Test Structure

```
__tests__/
├── integration/          # Integration tests
│   ├── navigation.test.tsx
│   └── form-submission.test.tsx
├── functional/          # Functional/user story tests
│   └── user-stories.test.tsx
├── regression/         # Regression tests
│   └── critical-path.test.tsx
├── accessibility/      # A11y tests
│   └── a11y.test.tsx
├── performance/       # Performance tests
│   └── web-vitals.test.ts
└── security/          # Security tests
    └── security.test.ts

docs/testing/          # Testing documentation
├── TEST_REPORT_TEMPLATE.md
└── ACCEPTANCE_CRITERIA.md
```

### Running Tests

#### All Tests
```bash
npm run test:all
```

#### Specific Test Types
```bash
# Unit tests
npm test

# Integration tests
npm run test:integration

# Functional tests
npm run test:functional

# Regression tests
npm run test:regression

# Performance tests
npm run test:performance

# Security audit
npm run test:security

# Accessibility tests (requires server running)
npm run dev  # In one terminal
npm run test:a11y  # In another terminal
```

#### Coverage Report
```bash
npm run test:coverage
```

### Test Coverage Goals

| Test Type | Target Coverage |
|-----------|----------------|
| Unit Tests | 90%+ |
| Integration | 70%+ |
| Functional | 100% of user stories |
| Regression | All critical paths |
| Security | 100% of checks |
| Accessibility | WCAG 2.1 AA |

### CI/CD Pipeline

The GitHub Actions workflow (`.github/workflows/ci.yml`) runs:

1. **Lint Check** - Code quality
2. **Unit Tests** - Fast feedback
3. **Integration Tests** - Component interactions
4. **Functional Tests** - User stories
5. **Regression Tests** - Critical paths
6. **Security Audit** - Vulnerability scan
7. **Build Verification** - Production build
8. **Performance Tests** - Web vitals

Triggered on:
- Every push to main/develop
- Pull requests
- Daily scheduled runs

### Test Documentation

#### Test Reports
- Template: `docs/testing/TEST_REPORT_TEMPLATE.md`
- Generate reports after each test run
- Include in Portfolio of Evidence (PoE)

#### Acceptance Criteria
- Document: `docs/testing/ACCEPTANCE_CRITERIA.md`
- Maps user stories to test cases
- Used for client sign-off

### Security Testing

#### Automated Checks
- `npm audit` - Dependency vulnerabilities
- ESLint security plugin - Code security issues
- Snyk (optional) - Advanced vulnerability scanning

#### Manual Checks
- Input sanitization
- XSS prevention
- Secure configuration
- Sensitive data handling

### Accessibility Testing

#### Automated
- Basic structure validation
- Semantic HTML checks
- ARIA label verification

#### Tools
- `pa11y` - CLI accessibility testing
- `@axe-core/react` - React component testing
- Lighthouse - Full accessibility audit

### Performance Testing

#### Metrics Tracked
- **FCP** (First Contentful Paint) - Target: < 1.8s
- **LCP** (Largest Contentful Paint) - Target: < 2.5s
- **CLS** (Cumulative Layout Shift) - Target: < 0.1
- **TTI** (Time to Interactive) - Target: < 3.8s

#### Tools
- Web Vitals API
- Lighthouse CI
- Bundle analyzer

### Real-World Testing

#### Network Conditions
- Slow 3G simulation
- Offline behavior
- Intermittent connectivity

#### Device Testing
- Desktop browsers
- Mobile devices
- Tablets

### Test Execution Checklist

Before release:
- [ ] All unit tests pass
- [ ] Integration tests pass
- [ ] Functional tests validate all user stories
- [ ] Regression tests verify critical paths
- [ ] Security audit passes
- [ ] Performance metrics meet targets
- [ ] Accessibility tests pass
- [ ] Build succeeds
- [ ] Documentation updated

### Reporting Issues

When tests fail:
1. Document the failure in test report
2. Create GitHub issue with:
   - Test name and location
   - Expected vs actual behavior
   - Steps to reproduce
   - Environment details
3. Fix and verify
4. Update test documentation

### Additional Resources

- **TDD Guide**: `TDD_GUIDE.md` - Test-Driven Development approach
- **Step-by-Step**: `TDD_STEP_BY_STEP.md` - Detailed TDD instructions
- **Quick Start**: `QUICK_START_TESTING.md` - Quick reference
- **Strategy**: `TESTING_STRATEGY.md` - Complete testing strategy




## Project Documentation

- **Android App README**: `SenateWayApp/README.md` - Detailed Android app documentation
- **Testing Guide**: `SenateWayApp/TESTING_GUIDE.md` - Comprehensive testing documentation
- **Quick Test Start**: `SenateWayApp/QUICK_TEST_START.md` - Quick testing reference
- **Web App README**: `SenateWayGuesthouse-main/SenateWayGuesthouse-main/README.md`
- **Web App Testing**: `SenateWayGuesthouse-main/SenateWayGuesthouse-main/README_TESTING.md`
- **Migration Summary**: `SenateWayGuesthouse-main/SenateWayGuesthouse-main/MIGRATION_SUMMARY.md`
- **Deployment Guide**: `SenateWayGuesthouse-main/SenateWayGuesthouse-main/NETLIFY_DEPLOYMENT.md`
- **Architecture Diagram**: `diagramnew.drawio` - System architecture visualization
- **Project Documentation**: `INSY7315 POE.pdf` - Complete project documentation

## Security Notes

**Important**: This repository contains placeholder API keys. Before deploying:

1. Replace all placeholder API keys in `strings.xml` with your actual keys
2. Never commit real API keys to version control
3. Consider using environment variables or secure storage for production
4. Review and update Firebase security rules
5. Enable API key restrictions in Google Cloud Console

## Contributors

- **@ST10356407** (Sahil) - Project development
- **@Veeasha** - Project development

## License

This project is part of an academic submission for INSY7315. All rights reserved.

## Contact Information

**Senate Way Guesthouse**
- Address: 10 Senate Way, 8345 Kimberley, Northern Cape, South Africa
- Phone: +27 82 927 8907
- Email: vanessa141169@yahoo.com

## Acknowledgments

- Material Design 3 components and guidelines
- Firebase for backend services
- Google Maps Platform
- All open-source libraries and frameworks used in this project

---
