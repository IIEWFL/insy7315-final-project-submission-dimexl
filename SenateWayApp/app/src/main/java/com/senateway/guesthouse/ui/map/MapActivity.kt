package com.senateway.guesthouse.ui.map

import android.app.VoiceInteractor
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.bumptech.glide.Glide
import coil.Coil
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.senateway.guesthouse.R
import com.senateway.guesthouse.data.model.WeatherData
import com.senateway.guesthouse.databinding.ActivityMapBinding
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class MapActivity : AppCompatActivity(), OnMapReadyCallback {
    
    private lateinit var binding: ActivityMapBinding
    private lateinit var mMap: GoogleMap

    // OkHttp client for making weather API requests
    //localhost. “Create a REST API Client for Android Using Okhttp.” Stack Overflow, 18 Feb. 2018, stackoverflow.com/questions/48855662/create-a-rest-api-client-for-android-using-okhttp.
    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .build()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Our Location"
        
        // Initialize map fragment with Google maps
        initializeMap()
        // Set up direction button and load weather info
        setupNearbyAttractions()
        fetchWeather()
    }
    
    private fun initializeMap() {
        // Check if Google Play Services is available
        val googleApiAvailability = GoogleApiAvailability.getInstance()
        val resultCode = googleApiAvailability.isGooglePlayServicesAvailable(this)
        
        if (resultCode != ConnectionResult.SUCCESS) {
            android.util.Log.e("MapActivity", "Google Play Services not available: $resultCode")
            if (googleApiAvailability.isUserResolvableError(resultCode)) {
                googleApiAvailability.getErrorDialog(this, resultCode, 9001)?.show()
            } else {
                // If not available, show error message or prompt user, useful when testing
                showMapError("Google Play Services is required for maps but is not available on this device.")
            }
            return
        }
        
        try {
            val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as? SupportMapFragment
            
            if (mapFragment == null) {
                // If fragment not found, create it programmatically
                //Zapnologica. “How to Add a Fragment to a Programmatically Generated Layout?” Stack Overflow, 18 Aug. 2013, stackoverflow.com/questions/18296868/how-to-add-a-fragment-to-a-programmatically-generated-layout.
                val newMapFragment = SupportMapFragment.newInstance()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.mapContainer, newMapFragment)
                    .commit()
                newMapFragment.getMapAsync(this)
                android.util.Log.d("MapActivity", "Map fragment created programmatically")
            } else {
                mapFragment.getMapAsync(this)
                android.util.Log.d("MapActivity", "Map fragment found and initialized")
            }
        } catch (e: Exception) {
            android.util.Log.e("MapActivity", "Error initializing map: ${e.message}", e)
            showMapError("Failed to initialize map: ${e.message}")
        }
    }
    
    private fun showMapError(message: String) {
        // Create a TextView to show error message in the map container, also useful for testing
        val errorView = TextView(this).apply {
            text = "Unable to load map.\n\n$message\n\nPlease check your internet connection and Google Play Services."
            textSize = 14f
            setPadding(32, 32, 32, 32)
            gravity = android.view.Gravity.CENTER
        }
        binding.root.findViewById<View>(R.id.mapContainer)?.let { container ->
            if (container is android.widget.FrameLayout) {
                container.removeAllViews()
                container.addView(errorView)
            }
        }
    }
    
    override fun onMapReady(googleMap: GoogleMap) {
        try {
            mMap = googleMap
            
            // Senate Way Guesthouse location: -28.7674381, 24.7497489
            //https://www.gps-coordinates.net/
            val location = LatLng(-28.7674381, 24.7497489)
            
            // Add marker
            mMap.addMarker(
                MarkerOptions()
                    .position(location)
                    .title("Senate Way Guesthouse")
                    .snippet("10 Senate Way, Kimberley, Northern Cape, South Africa")
            )
            
            // Move camera to location with zoom
            //Lee. “MoveCamera with CameraUpdateFactory.newLatLngBounds Crashes.” Stack Overflow, 3 Dec. 2012, stackoverflow.com/questions/13692579/movecamera-with-cameraupdatefactory-newlatlngbounds-crashes.
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
            
            // Enable zoom controls and map settings
            //“UiSettings  |  Google Play Services  |  Google for Developers.” Google for Developers, 2025, developers.google.com/android/reference/com/google/android/gms/maps/UiSettings. Accessed 4 Nov. 2025.
            mMap.uiSettings.isZoomControlsEnabled = true
            mMap.uiSettings.isMapToolbarEnabled = true
            mMap.uiSettings.isZoomGesturesEnabled = true
            mMap.uiSettings.isScrollGesturesEnabled = true
            mMap.uiSettings.isTiltGesturesEnabled = true
            mMap.uiSettings.isRotateGesturesEnabled = true

            // Disable location if no permissions are grandted
            try {
                mMap.isMyLocationEnabled = false
            } catch (e: SecurityException) {
                android.util.Log.e("MapActivity", "Location permission not granted", e)
            }
            
            android.util.Log.d("MapActivity", "Map ready and location set: $location")
        } catch (e: Exception) {
            android.util.Log.e("MapActivity", "Error in onMapReady: ${e.message}", e)
        }
    }
    // Opens navigation to the guesthouse in Google Maps
    private fun setupNearbyAttractions() {
        binding.buttonGetDirections.setOnClickListener {
            val gmmIntentUri = Uri.parse("google.navigation:q=-28.7674381,24.7497489")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            if (mapIntent.resolveActivity(packageManager) != null) {
                startActivity(mapIntent)
            } else {
                // Fallback to web browser
                val webUri = Uri.parse("https://www.google.com/maps/place/Senate+Way+Guest+House/@-28.7674381,24.747174,17z")
                startActivity(Intent(Intent.ACTION_VIEW, webUri))
            }
        }
    }
    // Fetches current weather info for Kimberley using AccuWeather API
    private fun fetchWeather() {
        showWeatherLoading()
        
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val apiKey = getAccuWeatherApiKey()
                if (apiKey.isEmpty() || apiKey == "YOUR_ACCUWEATHER_API_KEY") {
                    withContext(Dispatchers.Main) {
                        showWeatherError("Weather service not configured. Please add AccuWeather API key.")
                    }
                    return@launch
                }
                
                // Get location key for Kimberley
                val locationUrl = "https://dataservice.accuweather.com/locations/v1/cities/search?apikey=$apiKey&q=Kimberley&country=ZA"
                val locationRequest = Request.Builder().url(locationUrl).build()
                val locationResponse = client.newCall(locationRequest).execute()
                
                if (!locationResponse.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        showWeatherError("Failed to fetch location data")
                    }
                    return@launch
                }
                
                val locationBody = locationResponse.body
                val locationData = if (locationBody != null) {
                    JSONArray(locationBody.string())
                } else {
                    JSONArray("[]")
                }
                if (locationData.length() == 0) {
                    withContext(Dispatchers.Main) {
                        showWeatherError("Location not found")
                    }
                    return@launch
                }
                
                val locationKey = locationData.getJSONObject(0).getString("Key")
                
                // Get current weather conditions
                val weatherUrl = "https://dataservice.accuweather.com/currentconditions/v1/$locationKey?apikey=$apiKey&details=true"
                val weatherRequest = Request.Builder().url(weatherUrl).build()
                val weatherResponse = client.newCall(weatherRequest).execute()
                
                if (!weatherResponse.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        showWeatherError("Failed to fetch weather data")
                    }
                    return@launch
                }
                
                val weatherBody = weatherResponse.body
                val weatherDataArray = if (weatherBody != null) {
                    JSONArray(weatherBody.string())
                } else {
                    JSONArray("[]")
                }
                if (weatherDataArray.length() == 0) {
                    withContext(Dispatchers.Main) {
                        showWeatherError("Weather data not available")
                    }
                    return@launch
                }
                // Display weather info on main thread
                //“How to Check If Current Thread Is Not Main Thread.” Stack Overflow, stackoverflow.com/questions/11411022/how-to-check-if-current-thread-is-not-main-thread.
                val weatherJson = weatherDataArray.getJSONObject(0)
                val weatherData = parseWeatherData(weatherJson)
                
                withContext(Dispatchers.Main) {
                    displayWeather(weatherData)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    showWeatherError("Unable to load weather data: ${e.message}")
                }
            }
        }
    }
    // Parses raw JSON response into WeatherData model
    //Gharsa Khouloud. “How Can I Get Json Response for Weather Response.” Stack Overflow, 1 June 2019, stackoverflow.com/questions/56406286/how-can-i-get-json-response-for-weather-response.
    private fun parseWeatherData(json: JSONObject): WeatherData {
        fun parseTemperatureUnit(obj: JSONObject?): com.senateway.guesthouse.data.model.TemperatureUnit? {
            return obj?.let {
                val metric = it.getJSONObject("Metric")
                com.senateway.guesthouse.data.model.TemperatureUnit(
                    com.senateway.guesthouse.data.model.MetricValue(
                        metric.getDouble("Value"),
                        metric.getString("Unit")
                    )
                )
            }
        }
        
        fun parseMetricValue(obj: JSONObject?): com.senateway.guesthouse.data.model.MetricValue? {
            return obj?.let {
                com.senateway.guesthouse.data.model.MetricValue(
                    it.getDouble("Value"),
                    it.getString("Unit")
                )
            }
        }
        // Wind data
        fun parseWindData(obj: JSONObject?): com.senateway.guesthouse.data.model.WindData? {
            return obj?.let {
                val speed = it.getJSONObject("Speed").getJSONObject("Metric")
                val direction = it.getJSONObject("Direction")
                com.senateway.guesthouse.data.model.WindData(
                    com.senateway.guesthouse.data.model.MetricValue(
                        speed.getDouble("Value"),
                        speed.getString("Unit")
                    ),
                    com.senateway.guesthouse.data.model.WindDirection(
                        direction.getInt("Degrees"),
                        direction.getString("Localized"),
                        direction.getString("English")
                    )
                )
            }
        }
        
        fun parseWindGustData(obj: JSONObject?): com.senateway.guesthouse.data.model.WindGustData? {
            return obj?.let {
                val speed = it.getJSONObject("Speed").getJSONObject("Metric")
                com.senateway.guesthouse.data.model.WindGustData(
                    com.senateway.guesthouse.data.model.MetricValue(
                        speed.getDouble("Value"),
                        speed.getString("Unit")
                    )
                )
            }
        }
        
        fun parsePressureUnit(obj: JSONObject?): com.senateway.guesthouse.data.model.PressureUnit? {
            return obj?.let {
                val metric = it.getJSONObject("Metric")
                com.senateway.guesthouse.data.model.PressureUnit(
                    com.senateway.guesthouse.data.model.MetricValue(
                        metric.getDouble("Value"),
                        metric.getString("Unit")
                    )
                )
            }
        }
        
        fun parseVisibilityUnit(obj: JSONObject?): com.senateway.guesthouse.data.model.VisibilityUnit? {
            return obj?.let {
                val metric = it.getJSONObject("Metric")
                com.senateway.guesthouse.data.model.VisibilityUnit(
                    com.senateway.guesthouse.data.model.MetricValue(
                        metric.getDouble("Value"),
                        metric.getString("Unit")
                    )
                )
            }
        }
        // Combine all metrics into a WeatherData object
        return WeatherData(
            Temperature = parseTemperatureUnit(json.optJSONObject("Temperature")),
            WeatherText = json.optString("WeatherText", null),
            WeatherIcon = if (json.has("WeatherIcon")) json.getInt("WeatherIcon") else null,
            RealFeelTemperature = parseTemperatureUnit(json.optJSONObject("RealFeelTemperature")),
            RelativeHumidity = if (json.has("RelativeHumidity")) json.getInt("RelativeHumidity") else null,
            Wind = parseWindData(json.optJSONObject("Wind")),
            WindGust = parseWindGustData(json.optJSONObject("WindGust")),
            Pressure = parsePressureUnit(json.optJSONObject("Pressure")),
            Visibility = parseVisibilityUnit(json.optJSONObject("Visibility")),
            UVIndex = if (json.has("UVIndex")) json.getInt("UVIndex") else null,
            CloudCover = if (json.has("CloudCover")) json.getInt("CloudCover") else null,
            DewPoint = parseTemperatureUnit(json.optJSONObject("DewPoint"))
        )
    }
    // Displays parsed weather data on UI
    //Gharsa Khouloud. “How Can I Get Json Response for Weather Response.” Stack Overflow, 1 June 2019, stackoverflow.com/questions/56406286/how-can-i-get-json-response-for-weather-response.
    private fun displayWeather(weather: WeatherData) {
        binding.cardWeatherLoading.visibility = View.GONE
        binding.cardWeatherError.visibility = View.GONE
        binding.cardWeather.visibility = View.VISIBLE
        
        // Temperature
        val temp = weather.Temperature?.Metric?.Value?.toInt() ?: 0
        val tempUnit = weather.Temperature?.Metric?.Unit ?: "C"
        binding.textTemperature.text = "${temp}°$tempUnit"
        
        // Weather description
        binding.textWeatherDescription.text = weather.WeatherText?.replaceFirstChar { 
            if (it.isLowerCase()) it.titlecase() else it.toString() 
        } ?: ""
        
        // Feels like
        val feelsLike = weather.RealFeelTemperature?.Metric?.Value?.toInt() 
            ?: weather.Temperature?.Metric?.Value?.toInt() ?: 0
        val feelsLikeUnit = weather.RealFeelTemperature?.Metric?.Unit 
            ?: weather.Temperature?.Metric?.Unit ?: "C"
        binding.textFeelsLike.text = "${feelsLike}°$feelsLikeUnit"
        
        // Wind speed
        val windSpeed = weather.Wind?.Speed?.Value?.toInt() ?: 0
        val windUnit = weather.Wind?.Speed?.Unit ?: ""
        val windDirection = weather.Wind?.Direction?.Localized ?: ""
        binding.textWindSpeed.text = "$windSpeed $windUnit $windDirection"
        
        // Humidity
        binding.textHumidity.text = "${weather.RelativeHumidity ?: 0}%"
        
        // Pressure
        val pressure = weather.Pressure?.Metric?.Value?.toInt() ?: 0
        val pressureUnit = weather.Pressure?.Metric?.Unit ?: ""
        binding.textPressure.text = "$pressure $pressureUnit"
        
        // UV Index
        if (weather.UVIndex != null) {
            binding.cardUVIndex.visibility = View.VISIBLE
            val uvLevel = getUVLevel(weather.UVIndex)
            binding.textUVIndex.text = "${weather.UVIndex}\n($uvLevel)"
        } else {
            binding.cardUVIndex.visibility = View.GONE
        }
        
        // Visibility
        if (weather.Visibility != null) {
            binding.cardVisibility.visibility = View.VISIBLE
            val visibility = weather.Visibility.Metric.Value.toInt()
            val visibilityUnit = weather.Visibility.Metric.Unit
            binding.textVisibility.text = "$visibility $visibilityUnit"
        } else {
            binding.cardVisibility.visibility = View.GONE
        }
        
        // Wind Gust
        if (weather.WindGust != null) {
            binding.cardWindGust.visibility = View.VISIBLE
            val gustSpeed = weather.WindGust.Speed.Value.toInt()
            val gustUnit = weather.WindGust.Speed.Unit
            binding.textWindGust.text = "$gustSpeed $gustUnit"
        } else {
            binding.cardWindGust.visibility = View.GONE
        }
        
        // Dew Point
        if (weather.DewPoint != null) {
            binding.cardDewPoint.visibility = View.VISIBLE
            val dewPoint = weather.DewPoint.Metric.Value.toInt()
            val dewPointUnit = weather.DewPoint.Metric.Unit
            binding.textDewPoint.text = "${dewPoint}°$dewPointUnit"
        } else {
            binding.cardDewPoint.visibility = View.GONE
        }
        
        // Cloud Cover
        if (weather.CloudCover != null) {
            binding.cardCloudCover.visibility = View.VISIBLE
            binding.textCloudCover.text = "${weather.CloudCover}%"
        } else {
            binding.cardCloudCover.visibility = View.GONE
        }
        
        // Weather icon which will load AccuWeather icon
        val iconCode = weather.WeatherIcon ?: 1
        android.util.Log.d("MapActivity", "Weather icon code from API: ${weather.WeatherIcon}, using: $iconCode")
        loadAccuWeatherIcon(iconCode)
    }
    // Loads weather icons with Coil
    //Hameed, Abdullah. “Coil Didn’t Loading Network Images in Jetpack Compose | Solution.” Stack Overflow, 28 Jan. 2025, stackoverflow.com/questions/79393551/coil-didnt-loading-network-images-in-jetpack-compose-solution.
    private fun loadAccuWeatherIcon(iconCode: Int) {
        // AccuWeather SVG icon URL format from official documentation
        val iconUrl = "https://www.accuweather.com/assets/images/weather-icons/v2a/$iconCode.svg"
        
        android.util.Log.d("MapActivity", "Loading weather icon - Code: $iconCode, URL: $iconUrl")
        
        // Load SVG icon using Coil with SVG decoder
        val imageLoader = Coil.imageLoader(this)
        val request = ImageRequest.Builder(this)
            .data(iconUrl)
            .target(binding.imageWeatherIcon)
            .placeholder(android.R.drawable.ic_menu_recent_history)
            .error(android.R.drawable.ic_menu_recent_history)
            .listener(
                onStart = { 
                    android.util.Log.d("MapActivity", "Started loading weather icon from: $iconUrl")
                },
                onSuccess = { _, _ ->
                    android.util.Log.d("MapActivity", "Successfully loaded AccuWeather SVG icon from: $iconUrl")
                },
                onError = { _, result ->
                    android.util.Log.w("MapActivity", "Failed to load AccuWeather SVG icon, using fallback. Error: ${result.throwable.message}")
                    // Use fallback icon component based on weather code
                    setWeatherIconFallback(iconCode)
                }
            )
            .build()
        
        imageLoader.enqueue(request)
    }
    
    private fun setWeatherIconFallback(iconCode: Int) {
        // Helper function to check if it's currently night time (7pm - 7am)
        val isNight = isNightTime()
        
        // Get the emoji icon based on weather code
        val iconEmoji = getWeatherIconEmoji(iconCode, isNight)
        
        // Create a bitmap from the emoji text and display it in the ImageView
        //Ravindra Kushwaha. “How to Use the Image(Stored Image of Device) with Text on TextView Android?” Stack Overflow, 2 Aug. 2017, stackoverflow.com/questions/45453202/how-to-use-the-imagestored-image-of-device-with-text-on-textview-android.
        val bitmap = createBitmapFromEmoji(iconEmoji, 128, 128)
        binding.imageWeatherIcon.setImageBitmap(bitmap)
        binding.imageWeatherIcon.contentDescription = "Weather icon: $iconEmoji"
    }
    
    private fun createBitmapFromEmoji(emoji: String, width: Int, height: Int): android.graphics.Bitmap {
        val bitmap = android.graphics.Bitmap.createBitmap(width, height, android.graphics.Bitmap.Config.ARGB_8888)
        val canvas = android.graphics.Canvas(bitmap)
        val paint = android.graphics.Paint().apply {
            textSize = (width * 0.8f)
            textAlign = android.graphics.Paint.Align.CENTER
            isAntiAlias = true
        }
        
        // Center the emoji
        //Danedo. “Measuring Text Height to Be Drawn on Canvas ( Android ).” Stack Overflow, 6 Sept. 2010, stackoverflow.com/questions/3654321/measuring-text-height-to-be-drawn-on-canvas-android.
        val x = width / 2f
        val y = height / 2f - (paint.descent() + paint.ascent()) / 2f
        
        canvas.drawText(emoji, x, y, paint)
        return bitmap
    }
    
    private fun isNightTime(): Boolean {
        val hour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
        return hour >= 19 || hour < 7
    }
    
    private fun getWeatherIconEmoji(iconCode: Int, isNight: Boolean): String {
        return when {
            // Clear/Sunny - Day codes: 1, 2 | Night codes: 33, 34
            iconCode in listOf(1, 2, 33, 34) -> {
                if (isNight || iconCode >= 33) "🌙" else "☀️"
            }
            //codes are accuweather icon codes
            // Partly cloudy - Day codes: 3, 4, 6 | Night codes: 35, 36, 38
            iconCode in listOf(3, 4, 6, 35, 36, 38) -> "⛅"
            // Cloudy (code 7, 8)
            iconCode in listOf(7, 8) -> "☁️"
            // Rain (codes 12, 13, 14, 18, 26, 39, 40)
            iconCode in listOf(12, 13, 14, 18, 26, 39, 40) -> "🌧️"
            // Thunderstorm (codes 15, 16, 17, 41, 42)
            iconCode in listOf(15, 16, 17, 41, 42) -> "⛈️"
            // Snow (codes 19, 20, 21, 22, 23, 24, 25, 29, 30, 31)
            iconCode in listOf(19, 20, 21, 22, 23, 24, 25, 29, 30, 31) -> "❄️"
            // Default
            else -> "☁️"
        }
    }
    
    private fun showWeatherLoading() {
        binding.cardWeather.visibility = View.GONE
        binding.cardWeatherError.visibility = View.GONE
        binding.cardWeatherLoading.visibility = View.VISIBLE
    }
    
    private fun showWeatherError(message: String) {
        binding.cardWeather.visibility = View.GONE
        binding.cardWeatherLoading.visibility = View.GONE
        binding.cardWeatherError.visibility = View.VISIBLE
        binding.textWeatherError.text = message
    }
    
    private fun getUVLevel(uv: Int): String {
        return when {
            uv <= 2 -> "Low"
            uv <= 5 -> "Moderate"
            uv <= 7 -> "High"
            uv <= 10 -> "Very High"
            else -> "Extreme"
        }
    }
    // Retrieve API key
    private fun getAccuWeatherApiKey(): String {
        // In production, get from BuildConfig or secure storage
        return getString(R.string.accuweather_api_key)
    }
    
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
