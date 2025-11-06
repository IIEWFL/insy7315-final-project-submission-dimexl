# Quick Test Start Guide

## 🚀 Quick Start

### Run All Tests (Easiest Method)

**Windows:**
```powershell
.\run_tests.ps1
```

**Linux/Mac:**
```bash
./run_tests.sh
```

### Run Tests by Category

#### Unit Tests (No Device Needed)
```bash
# All unit tests
./gradlew test

# Functional tests only
./gradlew test --tests com.senateway.guesthouse.functional.*

# Performance tests only
./gradlew test --tests com.senateway.guesthouse.performance.*

# Security tests only
./gradlew test --tests com.senateway.guesthouse.security.*

# Integration tests only
./gradlew test --tests com.senateway.guesthouse.integration.*

# API tests only
./gradlew test --tests com.senateway.guesthouse.api.*
```

#### Instrumented Tests (Requires Device/Emulator)
```bash
# All instrumented tests
./gradlew connectedAndroidTest

# Usability tests
./gradlew connectedAndroidTest --tests com.senateway.guesthouse.usability.*

# Compatibility tests
./gradlew connectedAndroidTest --tests com.senateway.guesthouse.compatibility.*

# End-to-end tests
./gradlew connectedAndroidTest --tests com.senateway.guesthouse.e2e.*

# Regression tests
./gradlew connectedAndroidTest --tests com.senateway.guesthouse.regression.*
```

## 📊 Test Types Summary

| Test Type | Location | Device Required | Command |
|-----------|----------|----------------|---------|
| **Functional** | `test/functional/` | ❌ No | `./gradlew test --tests *.functional.*` |
| **Performance** | `test/performance/` | ❌ No | `./gradlew test --tests *.performance.*` |
| **Security** | `test/security/` | ❌ No | `./gradlew test --tests *.security.*` |
| **Integration** | `test/integration/` | ❌ No | `./gradlew test --tests *.integration.*` |
| **API** | `test/api/` | ❌ No | `./gradlew test --tests *.api.*` |
| **Usability** | `androidTest/usability/` | ✅ Yes | `./gradlew connectedAndroidTest --tests *.usability.*` |
| **Compatibility** | `androidTest/compatibility/` | ✅ Yes | `./gradlew connectedAndroidTest --tests *.compatibility.*` |
| **End-to-End** | `androidTest/e2e/` | ✅ Yes | `./gradlew connectedAndroidTest --tests *.e2e.*` |
| **Regression** | `androidTest/regression/` | ✅ Yes | `./gradlew connectedAndroidTest --tests *.regression.*` |

## 🔍 View Test Results

After running tests, view reports:
- **Unit Tests:** `app/build/reports/tests/test/index.html`
- **Instrumented Tests:** `app/build/reports/androidTests/connected/index.html`

Open these HTML files in your browser for detailed results.

## ⚠️ Prerequisites

**For Unit Tests:**
- None required - runs on any machine

**For Instrumented Tests:**
- Connected Android device OR running emulator
- Check connection: `adb devices`

## 🛠️ Troubleshooting

**Tests won't run?**
```bash
./gradlew clean build
```

**Device not detected?**
```bash
adb devices
adb kill-server
adb start-server
```

**Need help?**
See `TESTING_GUIDE.md` for detailed documentation.


