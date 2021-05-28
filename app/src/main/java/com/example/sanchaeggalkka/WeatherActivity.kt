package com.example.sanchaeggalkka

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
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

        val spX = getSharedPreferences("currentLocationX", Context.MODE_PRIVATE)
        val spY = getSharedPreferences("currentLocationY", Context.MODE_PRIVATE)

        val nx = spX.getInt("nx", 60)
        val ny = spY.getInt("ny", 127)

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
                binding.explain1.background =
                    ResourcesCompat.getDrawable(resources, R.color.small_size, null)
                binding.explain2.background =
                    ResourcesCompat.getDrawable(resources, R.color.small_size, null)
                binding.explain3.background =
                    ResourcesCompat.getDrawable(resources, R.color.small_size, null)
            }
            "medium" -> {
                binding.explain1.background =
                    ResourcesCompat.getDrawable(resources, R.color.medium_size, null)
                binding.explain2.background =
                    ResourcesCompat.getDrawable(resources, R.color.medium_size, null)
                binding.explain3.background =
                    ResourcesCompat.getDrawable(resources, R.color.medium_size, null)
            }
            "large" -> {
                binding.explain1.background =
                    ResourcesCompat.getDrawable(resources, R.color.large_size, null)
                binding.explain2.background =
                    ResourcesCompat.getDrawable(resources, R.color.large_size, null)
                binding.explain3.background =
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
                        val weatherInf = response.body()?.response?.body?.items?.item
                        var temperature = 80
                        var rain = -1
                        if (weatherInf != null) {
                            for (info in weatherInf) {
                                if (info.category == "T3H") temperature = info.fcstValue.toInt()
                                if (info.category == "PTY") rain = info.fcstValue.toInt()
                            }
                        }
                        if (temperature != 80 && size != null) {
                            showInformation(temperature, size)
                            binding.rain.text = when (rain) {
                                1 -> "강수형태: 비"
                                2 -> "강수형태: 진눈깨비"
                                3 -> "강수형태: 눈"
                                4 -> "강수형태: 소나기"
                                5 -> "강수형태: 빗방울"
                                6 -> "강수형태: 빗방울/눈날림"
                                else -> ""
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<WeatherForecast>, t: Throwable) {
                    Log.i("weather api", "fail ${t.message}")
                    Log.i("weather api", "nx: $nx, ny: $ny")
                    showToast()
                }
            })

        var infoDetailFlag = 0 // visibility == gone
        binding.info.setOnClickListener {
            if (infoDetailFlag == 0) {
                binding.infoDetail.visibility = View.VISIBLE
                infoDetailFlag = 1 // visibility == visible
            } else if (infoDetailFlag == 1) {
                binding.infoDetail.visibility = View.GONE
                infoDetailFlag = 0
            }
        }

        binding.container.setOnClickListener {
            binding.infoDetail.visibility = View.GONE
            infoDetailFlag = 0
        }

        binding.location.setOnClickListener {
            val lcIntent = Intent(this, LocationListActivity::class.java)

            startActivity(lcIntent)
        }
    }

    private fun showInformation(temperature: Int, size: String) {
        binding.textTemperature.text = "$temperature°C"
        when (size) {
            "small" -> {
                if (temperature in 10..20) explain(1)
                else if ((temperature in 21..22) || (temperature in 7..9)) explain(2)
                else if ((temperature in 23..28) || (-1 <= temperature && temperature < 7)) explain(3)
                else if ((temperature in 29..31) || (-4 <= temperature && temperature < -1)) explain(4)
                else if (32 <= temperature) explain(5)
                else if (temperature < -4) explain(6)
                else {}
            }
            "medium" -> {
                if (temperature in 10..20) explain(1)
                else if ((temperature in 21..22) || (temperature in 7..9)) explain(2)
                else if ((temperature in 23..28) || (-1 < temperature && temperature < 7)) explain(3)
                else if ((temperature in 29..31) || (-9 <= temperature && temperature < -1)) explain(4)
                else if (32 <= temperature) explain(5)
                else if (temperature < -9) explain(6)
                else {}
            }
            "large" -> {
                if (temperature in 7..17) explain(1)
                else if ((temperature in 18..20) || (temperature in 4..6)) explain(2)
                else if ((temperature in 21..25) || (-6 <= temperature && temperature < 4)) explain(3)
                else if ((temperature in 26..28) || (-9 <= temperature && temperature < -6)) explain(4)
                else if (29 <= temperature) explain(5)
                else if (temperature < -9) explain(6)
                else {}
            }
        }
    }

    private fun explain(no: Int) {
        when (no) {
            1 -> {
                binding.explain2.text = "산책가기 딱 좋은 날씨!"
            }
            2 -> {
                binding.explain2.text = "산책가도 좋아요"
                binding.explain3.text = "약간 주의가 필요해요"
            }
            3 -> {
                binding.explain2.text = "견종에 따라 안전한 날씨는 아니에요"
                binding.explain3.text = "밖에 나가면 잘 살펴봐주세요"
            }
            4 -> {
                binding.explain2.text = "산책가기 좋은 날씨가 아니에요"
                binding.explain3.text = "주의해주세요"
            }
            5 -> {
                binding.explain1.text = "너무 더워요"
                binding.explain2.text = "생명에 지장을 줄 수 있어요"
                binding.explain3.text = "장시간 산책은 피해주세요"
            }
            6 -> {
                binding.explain1.text = "너무 추워요"
                binding.explain2.text = "생명에 지장을 줄 수 있어요"
                binding.explain3.text = "장시간 산책은 피해주세요"
            }
        }
    }

    private fun showToast() {
        Toast.makeText(this, "날씨 정보를 가져오는데 실패했습니다. 다시 시도해주세요.", Toast.LENGTH_LONG).show()
    }
}