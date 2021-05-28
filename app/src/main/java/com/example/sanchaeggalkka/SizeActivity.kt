package com.example.sanchaeggalkka

import android.content.Context
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

        val sharedPreferences = getSharedPreferences("checkFirst", Context.MODE_PRIVATE)
        val value = sharedPreferences.getBoolean("first", false)
        if (!value) {
            val editor = sharedPreferences.edit()
            editor.putBoolean("first", true)
            editor.commit()

            val locationIntent = Intent(this, LocationActivity::class.java)

            startActivity(locationIntent)
        }

        binding.small.setOnClickListener {
            val smallIntent = Intent(this, WeatherActivity::class.java)

            smallIntent.putExtra("size", "small")

            startActivity(smallIntent)
        }

        binding.medium.setOnClickListener {
            val mediumIntent = Intent(this, WeatherActivity::class.java)

            mediumIntent.putExtra("size", "medium")

            startActivity(mediumIntent)
        }

        binding.large.setOnClickListener {
            val largeIntent = Intent(this, WeatherActivity::class.java)

            largeIntent.putExtra("size", "large")

            startActivity(largeIntent)
        }
    }
}