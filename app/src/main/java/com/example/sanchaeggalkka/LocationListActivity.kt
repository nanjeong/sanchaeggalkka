package com.example.sanchaeggalkka

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.sanchaeggalkka.databinding.ActivityLocationListBinding
import com.example.sanchaeggalkka.db.DistrictDatabase
import com.skydoves.balloon.Balloon
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
        },
        MoreListener { id ->
            val balloon = Balloon.Builder(this)
                .setLayout(R.layout.layout_more)
                .setIsVisibleArrow(false)
                .setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.balloon_background))
                .build()

            balloon.showAlignTop(binding.guide)

            val setLocBtn: TextView = balloon.getContentView().findViewById(R.id.set_current_location)
            val deleteBtn: TextView = balloon.getContentView().findViewById(R.id.delete)

            setLocBtn.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                    val clickedLoc = dataSource.get(id)

                    val spX = getSharedPreferences("currentLocationX", Context.MODE_PRIVATE)
                    val editorX = spX.edit()
                    editorX.putInt("nx", clickedLoc.x)
                    editorX.commit()

                    val spY = getSharedPreferences("currentLocationY", Context.MODE_PRIVATE)
                    val editorY = spY.edit()
                    editorY.putInt("ny", clickedLoc.y)
                    editorY.commit()
                }
                balloon.dismiss()
                Toast.makeText(this, "현위치를 변경했습니다.", Toast.LENGTH_SHORT).show()
            }

            deleteBtn.setOnClickListener {
                balloon.dismiss()
                val deleteDialog = AlertDialog.Builder(this)
                deleteDialog.setMessage(R.string.dialog_delete)
                deleteDialog.setPositiveButton(R.string.delete, DialogInterface.OnClickListener { _, _ ->
                    CoroutineScope(Dispatchers.IO).launch {
                        val thisLocation = dataSource.get(id)

                        val spX = getSharedPreferences("currentLocationX", Context.MODE_PRIVATE)
                        val spY = getSharedPreferences("currentLocationY", Context.MODE_PRIVATE)

                        val nx = spX.getInt("nx", 0)
                        val ny = spY.getInt("ny", 0)

                        if (thisLocation.x == nx && thisLocation.y == ny) {
                            CoroutineScope(Dispatchers.Main).launch {
                                Toast.makeText(application, "현재 위치를 설정해주세요", Toast.LENGTH_SHORT).show()
                            }
                        }
                        dataSource.delete(thisLocation)
                    }
                })
                deleteDialog.setNegativeButton(R.string.cancel, DialogInterface.OnClickListener { _, _ ->

                })
                deleteDialog.create().show()
            }
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