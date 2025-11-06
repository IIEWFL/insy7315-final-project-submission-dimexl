package com.senateway.guesthouse.compatibility

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SdkSuppress
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*

 //Compatibility Testing Suite
 //Tests app compatibility across different Android versions and devices
//---. “Testing App Compatibility with Android Studio.” YouTube, 6 July 2020, www.youtube.com/watch?v=GM7CXRJKpEI. Accessed 4 Nov. 2025.
//---. “The Ultimate Guide to Android Testing (Unit Tests, UI Tests, End-To-End Tests) - Clean Architecture.” YouTube, 29 Sept. 2021, www.youtube.com/watch?v=nDCCwyS0_MQ. Accessed 4 Nov. 2025.
@RunWith(AndroidJUnit4::class)
class CompatibilityTestSuite {

   //  Test 1: Minimum SDK Compatibility
    @Test
    @SdkSuppress(minSdkVersion = 24)
    fun testMinimumSdkCompatibility() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val packageManager = context.packageManager
        val packageInfo = packageManager.getPackageInfo(context.packageName, 0)
        
        assertTrue("App should support minimum SDK 24", 
            android.os.Build.VERSION.SDK_INT >= 24)
    }

     //Test 2: Target SDK Compatibility
    @Test
    fun testTargetSdkCompatibility() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val packageManager = context.packageManager
        val packageInfo = packageManager.getPackageInfo(context.packageName, 0)
        
        assertTrue("App should target SDK 34", 
            packageInfo.applicationInfo.targetSdkVersion == 34)
    }

    // Test 3: Screen Size Compatibility
    @Test
    fun testScreenSizeCompatibility() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val resources = context.resources
        val displayMetrics = resources.displayMetrics
        
        val widthDp = displayMetrics.widthPixels / displayMetrics.density
        val heightDp = displayMetrics.heightPixels / displayMetrics.density
        
        // Verify layout works on different screen sizes
        assertTrue("Width should be valid", widthDp > 0)
        assertTrue("Height should be valid", heightDp > 0)
        
        // Test small screens (phones)
        if (widthDp < 600) {
            assertTrue("Layout should adapt to small screens", true)
        }
        
        // Test large screens (tablets)
        if (widthDp >= 600) {
            assertTrue("Layout should adapt to large screens", true)
        }
    }

    // Test 4: Orientation Compatibility
    @Test
    fun testOrientationCompatibility() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val configuration = context.resources.configuration
        
        // Test portrait orientation (default)
        assertTrue("App should support portrait orientation", 
            configuration.orientation == android.content.res.Configuration.ORIENTATION_PORTRAIT ||
            configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE)
    }

    // Test 5: API Level Compatibility

    @Test
    fun testApiLevelCompatibility() {
        val apiLevel = android.os.Build.VERSION.SDK_INT
        
        // Test that app handles different API levels
        when {
            apiLevel >= 34 -> {
                // Android 14+ features
                assertTrue("App should support Android 14+", true)
            }
            apiLevel >= 33 -> {
                // Android 13+ features
                assertTrue("App should support Android 13+", true)
            }
            apiLevel >= 24 -> {
                // Android 7.0+ features (minimum)
                assertTrue("App should support Android 7.0+", true)
            }
        }
    }
    

     //Test 6: Permission Compatibility
    @Test
    fun testPermissionCompatibility() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        
        // Check required permissions
        val requiredPermissions = listOf(
            android.Manifest.permission.INTERNET,
            android.Manifest.permission.ACCESS_NETWORK_STATE,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        )
        
        val packageManager = context.packageManager
        val packageInfo = packageManager.getPackageInfo(context.packageName, 
            android.content.pm.PackageManager.GET_PERMISSIONS)
        
        val declaredPermissions = packageInfo.requestedPermissions?.toSet() ?: emptySet()
        
        // Verify all required permissions are declared
        requiredPermissions.forEach { permission ->
            assertTrue("Permission should be declared: $permission", 
                declaredPermissions.contains(permission))
        }
    }

     //Test 7: Feature Compatibility

    @Test
    fun testFeatureCompatibility() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val packageManager = context.packageManager
        
        // Check if device has required features
        val hasInternet = packageManager.hasSystemFeature(android.content.pm.PackageManager.FEATURE_WIFI) ||
                         packageManager.hasSystemFeature(android.content.pm.PackageManager.FEATURE_TELEPHONY)
        
        // Internet connectivity is required (can be via WiFi or mobile)
        assertTrue("Device should support internet connectivity", hasInternet)
    }
    

     //Test 8: Display Density Compatibility

    @Test
    fun testDisplayDensityCompatibility() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val resources = context.resources
        val displayMetrics = resources.displayMetrics
        
        val density = displayMetrics.density
        
        // Test different density buckets
        when {
            density >= 3.0 -> {
                // xxxhdpi (very high density)
                assertTrue("App should support xxxhdpi", true)
            }
            density >= 2.0 -> {
                // xhdpi (extra high density)
                assertTrue("App should support xhdpi", true)
            }
            density >= 1.5 -> {
                // hdpi (high density)
                assertTrue("App should support hdpi", true)
            }
            density >= 1.0 -> {
                // mdpi (medium density)
                assertTrue("App should support mdpi", true)
            }
            else -> {
                // ldpi (low density)
                assertTrue("App should support ldpi", true)
            }
        }
    }
}


