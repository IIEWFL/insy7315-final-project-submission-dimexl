# Running Tests in Android Studio

Since the Gradle wrapper is not present, here's how to run tests directly in Android Studio:

## Method 1: Run All Tests (Easiest)

1. **Open Android Studio**
2. **Right-click on the `app/src/test` folder** (for unit tests) or `app/src/androidTest` folder (for instrumented tests)
3. Select **"Run 'Tests in 'test''"** or **"Run 'Tests in 'androidTest''"**

## Method 2: Run Specific Test Suites

1. **Navigate to the test file** you want to run (e.g., `FunctionalTestSuite.kt`)
2. **Right-click on the file** or the test class name
3. Select **"Run 'FunctionalTestSuite'"**

## Method 3: Run Individual Tests

1. **Open a test file**
2. **Click the green play icon** next to the test method name
3. Or **right-click on the test method** and select **"Run 'testMethodName()'"**

## Method 4: Using Build Menu

1. **Build > Run Tests** - Runs all unit tests
2. **Build > Run Tests with Coverage** - Runs tests and generates coverage report

## Method 5: Using Gradle Panel

1. **Open the Gradle panel** (usually on the right side)
2. Navigate to **app > Tasks > verification**
3. Double-click:
   - **test** - Run all unit tests
   - **connectedAndroidTest** - Run all instrumented tests

## Test Suites Available

### Unit Tests (can run without device)
- `FunctionalTestSuite` - Functional tests
- `PerformanceTestSuite` - Performance tests
- `SecurityTestSuite` - Security tests
- `IntegrationTestSuite` - Integration tests
- `ApiTestSuite` - API tests

### Instrumented Tests (require device/emulator)
- `UsabilityTestSuite` - Usability tests
- `CompatibilityTestSuite` - Compatibility tests
- `EndToEndTestSuite` - End-to-end tests
- `RegressionTestSuite` - Regression tests

## Viewing Test Results

After running tests:
1. **Bottom panel** will show test results
2. **Green checkmarks** = Passed
3. **Red X** = Failed
4. Click on failed tests to see error details

## Generating Test Reports

1. **Build > Generate Test Report**
2. Reports are saved to:
   - Unit Tests: `app/build/reports/tests/test/index.html`
   - Instrumented Tests: `app/build/reports/androidTests/connected/index.html`

## Quick Commands

- **Run all unit tests:** `Ctrl+Shift+F10` (Windows) or `Cmd+Shift+R` (Mac)
- **Run test under cursor:** `Ctrl+Shift+F10` (Windows) or `Ctrl+Shift+R` (Mac)
- **Debug test:** `Ctrl+Shift+F9` (Windows) or `Cmd+Shift+D` (Mac)

## Troubleshooting

**Tests not showing up?**
- Click **File > Sync Project with Gradle Files**
- Click **Build > Rebuild Project**

**Can't run instrumented tests?**
- Ensure device/emulator is connected
- Check **Tools > Device Manager**
- Verify device appears in **Run** dropdown

**Tests fail to compile?**
- Click **File > Invalidate Caches / Restart**
- Clean project: **Build > Clean Project**
- Rebuild: **Build > Rebuild Project**


