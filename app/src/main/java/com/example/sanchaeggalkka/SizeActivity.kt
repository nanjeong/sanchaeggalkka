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

        val nx = intent.getIntExtra("nx", 60)
        val ny = intent.getIntExtra("ny", 127)

        binding.small.setOnClickListener {
            val smallIntent = Intent(this, WeatherActivity::class.java)

            smallIntent.putExtra("size", "small")
            smallIntent.putExtra("nx", nx)
            smallIntent.putExtra("ny", ny)

            startActivity(smallIntent)
        }

        binding.medium.setOnClickListener {
            val mediumIntent = Intent(this, WeatherActivity::class.java)

            mediumIntent.putExtra("size", "medium")
            mediumIntent.putExtra("nx", nx)
            mediumIntent.putExtra("ny", ny)

            startActivity(mediumIntent)
        }

        binding.large.setOnClickListener {
            val largeIntent = Intent(this, WeatherActivity::class.java)

            largeIntent.putExtra("size", "large")
            largeIntent.putExtra("nx", nx)
            largeIntent.putExtra("ny", ny)

            startActivity(largeIntent)
        }
    }
}