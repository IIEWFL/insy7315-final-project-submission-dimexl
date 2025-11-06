package com.senateway.guesthouse

import org.junit.runner.RunWith
import org.junit.runners.Suite
import com.senateway.guesthouse.usability.UsabilityTestSuite
import com.senateway.guesthouse.compatibility.CompatibilityTestSuite
import com.senateway.guesthouse.e2e.EndToEndTestSuite
import com.senateway.guesthouse.regression.RegressionTestSuite

/**
 * Master Test Runner for Android Instrumented Tests
 * Runs all instrumented test suites
 */
@RunWith(Suite::class)
@Suite.SuiteClasses(
    UsabilityTestSuite::class,
    CompatibilityTestSuite::class,
    EndToEndTestSuite::class,
    RegressionTestSuite::class
)
class AndroidTestRunner


