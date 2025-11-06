# PowerShell script to run all tests for Senate Way Guesthouse App
# Run this script from the project root directory

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Senate Way Guesthouse - Test Runner" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Function to run tests and display results
function Run-TestSuite {
    param(
        [string]$TestType,
        [string]$Command
    )
    
    Write-Host "Running $TestType tests..." -ForegroundColor Yellow
    Write-Host "Command: $Command" -ForegroundColor Gray
    Write-Host ""
    
    Invoke-Expression $Command
    
    if ($LASTEXITCODE -eq 0) {
        Write-Host "$TestType tests PASSED" -ForegroundColor Green
    } else {
        Write-Host "$TestType tests FAILED" -ForegroundColor Red
    }
    
    Write-Host ""
    Write-Host "----------------------------------------" -ForegroundColor Gray
    Write-Host ""
}

# Check for Gradle wrapper or system Gradle
$gradleCommand = $null

if (Test-Path ".\gradlew.bat") {
    $gradleCommand = ".\gradlew.bat"
} elseif (Test-Path ".\gradlew") {
    $gradleCommand = ".\gradlew"
} elseif (Get-Command gradle -ErrorAction SilentlyContinue) {
    $gradleCommand = "gradle"
} else {
    Write-Host "ERROR: Gradle not found!" -ForegroundColor Red
    Write-Host "Please either:" -ForegroundColor Yellow
    Write-Host "  1. Run this from Android Studio (Build > Run Tests)" -ForegroundColor Yellow
    Write-Host "  2. Generate Gradle wrapper: gradle wrapper" -ForegroundColor Yellow
    Write-Host "  3. Install Gradle and add to PATH" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "For now, you can run tests manually in Android Studio:" -ForegroundColor Cyan
    Write-Host "  - Right-click on test folder > Run Tests" -ForegroundColor Gray
    Write-Host "  - Or use: Build > Run Tests" -ForegroundColor Gray
    exit 1
}

Write-Host "Using Gradle: $gradleCommand" -ForegroundColor Green
Write-Host ""

# 1. Unit Tests
Write-Host "1. FUNCTIONAL TESTING" -ForegroundColor Cyan
Run-TestSuite "Functional" "$gradleCommand test --tests com.senateway.guesthouse.functional.*"

Write-Host "2. PERFORMANCE TESTING" -ForegroundColor Cyan
Run-TestSuite "Performance" "$gradleCommand test --tests com.senateway.guesthouse.performance.*"

Write-Host "3. SECURITY TESTING" -ForegroundColor Cyan
Run-TestSuite "Security" "$gradleCommand test --tests com.senateway.guesthouse.security.*"

Write-Host "4. INTEGRATION TESTING" -ForegroundColor Cyan
Run-TestSuite "Integration" "$gradleCommand test --tests com.senateway.guesthouse.integration.*"

Write-Host "5. API TESTING" -ForegroundColor Cyan
Run-TestSuite "API" "$gradleCommand test --tests com.senateway.guesthouse.api.*"

# 6. All Unit Tests
Write-Host "6. ALL UNIT TESTS" -ForegroundColor Cyan
Run-TestSuite "All Unit Tests" "$gradleCommand test"

# 7. Instrumented Tests (requires connected device/emulator)
Write-Host "7. USABILITY TESTING" -ForegroundColor Cyan
Write-Host "Note: Requires connected device or emulator" -ForegroundColor Yellow
Run-TestSuite "Usability" "$gradleCommand connectedAndroidTest --tests com.senateway.guesthouse.usability.*"

Write-Host "8. COMPATIBILITY TESTING" -ForegroundColor Cyan
Run-TestSuite "Compatibility" "$gradleCommand connectedAndroidTest --tests com.senateway.guesthouse.compatibility.*"

Write-Host "9. END-TO-END TESTING" -ForegroundColor Cyan
Run-TestSuite "End-to-End" "$gradleCommand connectedAndroidTest --tests com.senateway.guesthouse.e2e.*"

Write-Host "10. REGRESSION TESTING" -ForegroundColor Cyan
Run-TestSuite "Regression" "$gradleCommand connectedAndroidTest --tests com.senateway.guesthouse.regression.*"

# 11. All Instrumented Tests
Write-Host "11. ALL INSTRUMENTED TESTS" -ForegroundColor Cyan
Run-TestSuite "All Instrumented Tests" "$gradleCommand connectedAndroidTest"

# 12. Generate Test Report
Write-Host "12. GENERATING TEST REPORTS" -ForegroundColor Cyan
Write-Host "Test reports location:" -ForegroundColor Yellow
Write-Host "  Unit Tests: app/build/reports/tests/test/" -ForegroundColor Gray
Write-Host "  Instrumented Tests: app/build/reports/androidTests/connected/" -ForegroundColor Gray
Write-Host ""

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Test Execution Complete!" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan

