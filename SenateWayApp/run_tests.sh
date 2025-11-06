#!/bin/bash

# Bash script to run all tests for Senate Way Guesthouse App
# Run this script from the project root directory

echo "========================================"
echo "Senate Way Guesthouse - Test Runner"
echo "========================================"
echo ""

# Function to run tests and display results
run_test_suite() {
    local test_type=$1
    local command=$2
    
    echo "Running $test_type tests..."
    echo "Command: $command"
    echo ""
    
    eval $command
    
    if [ $? -eq 0 ]; then
        echo "$test_type tests PASSED"
    else
        echo "$test_type tests FAILED"
    fi
    
    echo ""
    echo "----------------------------------------"
    echo ""
}

# 1. Unit Tests
echo "1. FUNCTIONAL TESTING"
run_test_suite "Functional" "./gradlew test --tests com.senateway.guesthouse.functional.*"

echo "2. PERFORMANCE TESTING"
run_test_suite "Performance" "./gradlew test --tests com.senateway.guesthouse.performance.*"

echo "3. SECURITY TESTING"
run_test_suite "Security" "./gradlew test --tests com.senateway.guesthouse.security.*"

echo "4. INTEGRATION TESTING"
run_test_suite "Integration" "./gradlew test --tests com.senateway.guesthouse.integration.*"

echo "5. API TESTING"
run_test_suite "API" "./gradlew test --tests com.senateway.guesthouse.api.*"

# 6. All Unit Tests
echo "6. ALL UNIT TESTS"
run_test_suite "All Unit Tests" "./gradlew test"

# 7. Instrumented Tests (requires connected device/emulator)
echo "7. USABILITY TESTING"
echo "Note: Requires connected device or emulator"
run_test_suite "Usability" "./gradlew connectedAndroidTest --tests com.senateway.guesthouse.usability.*"

echo "8. COMPATIBILITY TESTING"
run_test_suite "Compatibility" "./gradlew connectedAndroidTest --tests com.senateway.guesthouse.compatibility.*"

echo "9. END-TO-END TESTING"
run_test_suite "End-to-End" "./gradlew connectedAndroidTest --tests com.senateway.guesthouse.e2e.*"

echo "10. REGRESSION TESTING"
run_test_suite "Regression" "./gradlew connectedAndroidTest --tests com.senateway.guesthouse.regression.*"

# 11. All Instrumented Tests
echo "11. ALL INSTRUMENTED TESTS"
run_test_suite "All Instrumented Tests" "./gradlew connectedAndroidTest"

# 12. Generate Test Report
echo "12. GENERATING TEST REPORTS"
echo "Test reports location:"
echo "  Unit Tests: app/build/reports/tests/test/"
echo "  Instrumented Tests: app/build/reports/androidTests/connected/"
echo ""

echo "========================================"
echo "Test Execution Complete!"
echo "========================================"


