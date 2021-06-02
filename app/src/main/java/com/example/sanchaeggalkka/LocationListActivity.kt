package com.example.sanchaeggalkka

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

            viewModel.onLocationClicked(id)
        },
            MoreListener { id ->
                val balloon = Balloon.Builder(this)
                    .setLayout(R.layout.layout_more)
                    .setIsVisibleArrow(false)
                    .setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.balloon_background)
                    )
                    .build()

                balloon.showAlignTop(binding.guide)

                val setLocBtn: TextView = balloon.getContentView().findViewById(R.id.set_current_location)
                val deleteBtn: TextView = balloon.getContentView().findViewById(R.id.delete)

                setLocBtn.setOnClickListener {
                    CoroutineScope(Dispatchers.IO).launch {
                        val clickedLoc = dataSource.get(id)
                        clickedLoc.current = 1
                        dataSource.update(clickedLoc)

                        val sp = getSharedPreferences("currentLocation", Context.MODE_PRIVATE)
                        val currId = sp.getLong("currId", 0)

                        if (currId != 0L) {
                            val currLoc = dataSource.get(currId)
                            currLoc.current = 0
                            dataSource.update(currLoc)
                        }

                        val editor = sp.edit()
                        editor.putLong("currId", id)
                        editor.putString("currName", clickedLoc.lcName)
                        editor.putInt("nx", clickedLoc.x)
                        editor.putInt("ny", clickedLoc.y)
                        editor.commit()
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

                        val sp = getSharedPreferences("currentLocation", Context.MODE_PRIVATE)
                        val editor = sp.edit()
                        val currId = sp.getLong("currId", 0)

                        if (currId == id) {
                            CoroutineScope(Dispatchers.Main).launch {
                                Toast.makeText(application, "현재 위치를 설정해주세요", Toast.LENGTH_SHORT).show()
                            }
                            editor.putLong("currId", 0L)
                            editor.putString("currName", "위치를 설정해주세요. 기본 위치: 서울")
                            editor.putInt("nx", 0)
                            editor.putInt("ny", 0)
                            editor.commit()
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