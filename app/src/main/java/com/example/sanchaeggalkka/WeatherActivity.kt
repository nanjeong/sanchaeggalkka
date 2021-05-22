package com.example.sanchaeggalkka

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.example.sanchaeggalkka.databinding.ActivityWeatherBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*

class WeatherActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWeatherBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val size = intent.getStringExtra("size")
        val nx = intent.getIntExtra("nx", 60)
        val ny = intent.getIntExtra("ny", 127)
        val currTime = System.currentTimeMillis()
        val date = Date(currTime)
        val today = SimpleDateFormat("yyyyMMdd HH").format(date)
        val calendar = GregorianCalendar(Locale.KOREA)
        calendar.time = SimpleDateFormat("yyyyMMdd").parse(today)
        calendar.add(Calendar.DATE, -1)
        val yesterday = SimpleDateFormat("yyyyMMdd").format(calendar.time)
        Log.i("date", "today: $today yesterday: $yesterday")

        var baseDate = today.split(" ")[0]
        var baseTime: String
        when (today.split(" ")[1]) {
            "05", "06", "07" -> baseTime = "0200"
            "08", "09", "10" -> baseTime = "0500"
            "11", "12", "13" -> baseTime = "0800"
            "14", "15", "16" -> baseTime = "1100"
            "17", "18", "19" -> baseTime = "1400"
            "20", "21", "22" -> baseTime = "1700"
            "23" -> baseTime = "2000"
            "00", "01" -> {
                baseTime = "2000"
                baseDate = yesterday
            }
            "02", "03", "04" -> {
                baseTime = "2300"
                baseDate = yesterday
            }
            else -> baseTime = ""
        }

        when (size) {
            "small" -> {
                binding.explain.background =
                    ResourcesCompat.getDrawable(resources, R.color.small_size, null)
            }
            "medium" -> {
                binding.explain.background =
                    ResourcesCompat.getDrawable(resources, R.color.medium_size, null)
            }
            "large" -> {
                binding.explain.background =
                    ResourcesCompat.getDrawable(resources, R.color.large_size, null)
            }
        }

        val retrofit = Retrofit.Builder()
            .baseUrl("http://apis.data.go.kr/1360000/VilageFcstInfoService/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(WeatherInterface::class.java)

        service.getWeather(10, 1, "JSON", baseDate, baseTime, nx.toString(), ny.toString())
            .enqueue(object : Callback<WeatherForecast> {
                override fun onResponse(
                    call: Call<WeatherForecast>,
                    response: Response<WeatherForecast>
                ) {
                    if (response.isSuccessful) {
                        Log.i("weather api", response.body()?.response?.body?.items?.item?.get(1).toString())
                        Log.i("weather api", response.body()?.response?.body?.items?.item?.get(4).toString())

                        binding.textTemperature.text = response.body()?.response?.body?.items?.item?.get(4)?.fcstValue.toString() + "°C"
                    }
                }

                override fun onFailure(call: Call<WeatherForecast>, t: Throwable) {
                    Log.i("weather api", "fail ${t.message}")
                }
            })
    }
}