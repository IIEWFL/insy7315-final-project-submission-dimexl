# Testing Guide - Senate Way Guesthouse App

This document provides a comprehensive guide for running all test suites in the Senate Way Guesthouse Android application.

## Test Types

### 1. Functional Testing
**Location:** `app/src/test/java/com/senateway/guesthouse/functional/`

Tests that all features work correctly according to specifications:
- Room filtering functionality
- Booking form validation
- Gallery navigation
- Reviews display
- Map location
- Chatbot functionality

**Run:** `./gradlew test --tests com.senateway.guesthouse.functional.*`

### 2. Usability Testing
**Location:** `app/src/androidTest/java/com/senateway/guesthouse/usability/`

Tests user experience, navigation flow, and UI/UX aspects:
- Navigation flow
- Button visibility and accessibility
- Form usability
- Image gallery usability
- Text readability
- Loading states
- Error handling usability

**Run:** `./gradlew connectedAndroidTest --tests com.senateway.guesthouse.usability.*`

**Note:** Requires connected device or emulator

### 3. Performance Testing
**Location:** `app/src/test/java/com/senateway/guesthouse/performance/`

Tests app performance, memory usage, and response times:
- Image loading performance
- Room filtering performance
- RecyclerView performance
- Memory usage
- API response time
- Database query performance
- Activity launch time

**Run:** `./gradlew test --tests com.senateway.guesthouse.performance.*`

### 4. Compatibility Testing
**Location:** `app/src/androidTest/java/com/senateway/guesthouse/compatibility/`

Tests app compatibility across different Android versions and devices:
- Minimum SDK compatibility (API 24)
- Target SDK compatibility (API 34)
- Screen size compatibility
- Orientation compatibility
- API level compatibility
- Permission compatibility
- Feature compatibility
- Display density compatibility

**Run:** `./gradlew connectedAndroidTest --tests com.senateway.guesthouse.compatibility.*`

### 5. Security Testing
**Location:** `app/src/test/java/com/senateway/guesthouse/security/`

Tests security aspects:
- API key security
- SQL injection prevention
- XSS prevention
- Email validation
- Phone number validation
- Data encryption
- Firebase security rules
- Permission validation

**Run:** `./gradlew test --tests com.senateway.guesthouse.security.*`

### 6. Integration Testing
**Location:** `app/src/test/java/com/senateway/guesthouse/integration/`

Tests integration between different components and services:
- Firebase integration
- Google Maps integration
- Weather API integration
- EmailJS integration
- Gemini AI integration
- Navigation component integration
- ViewBinding integration
- Glide image loading integration
- Data model integration
- UI component integration

**Run:** `./gradlew test --tests com.senateway.guesthouse.integration.*`

### 7. API Testing
**Location:** `app/src/test/java/com/senateway/guesthouse/api/`

Tests API integrations:
- Firebase Realtime Database API
- AccuWeather API
- Gemini AI API
- EmailJS API
- Google Maps API
- API error handling
- API response time
- API authentication
- API rate limiting

**Run:** `./gradlew test --tests com.senateway.guesthouse.api.*`

### 8. End-to-End Testing
**Location:** `app/src/androidTest/java/com/senateway/guesthouse/e2e/`

Tests complete user flows from start to finish:
- Complete booking flow
- Complete gallery viewing flow
- Complete room selection flow
- Complete review submission flow
- Complete chatbot interaction flow
- Complete location viewing flow
- Complete navigation flow
- Complete theme toggle flow
- Complete admin login flow
- Complete multi-step user journey

**Run:** `./gradlew connectedAndroidTest --tests com.senateway.guesthouse.e2e.*`

### 9. Regression Testing
**Location:** `app/src/androidTest/java/com/senateway/guesthouse/regression/`

Tests that existing features still work after new changes:
- Gallery image display regression
- Toolbar header text regression
- Room filtering regression
- Booking form regression
- Navigation regression
- Image viewer regression
- API integration regression
- Theme toggle regression
- Data model regression
- UI component regression

**Run:** `./gradlew connectedAndroidTest --tests com.senateway.guesthouse.regression.*`

## Running Tests

### Option 1: Run All Tests (Recommended)

**Windows (PowerShell):**
```powershell
.\run_tests.ps1
```

**Linux/Mac (Bash):**
```bash
chmod +x run_tests.sh
./run_tests.sh
```

### Option 2: Run Tests by Type

**Unit Tests (all):**
```bash
./gradlew test
```

**Instrumented Tests (all):**
```bash
./gradlew connectedAndroidTest
```

**Specific Test Suite:**
```bash
./gradlew test --tests com.senateway.guesthouse.functional.*
```

**Specific Test Class:**
```bash
./gradlew test --tests com.senateway.guesthouse.functional.RoomFilteringTest
```

**Specific Test Method:**
```bash
./gradlew test --tests com.senateway.guesthouse.functional.RoomFilteringTest.testFilterBySize
```

### Option 3: Run Tests from Android Studio

1. Open the project in Android Studio
2. Right-click on test directory or test file
3. Select "Run Tests" or "Debug Tests"

## Test Reports

After running tests, reports are generated in:

- **Unit Tests:** `app/build/reports/tests/test/`
- **Instrumented Tests:** `app/build/reports/androidTests/connected/`

Open `index.html` in a browser to view detailed test reports.

## Prerequisites

### For Unit Tests
- No prerequisites needed
- Can run on any machine

### For Instrumented Tests
- Requires Android device or emulator
- Device must be connected via USB or running emulator
- Check connection: `adb devices`

## Continuous Integration

These tests can be integrated into CI/CD pipelines:

```yaml
# Example GitHub Actions workflow
- name: Run Unit Tests
  run: ./gradlew test

- name: Run Instrumented Tests
  run: ./gradlew connectedAndroidTest
```

## Test Coverage

To generate test coverage reports:

```bash
./gradlew test jacocoTestReport
```

Coverage report: `app/build/reports/jacoco/test/html/index.html`

## Troubleshooting

### Tests Fail to Run
- Ensure Gradle is up to date: `./gradlew --version`
- Clean and rebuild: `./gradlew clean build`
- Sync project in Android Studio

### Instrumented Tests Fail
- Ensure device/emulator is connected: `adb devices`
- Check device API level matches app requirements
- Ensure app is installed on device

### Build Errors
- Check dependencies in `build.gradle`
- Ensure all required libraries are downloaded
- Try: `./gradlew clean build --refresh-dependencies`

## Best Practices

1. **Run tests before committing code**
2. **Fix failing tests before adding new features**
3. **Write tests for new features**
4. **Keep tests up to date with code changes**
5. **Review test reports regularly**

## Additional Resources

- [Android Testing Guide](https://developer.android.com/training/testing)
- [JUnit Documentation](https://junit.org/junit4/)
- [Espresso Testing Guide](https://developer.android.com/training/testing/espresso)


