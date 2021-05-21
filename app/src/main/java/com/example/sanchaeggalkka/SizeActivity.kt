package com.example.sanchaeggalkka

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.sanchaeggalkka.databinding.ActivitySizeBinding

class SizeActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySizeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySizeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val location = intent.getStringArrayExtra("location")

        binding.small.setOnClickListener {
            val smallIntent = Intent(this, WeatherActivity::class.java)

            smallIntent.putExtra("size", "small")
            smallIntent.putExtra("location", location)

            startActivity(smallIntent)
        }

        binding.medium.setOnClickListener {
            val mediumIntent = Intent(this, WeatherActivity::class.java)

            mediumIntent.putExtra("size", "medium")
            mediumIntent.putExtra("location", location)

            startActivity(mediumIntent)
        }

        binding.large.setOnClickListener {
            val largeIntent = Intent(this, WeatherActivity::class.java)

            largeIntent.putExtra("size", "large")
            largeIntent.putExtra("location", location)

            startActivity(largeIntent)
        }
    }
}