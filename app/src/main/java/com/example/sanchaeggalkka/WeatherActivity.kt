package com.example.sanchaeggalkka

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.example.sanchaeggalkka.databinding.ActivityWeatherBinding

class WeatherActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWeatherBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val size = intent.getStringExtra("size")
        val location = intent.getStringArrayExtra("location")

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
    }
}