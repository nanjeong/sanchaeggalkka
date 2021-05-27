package com.example.sanchaeggalkka

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.sanchaeggalkka.databinding.ActivityLocationListBinding
import com.example.sanchaeggalkka.db.DistrictDatabase

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

        val adapter = LocationAdapter()
        binding.locationRecyclerview.adapter = adapter

        viewModel.locations.observe(this, Observer {
            it?.let {
                adapter.data = it
            }
        })
    }
}