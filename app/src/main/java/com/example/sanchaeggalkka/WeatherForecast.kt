package com.example.sanchaeggalkka

data class WeatherForecast (
    val response: ForecastResponse
        )

data class ForecastResponse(
    val header: ForecastHeader,
    val body: ForecastBody
)

data class ForecastHeader(
    val resultCode: String,
    val resultMsg: String
)

data class ForecastBody(
    val dType: String,
    val items: ForecastItems,
    val pageNo: Int,
    val numOfRows: Int,
    val totalCount: Int
)

data class ForecastItems(
    val item: List<ForecastItem>
)

data class ForecastItem(
    val baseDate: String,
    val baseTime: String,
    val category: String,
    val fcstDate: String,
    val fcstTime: String,
    val fcstValue: String,
    val nx: Int,
    val ny: Int
)