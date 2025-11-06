package com.senateway.guesthouse

import org.junit.runner.RunWith
import org.junit.runners.Suite
import com.senateway.guesthouse.functional.FunctionalTestSuite
import com.senateway.guesthouse.performance.PerformanceTestSuite
import com.senateway.guesthouse.security.SecurityTestSuite
import com.senateway.guesthouse.integration.IntegrationTestSuite
import com.senateway.guesthouse.api.ApiTestSuite

/**
 * Master Test Runner for Unit Tests
 * Runs all unit test suites
 */
@RunWith(Suite::class)
@Suite.SuiteClasses(
    FunctionalTestSuite::class,
    PerformanceTestSuite::class,
    SecurityTestSuite::class,
    IntegrationTestSuite::class,
    ApiTestSuite::class
)
class TestRunner


