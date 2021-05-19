package com.example.sanchaeggalkka

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.res.ResourcesCompat
import com.example.sanchaeggalkka.databinding.ActivityWeatherBinding

class WeatherActivity : AppCompatActivity() {

    private var lng: Double = 0.0
    private var lat: Double = 0.0

    private lateinit var binding: ActivityWeatherBinding

    private lateinit var locationManager: LocationManager

    private val locationListener = LocationListener {
        fun onLocationChanged(location: Location) {
            lng = location.longitude
            lat = location.latitude
        }
    }

    companion object {
        const val PERMISSION_REQUEST_CODE = 5055
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        getUserLocation()
        Log.i("weatherActivity", "longitude : $lng latitude : $lat")

        val size = intent.getStringExtra("size")

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

    private fun getUserLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) requestLocationPermissions()
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0F, locationListener)
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0F, locationListener)
        }
    }

    private fun requestLocationPermissions() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                        showDialogToGetPermission()
                    } else {
                        requestPermissions(
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                            PERMISSION_REQUEST_CODE
                        )
                    }
                }
            }
        }
    }

    private fun showDialogToGetPermission() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("권한 요청")
            .setMessage(
                "앱을 사용하려면 위치 정보가 필요합니다." +
                        "위치 권한을 허용하시겠습니까?"
            )

        builder.setPositiveButton("네") { _, _ ->
            val intent = Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", packageName, null)
            )
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
        builder.setNegativeButton("아니요") { _, _ ->
            ActivityCompat.finishAffinity(this)
            System.exit(0)
        }

        val dialog = builder.create()
        dialog.show()
    }
}