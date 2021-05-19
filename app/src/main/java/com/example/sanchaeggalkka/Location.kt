package com.example.sanchaeggalkka

import android.content.res.AssetManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.example.sanchaeggalkka.db.AdministrativeDistrict
import com.example.sanchaeggalkka.db.DistrictDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.InputStream

class Location : AppCompatActivity() {

    private lateinit var spinnerSigungu: Spinner
    private lateinit var spinnerDong: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)

        val db = DistrictDatabase.getInstance(this)
        var flag = 0

        CoroutineScope(Dispatchers.IO).launch {
            if (db.districtDatabaseDao.getAll().isEmpty()) {
                flag = 1
            }
        }

        if (flag == 1) {
            val assetManager: AssetManager = resources.assets
            val inputStream: InputStream = assetManager.open("DistrictDB.txt")

            inputStream.bufferedReader().readLines().forEach {
                var token = it.split("\t")
                val input = AdministrativeDistrict(
                    name1 = token[0],
                    name2 = token[1],
                    name3 = token[2],
                    x = token[3].toInt(),
                    y = token[4].toInt()
                )
                CoroutineScope(Dispatchers.IO).launch {
                    db.districtDatabaseDao.insert(input)
                }
            }
        }

        val spinnerSuper: Spinner = findViewById(R.id.super_district)
        ArrayAdapter.createFromResource(
            this,
            R.array.super_district_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerSuper.adapter = adapter
        }

        spinnerSigungu = findViewById(R.id.sigungu)
        spinnerDong = findViewById(R.id.dong)

        spinnerSuper.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (spinnerSuper.getItemAtPosition(position)) {
                    "서울특별시" -> {
                        setSigunguAdapter(R.array.seoul_array)
                    }
                    "부산광역시" -> {
                        setSigunguAdapter(R.array.busan_array)
                    }
                    "대구광역시" -> {
                        setSigunguAdapter(R.array.daegu_array)
                    }
                    "인천광역시" -> {
                        setSigunguAdapter(R.array.incheon_array)
                    }
                    "광주광역시" -> {
                        setSigunguAdapter(R.array.gwangju_array)
                    }
                    "대전광역시" -> {
                        setSigunguAdapter(R.array.daejeon_array)
                    }
                    "울산광역시" -> {
                        setSigunguAdapter(R.array.ulsan_array)
                    }
                    "세종특별자치시" -> {
                        setSigunguAdapter(R.array.sejong_array)
                    }
                    "경기도" -> {
                        setSigunguAdapter(R.array.gyeonggi_array)
                    }
                    "강원도" -> {
                        setSigunguAdapter(R.array.gangwon_array)
                    }
                    "충청북도" -> {
                        setSigunguAdapter(R.array.chungcheongbuk_array)
                    }
                    "충청남도" -> {
                        setSigunguAdapter(R.array.chungcheongnam_array)
                    }
                    "전라북도" -> {
                        setSigunguAdapter(R.array.jeollabuk_array)
                    }
                    "전라남도" -> {
                        setSigunguAdapter(R.array.jeollanam_array)
                    }
                    "경상북도" -> {
                        setSigunguAdapter(R.array.gyeongsangbuk_array)
                    }
                    "경상남도" -> {
                        setSigunguAdapter(R.array.gyeongsangnam_array)
                    }
                    "제주특별자치도" -> {
                        setSigunguAdapter(R.array.jeju_array)
                    }
                    "이어도" -> {
                        setSigunguAdapter(R.array.mu)
                    }
                    else -> {
                        setSigunguAdapter(R.array.mu)
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }


    }

    private fun setSigunguAdapter(array: Int) {
        ArrayAdapter.createFromResource(
            this,
            array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerSigungu.adapter = adapter
        }
    }
}