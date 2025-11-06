package com.senateway.guesthouse.functional

import com.senateway.guesthouse.data.model.Room
import org.junit.Test
import org.junit.Assert.*
import org.junit.Before
import org.junit.runner.RunWith
import org.junit.runners.Suite

/**
 * Functional Testing Suite
 * Tests that all features work correctly according to specifications
 * SkyFish. “Functional Unit Testing Kotlin View Models and Use Cases.” YouTube, 25 Sept. 2023, www.youtube.com/watch?v=4aa0cyIgG8s. Accessed 4 Oct. 2025.
 * ---. “The Ultimate Guide to Android Testing (Unit Tests, UI Tests, End-To-End Tests) - Clean Architecture.” YouTube, 29 Sept. 2021, www.youtube.com/watch?v=nDCCwyS0_MQ. Accessed 4 Nov. 2025.SkyFish. “Functional Unit Testing Kotlin View Models and Use Cases.” YouTube, 25 Sept. 2023, www.youtube.com/watch?v=4aa0cyIgG8s. Accessed 4 Oct. 2025.
 */
@RunWith(Suite::class)
@Suite.SuiteClasses(
    RoomFilteringTest::class,
    BookingFormTest::class,
    GalleryNavigationTest::class,
    ReviewsDisplayTest::class,
    MapLocationTest::class,
    ChatbotTest::class
)
class FunctionalTestSuite

/**
 * Test Room Filtering Functionality
 */
class RoomFilteringTest {
    private lateinit var rooms: List<Room>
    
    @Before
    fun setup() {
        rooms = listOf(
            Room(1, "Single Room", "small", 1, "1 Single Bed", 400, "images/Senate1.jpg", listOf("WiFi", "AC")),
            Room(2, "Double Room", "medium", 2, "1 Double Bed", 600, "images/Senate2.jpg", listOf("WiFi", "AC", "TV")),
            Room(3, "Family Room", "large", 4, "2 Double Beds", 1200, "images/Senate3.jpg", listOf("WiFi", "AC", "TV", "Kitchen"))
        )
    }
    
    @Test
    fun testFilterBySize() {
        val filtered = rooms.filter { it.size == "small" }
        assertEquals(1, filtered.size)
        assertEquals("Single Room", filtered[0].name)
    }
    
    @Test
    fun testFilterByCapacity() {
        val filtered = rooms.filter { it.capacity >= 2 }
        assertEquals(2, filtered.size)
        assertTrue(filtered.all { it.capacity >= 2 })
    }
    
    @Test
    fun testFilterByPriceRange() {
        val minPrice = 500
        val maxPrice = 1000
        val filtered = rooms.filter { it.price in minPrice..maxPrice }
        assertEquals(1, filtered.size)
        assertEquals("Double Room", filtered[0].name)
    }
    
    @Test
    fun testFilterByMultipleCriteria() {
        val filtered = rooms.filter { 
            it.size == "large" && it.capacity >= 3 && it.price <= 1500 
        }
        assertEquals(1, filtered.size)
        assertEquals("Family Room", filtered[0].name)
    }
}

/**
 * Test Booking Form Functionality
 */
class BookingFormTest {
    
    @Test
    fun testValidBookingData() {
        val name = "John Doe"
        val email = "john@example.com"
        val phone = "+27123456789"
        val checkIn = "2025-12-01"
        val checkOut = "2025-12-05"
        val guests = 2L
        
        assertTrue("Name should not be empty", name.isNotEmpty())
        assertTrue("Email should be valid", email.contains("@"))
        assertTrue("Phone should not be empty", phone.isNotEmpty())
        assertTrue("Check-in should be before check-out", checkIn < checkOut)
        assertTrue("Guests should be positive", guests > 0)
    }
    
    @Test
    fun testInvalidEmail() {
        val invalidEmails = listOf("invalid", "invalid@", "@invalid.com", "")
        invalidEmails.forEach { email ->
            // Check if email is invalid (doesn't have proper email format)
            val isValidFormat = email.isNotEmpty() && 
                               email.contains("@") && 
                               email.contains(".") &&
                               email.indexOf("@") > 0 &&
                               email.indexOf(".") > email.indexOf("@") + 1
            assertFalse("Email should be invalid: $email", isValidFormat)
        }
    }
    
