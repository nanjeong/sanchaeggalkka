package com.example.sanchaeggalkka

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.room.Room
import com.example.sanchaeggalkka.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

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