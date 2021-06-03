package com.example.sanchaeggalkka

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import com.example.sanchaeggalkka.databinding.ActivityWeatherBinding
import com.skydoves.balloon.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*

class WeatherActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWeatherBinding
    private lateinit var size: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val balloon = Balloon.Builder(this)
            .setArrowSize(10)
            .setPadding(8)
            .setTextSize(15f)
            .setMarginRight(10)
            .setBackgroundColor(ContextCompat.getColor(this, R.color.balloon_background))
            .setArrowColor(ContextCompat.getColor(this, R.color.balloon_background))
            .setTextColor(ContextCompat.getColor(this, R.color.white))
            .setCornerRadius(6f)
            .setText("견종, 나이(6개월 미만, 노견), 비만 여부에 따라 추위, 더위를 느끼는 정도에 차이가 있을 수 있습니다. 앱의 정보는 참고용으로 사용해주세요.\n\n출처:The Tufts Animal Care and Condition (TACC) scales\n날씨 정보: 기상청")
            .setArrowOrientation(ArrowOrientation.TOP)
            .setArrowPosition(0.95f)
            .setTextGravity(Gravity.START)
            .build()

        binding.info.setOnClickListener {
            balloon.showAlignBottom(binding.info)
        }

        val sharedPreferences = getSharedPreferences("visitFirst", Context.MODE_PRIVATE)
        val value = sharedPreferences.getBoolean("weatherFirst", true)

        if (value) {
            balloon.showAlignBottom(binding.info)

            val editor = sharedPreferences.edit()
            editor.putBoolean("weatherFirst", false)
            editor.commit()
        }

        size = intent.getStringExtra("size") ?: "small"

        when (size) {
            "small" -> {
                binding.explain1.background =
                    ResourcesCompat.getDrawable(resources, R.color.small_size, null)
                binding.explain2.background =
                    ResourcesCompat.getDrawable(resources, R.color.small_size, null)
                binding.explain3.background =
                    ResourcesCompat.getDrawable(resources, R.color.small_size, null)
                binding.currLocation.setImageResource(R.drawable.ic_checked_location_on_24)
            }
            "medium" -> {
                binding.explain1.background =
                    ResourcesCompat.getDrawable(resources, R.color.medium_size, null)
                binding.explain2.background =
                    ResourcesCompat.getDrawable(resources, R.color.medium_size, null)
                binding.explain3.background =
                    ResourcesCompat.getDrawable(resources, R.color.medium_size, null)
                binding.currLocation.setImageResource(R.drawable.ic_medium_location_on_24)
            }
            "large" -> {
                binding.explain1.background =
                    ResourcesCompat.getDrawable(resources, R.color.large_size, null)
                binding.explain2.background =
                    ResourcesCompat.getDrawable(resources, R.color.large_size, null)
                binding.explain3.background =
                    ResourcesCompat.getDrawable(resources, R.color.large_size, null)
                binding.currLocation.setImageResource(R.drawable.ic_large_location_on_24)
            }
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

    private fun showToast(nx: Int, ny: Int) {
        if (nx == 0 && ny == 0) {
            Toast.makeText(this, "위치 설정이 올바르지 않습니다. 위치를 다시 설정해주세요.", Toast.LENGTH_LONG).show()
        } else {
        Toast.makeText(this, "날씨 정보를 가져오는데 실패했습니다. 다시 시도해주세요.", Toast.LENGTH_LONG).show()
        }
    }

    override fun onResume() {
        super.onResume()
        val sp = getSharedPreferences("currentLocation", Context.MODE_PRIVATE)
        val nx = sp.getInt("nx", 60)
        val ny = sp.getInt("ny", 127)
        var currName = sp.getString("currName", "") ?: ""

        if (currName == "") {
            currName = "현재 위치를 설정해주세요. 기본 위치: 서울"
        }
        binding.currentName.text = currName

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
                    showToast(nx, ny)
                }
            })
    }
}