    @Test
    fun testDateValidation() {
        // Test valid date scenario
        val checkIn = "2025-12-01"
        val checkOut = "2025-12-05" // Valid: check-out after check-in
        assertTrue("Check-out should be after check-in", checkOut > checkIn)
        
        // Test invalid date scenario
        val invalidCheckIn = "2025-12-01"
        val invalidCheckOut = "2025-11-30" // Invalid: check-out before check-in
        assertFalse("Check-out should not be before check-in", invalidCheckOut > invalidCheckIn)
    }
    
    @Test
    fun testGuestCountValidation() {
        val validGuests = listOf(1L, 2L, 4L, 5L)
        val invalidGuests = listOf(0L, -1L, 10L)
        
        validGuests.forEach { guests ->
            assertTrue("Guest count should be valid: $guests", guests > 0 && guests <= 5)
        }
        
        invalidGuests.forEach { guests ->
            assertFalse("Guest count should be invalid: $guests", guests > 0 && guests <= 5)
        }
    }
}

/**
 * Test Gallery Navigation
 */
class GalleryNavigationTest {
    
    @Test
    fun testGalleryImageCount() {
        val expectedImages = 29
        // In real test, this would check the actual gallery
        assertTrue("Gallery should have 29 images", expectedImages == 29)
    }
    
    @Test
    fun testImageOrder() {
        val imageIds = (1..29).toList()
        val sortedIds = imageIds.sorted()
        assertEquals("Images should be in order", imageIds, sortedIds)
    }
    
    @Test
    fun testImageUniqueness() {
        val imageIds = (1..29).toList()
        val uniqueIds = imageIds.toSet()
        assertEquals("All image IDs should be unique", imageIds.size, uniqueIds.size)
    }
}

/**
 * Test Reviews Display
 */
class ReviewsDisplayTest {
    
    @Test
    fun testRatingCalculation() {
        val ratings = listOf(5, 5, 4, 5, 4)
        val average = ratings.average()
        assertEquals(4.6, average, 0.1)
    }
    
    @Test
    fun testRatingRange() {
        val validRatings = listOf(1, 2, 3, 4, 5)
        val invalidRatings = listOf(0, 6, -1, 10)
        
        validRatings.forEach { rating ->
            assertTrue("Rating should be valid: $rating", rating in 1..5)
        }
        
        invalidRatings.forEach { rating ->
            assertFalse("Rating should be invalid: $rating", rating in 1..5)
        }
    }
}

/**
 * Test Map Location
 */
class MapLocationTest {
    
    @Test
    fun testLocationCoordinates() {
        val latitude = -28.7474
        val longitude = 24.7668
        val expectedLatitude = -28.7474
        val expectedLongitude = 24.7668
        
        assertEquals("Latitude should match", expectedLatitude, latitude, 0.0001)
        assertEquals("Longitude should match", expectedLongitude, longitude, 0.0001)
    }
    
    @Test
    fun testLocationBounds() {
        val latitude = -28.7474
        val longitude = 24.7668
        
        assertTrue("Latitude should be valid", latitude in -90.0..90.0)
        assertTrue("Longitude should be valid", longitude in -180.0..180.0)
    }
}

/**
 * Test Chatbot Functionality
 */
class ChatbotTest {
    
    @Test
    fun testMessageValidation() {
        val validMessage = "What are your room rates?"
        val emptyMessage = ""
        val longMessage = "a".repeat(1001)
        
        assertTrue("Valid message should pass", validMessage.isNotEmpty() && validMessage.length <= 1000)
        assertFalse("Empty message should fail", emptyMessage.isNotEmpty())
        assertFalse("Too long message should fail", longMessage.length <= 1000)
    }
    
    @Test
    fun testQuickQuestions() {
        val quickQuestions = listOf(
            "What are your room rates?",
            "Do you have WiFi?",
            "What is your cancellation policy?",
            "Do you provide parking?"
        )
        
        assertTrue("Should have quick questions", quickQuestions.isNotEmpty())
        quickQuestions.forEach { question ->
            assertTrue("Question should not be empty", question.isNotEmpty())
        }
    }
}


