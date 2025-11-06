package com.senateway.guesthouse.data.model

// Represents all the weather details fetched from the API
//“Weather API Overview.” Google for Developers, 2025, developers.google.com/maps/documentation/weather/overview.
data class WeatherData(
    val Temperature: TemperatureUnit?,
    val WeatherText: String?,
    val WeatherIcon: Int?,
    val RealFeelTemperature: TemperatureUnit?,
    val RelativeHumidity: Int?,
    val Wind: WindData?,
    val WindGust: WindGustData?,
    val Pressure: PressureUnit?,
    val Visibility: VisibilityUnit?,
    val UVIndex: Int?,
    val CloudCover: Int?,
    val DewPoint: TemperatureUnit?
)

data class TemperatureUnit(
    val Metric: MetricValue
)

data class MetricValue(
    val Value: Double,
    val Unit: String
)

data class WindData(
    val Speed: MetricValue,
    val Direction: WindDirection
)

data class WindDirection(
    val Degrees: Int,
    val Localized: String,
    val English: String
)

data class WindGustData(
    val Speed: MetricValue
)

data class PressureUnit(
    val Metric: MetricValue
)

data class VisibilityUnit(
    val Metric: MetricValue
)

