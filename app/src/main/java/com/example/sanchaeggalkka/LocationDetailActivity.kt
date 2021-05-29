package com.example.sanchaeggalkka

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.sanchaeggalkka.databinding.ActivityLocationDetailBinding
import com.example.sanchaeggalkka.db.DistrictDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LocationDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLocationDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocationDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val application = requireNotNull(this).application
        val db = DistrictDatabase.getInstance(application).locDao
        val locationId = intent.getLongExtra("id", 0L)

        CoroutineScope(Dispatchers.IO).launch {
            val thisLocation = db.get(locationId)

            CoroutineScope(Dispatchers.Main).launch {
                binding.locationName.text = thisLocation.lcName
                binding.superDistrict.text = thisLocation.name1
                binding.sigungu.text = thisLocation.name2
                binding.dong.text = thisLocation.name3

                binding.updateButton.setOnClickListener {
                    val updateIntent = Intent(application, LocationActivity::class.java)

                    updateIntent.putExtra("start", "locationDetail")
                    updateIntent.putExtra("lcName", thisLocation.lcName)
                    updateIntent.putExtra("id", locationId)

                    startActivity(updateIntent)
                }
            }
        }

        binding.delete.setOnClickListener {
            val deleteDialog = AlertDialog.Builder(this)
            deleteDialog.setMessage(R.string.dialog_delete)
            deleteDialog.setPositiveButton(R.string.delete, DialogInterface.OnClickListener { dialog, which ->
                CoroutineScope(Dispatchers.IO).launch {
                    val thisLocation = db.get(locationId)
                    db.delete(thisLocation)
                }
                
            })
            deleteDialog.setNegativeButton(R.string.cancel, DialogInterface.OnClickListener { dialog, which ->

            })
            deleteDialog.create().show()
        }

        binding.setCurrentLocation.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val district = db.get(locationId)

                val spX = getSharedPreferences("currentLocationX", Context.MODE_PRIVATE)
                val editorX = spX.edit()
                editorX.putInt("nx", district.x)
                editorX.commit()

                val spY = getSharedPreferences("currentLocationY", Context.MODE_PRIVATE)
                val editorY = spY.edit()
                editorY.putInt("ny", district.y)
                editorY.commit()
            }
            Toast.makeText(this, "현위치를 변경했습니다.", Toast.LENGTH_SHORT).show()
        }
    }
}