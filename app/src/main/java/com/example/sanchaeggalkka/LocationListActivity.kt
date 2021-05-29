package com.example.sanchaeggalkka

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.sanchaeggalkka.databinding.ActivityLocationListBinding
import com.example.sanchaeggalkka.db.DistrictDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LocationListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLocationListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocationListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val application = requireNotNull(this).application

        val dataSource = DistrictDatabase.getInstance(application).locDao

        val viewModelFactory = LocationListViewModelFactory(dataSource, application)

        val viewModel = ViewModelProvider(this, viewModelFactory).get(LocationListViewModel::class.java)

        binding.locationListViewModel = viewModel

        val adapter = LocationAdapter(LocationListener { id ->
            Toast.makeText(this, "$id", Toast.LENGTH_SHORT).show()
            viewModel.onLocationClicked(id)
        })
        binding.locationRecyclerview.adapter = adapter

        viewModel.locations.observe(this, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        viewModel.navigateToLocationDetail.observe(this, Observer {
            it?.let {
                val detailIntent = Intent(this, LocationDetailActivity::class.java)

                detailIntent.putExtra("id", it)

                startActivity(detailIntent)
            }
        })

        binding.plus.setOnClickListener {
            val locationIntent = Intent(this, LocationActivity::class.java)

            locationIntent.putExtra("start", "locationList")

            startActivity(locationIntent)
        }
    }
}