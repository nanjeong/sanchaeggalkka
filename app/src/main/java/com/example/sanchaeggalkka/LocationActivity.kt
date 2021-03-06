package com.example.sanchaeggalkka

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.example.sanchaeggalkka.databinding.ActivityLocationBinding
import com.example.sanchaeggalkka.db.AdministrativeDistrict
import com.example.sanchaeggalkka.db.DistrictDatabase
import com.example.sanchaeggalkka.db.Loc
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LocationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLocationBinding
    private lateinit var start: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocationBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val selectedDistrict: Array<String> = Array(3) { "" }
        var district: List<AdministrativeDistrict> = listOf()
        val application = requireNotNull(this).application
        val db = DistrictDatabase.getInstance(application)

        start = intent.getStringExtra("start") ?: ""

        if (start == "locationList" || start == "locationDetail") {
            binding.nextButton.visibility = View.INVISIBLE
            binding.saveButton.visibility = View.VISIBLE
        } else {
            binding.nextButton.visibility = View.VISIBLE
            binding.saveButton.visibility = View.INVISIBLE
        }

        if (start == "locationDetail") {
            binding.pageName.text = "μμΉ μμ "

            val lcName = intent.getStringExtra("lcName")

            lcName?.let {
                binding.locationName.setText(it)
            }
        }

        CoroutineScope(Dispatchers.Main).launch {
            val job = CoroutineScope(Dispatchers.IO).launch {
                district = db.districtDao.getAll()
            }
            job.join()
            Log.i("locationn", "${district.size}")

            ArrayAdapter.createFromResource(
                application,
                R.array.super_district_array,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.superDistrict.adapter = adapter
            }

            if (start == "locationDetail") {
                val name1 = intent.getStringExtra("name1") ?: ""

                if (name1 != "") {
                    binding.superDistrict.setSelection(getIndex(binding.superDistrict, name1))
                }
            }

            binding.superDistrict.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        selectedDistrict[0] =
                            binding.superDistrict.getItemAtPosition(position) as String
                        selectedDistrict[1] = ""
                        selectedDistrict[2] = ""
                        when (binding.superDistrict.getItemAtPosition(position)) {
                            "μμΈνΉλ³μ" -> setSigunguAdapter(R.array.seoul_array)
                            "λΆμ°κ΄μ­μ" -> setSigunguAdapter(R.array.busan_array)
                            "λκ΅¬κ΄μ­μ" -> setSigunguAdapter(R.array.daegu_array)
                            "μΈμ²κ΄μ­μ" -> setSigunguAdapter(R.array.incheon_array)
                            "κ΄μ£Όκ΄μ­μ" -> setSigunguAdapter(R.array.gwangju_array)
                            "λμ κ΄μ­μ" -> setSigunguAdapter(R.array.daejeon_array)
                            "μΈμ°κ΄μ­μ" -> setSigunguAdapter(R.array.ulsan_array)
                            "μΈμ’νΉλ³μμΉμ" -> setSigunguAdapter(R.array.sejong_array)
                            "κ²½κΈ°λ" -> setSigunguAdapter(R.array.gyeonggi_array)
                            "κ°μλ" -> setSigunguAdapter(R.array.gangwon_array)
                            "μΆ©μ²­λΆλ" -> setSigunguAdapter(R.array.chungcheongbuk_array)
                            "μΆ©μ²­λ¨λ" -> setSigunguAdapter(R.array.chungcheongnam_array)
                            "μ λΌλΆλ" -> setSigunguAdapter(R.array.jeollabuk_array)
                            "μ λΌλ¨λ" -> setSigunguAdapter(R.array.jeollanam_array)
                            "κ²½μλΆλ" -> setSigunguAdapter(R.array.gyeongsangbuk_array)
                            "κ²½μλ¨λ" -> setSigunguAdapter(R.array.gyeongsangnam_array)
                            "μ μ£ΌνΉλ³μμΉλ" -> setSigunguAdapter(R.array.jeju_array)
                            "μ΄μ΄λ" -> {
                                setSigunguAdapter(R.array.mu)
                            }
                            else -> {
                                selectedDistrict[0] = ""
                                setSigunguAdapter(R.array.mu)
                            }
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                    }
                }


            binding.sigungu.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        when (selectedDistrict[0]) {
                            "μμΈνΉλ³μ" -> {
                                selectedDistrict[1] =
                                    binding.sigungu.getItemAtPosition(position) as String
                                selectedDistrict[2] = ""
                                when (binding.sigungu.getItemAtPosition(position)) {
                                    "μ’λ‘κ΅¬" -> setDongAdapter(2, 18, district)
                                    "μ€κ΅¬" -> setDongAdapter(20, 34, district)
                                    "μ©μ°κ΅¬" -> setDongAdapter(36, 51, district)
                                    "μ±λκ΅¬" -> setDongAdapter(53, 69, district)
                                    "κ΄μ§κ΅¬" -> setDongAdapter(71, 85, district)
                                    "λλλ¬Έκ΅¬" -> setDongAdapter(87, 100, district)
                                    "μ€λκ΅¬" -> setDongAdapter(102, 117, district)
                                    "μ±λΆκ΅¬" -> setDongAdapter(119, 138, district)
                                    "κ°λΆκ΅¬" -> setDongAdapter(140, 152, district)
                                    "λλ΄κ΅¬" -> setDongAdapter(154, 167, district)
                                    "λΈμκ΅¬" -> setDongAdapter(169, 187, district)
                                    "μνκ΅¬" -> setDongAdapter(189, 204, district)
                                    "μλλ¬Έκ΅¬" -> setDongAdapter(206, 219, district)
                                    "λ§ν¬κ΅¬" -> setDongAdapter(221, 236, district)
                                    "μμ²κ΅¬" -> setDongAdapter(238, 255, district)
                                    "κ°μκ΅¬" -> setDongAdapter(257, 276, district)
                                    "κ΅¬λ‘κ΅¬" -> setDongAdapter(278, 293, district)
                                    "κΈμ²κ΅¬" -> setDongAdapter(295, 304, district)
                                    "μλ±ν¬κ΅¬" -> setDongAdapter(306, 323, district)
                                    "λμκ΅¬" -> setDongAdapter(325, 339, district)
                                    "κ΄μκ΅¬" -> setDongAdapter(341, 361, district)
                                    "μμ΄κ΅¬" -> setDongAdapter(363, 380, district)
                                    "κ°λ¨κ΅¬" -> setDongAdapter(382, 403, district)
                                    "μ‘νκ΅¬" -> setDongAdapter(405, 431, district)
                                    "κ°λκ΅¬" -> setDongAdapter(433, 450, district)
                                    else -> {
                                        selectedDistrict[1] = ""
                                        setMuAdapter(R.array.mu)
                                    }
                                }
                            }
                            "λΆμ°κ΄μ­μ" -> {
                                selectedDistrict[1] =
                                    binding.sigungu.getItemAtPosition(position) as String
                                selectedDistrict[2] = ""
                                when (binding.sigungu.getItemAtPosition(position)) {
                                    "μ€κ΅¬" -> setDongAdapter(453, 461, district)
                                    "μκ΅¬" -> setDongAdapter(463, 475, district)
                                    "λκ΅¬" -> setDongAdapter(477, 488, district)
                                    "μλκ΅¬" -> setDongAdapter(490, 500, district)
                                    "λΆμ°μ§κ΅¬" -> setDongAdapter(502, 521, district)
                                    "λλκ΅¬" -> setDongAdapter(523, 535, district)
                                    "λ¨κ΅¬" -> setDongAdapter(537, 553, district)
                                    "λΆκ΅¬" -> setDongAdapter(555, 567, district)
                                    "ν΄μ΄λκ΅¬" -> setDongAdapter(569, 586, district)
                                    "μ¬νκ΅¬" -> setDongAdapter(588, 603, district)
                                    "κΈμ κ΅¬" -> setDongAdapter(605, 620, district)
                                    "κ°μκ΅¬" -> setDongAdapter(622, 629, district)
                                    "μ°μ κ΅¬" -> setDongAdapter(631, 642, district)
                                    "μμκ΅¬" -> setDongAdapter(644, 653, district)
                                    "μ¬μκ΅¬" -> setDongAdapter(655, 666, district)
                                    "κΈ°μ₯κ΅°" -> setDongAdapter(668, 672, district)
                                    else -> {
                                        selectedDistrict[1] = ""
                                        setMuAdapter(R.array.mu)
                                    }
                                }
                            }
                            "λκ΅¬κ΄μ­μ" -> {
                                selectedDistrict[1] =
                                    binding.sigungu.getItemAtPosition(position) as String
                                selectedDistrict[2] = ""
                                when (binding.sigungu.getItemAtPosition(position)) {
                                    "μ€κ΅¬" -> setDongAdapter(675, 686, district)
                                    "λκ΅¬" -> setDongAdapter(688, 710, district)
                                    "μκ΅¬" -> setDongAdapter(712, 728, district)
                                    "λ¨κ΅¬" -> setDongAdapter(730, 742, district)
                                    "λΆκ΅¬" -> setDongAdapter(744, 766, district)
                                    "μμ±κ΅¬" -> setDongAdapter(768, 790, district)
                                    "λ¬μκ΅¬" -> setDongAdapter(792, 813, district)
                                    "λ¬μ±κ΅°" -> setDongAdapter(815, 823, district)
                                    else -> {
                                        selectedDistrict[1] = ""
                                        setMuAdapter(R.array.mu)
                                    }
                                }
                            }
                            "μΈμ²κ΄μ­μ" -> {
                                selectedDistrict[1] =
                                    binding.sigungu.getItemAtPosition(position) as String
                                selectedDistrict[2] = ""
                                when (binding.sigungu.getItemAtPosition(position)) {
                                    "μ€κ΅¬" -> setDongAdapter(826, 837, district)
                                    "λκ΅¬" -> setDongAdapter(839, 849, district)
                                    "λ―ΈμΆνκ΅¬" -> setDongAdapter(851, 871, district)
                                    "μ°μκ΅¬" -> setDongAdapter(873, 887, district)
                                    "λ¨λκ΅¬" -> setDongAdapter(889, 908, district)
                                    "λΆνκ΅¬" -> setDongAdapter(910, 931, district)
                                    "κ³μκ΅¬" -> setDongAdapter(933, 944, district)
                                    "μκ΅¬" -> setDongAdapter(946, 967, district)
                                    "κ°νκ΅°" -> setDongAdapter(969, 981, district)
                                    "μΉμ§κ΅°" -> setDongAdapter(983, 989, district)
                                    else -> {
                                        selectedDistrict[1] = ""
                                        setMuAdapter(R.array.mu)
                                    }
                                }
                            }
                            "κ΄μ£Όκ΄μ­μ" -> {
                                selectedDistrict[1] =
                                    binding.sigungu.getItemAtPosition(position) as String
                                selectedDistrict[2] = ""
                                when (binding.sigungu.getItemAtPosition(position)) {
                                    "λκ΅¬" -> setDongAdapter(992, 1004, district)
                                    "μκ΅¬" -> setDongAdapter(1006, 1023, district)
                                    "λ¨κ΅¬" -> setDongAdapter(1025, 1040, district)
                                    "λΆκ΅¬" -> setDongAdapter(1042, 1069, district)
                                    "κ΄μ°κ΅¬" -> setDongAdapter(1071, 1091, district)
                                    else -> {
                                        selectedDistrict[1] = ""
                                        setMuAdapter(R.array.mu)
                                    }
                                }
                            }
                            "λμ κ΄μ­μ" -> {
                                selectedDistrict[1] =
                                    binding.sigungu.getItemAtPosition(position) as String
                                selectedDistrict[2] = ""
                                when (binding.sigungu.getItemAtPosition(position)) {
                                    "λκ΅¬" -> setDongAdapter(1094, 1109, district)
                                    "μ€κ΅¬" -> setDongAdapter(1111, 1127, district)
                                    "μκ΅¬" -> setDongAdapter(1129, 1151, district)
                                    "μ μ±κ΅¬" -> setDongAdapter(1153, 1163, district)
                                    "λλκ΅¬" -> setDongAdapter(1165, 1176, district)
                                    else -> {
                                        selectedDistrict[1] = ""
                                        setMuAdapter(R.array.mu)
                                    }
                                }
                            }
                            "μΈμ°κ΄μ­μ" -> {
                                selectedDistrict[1] =
                                    binding.sigungu.getItemAtPosition(position) as String
                                selectedDistrict[2] = ""
                                when (binding.sigungu.getItemAtPosition(position)) {
                                    "μ€κ΅¬" -> setDongAdapter(1179, 1191, district)
                                    "λ¨κ΅¬" -> setDongAdapter(1193, 1206, district)
                                    "λκ΅¬" -> setDongAdapter(1208, 1216, district)
                                    "λΆκ΅¬" -> setDongAdapter(1218, 1225, district)
                                    "μΈμ£Όκ΅°" -> setDongAdapter(1227, 1238, district)
                                    else -> {
                                        selectedDistrict[1] = ""
                                        setMuAdapter(R.array.mu)
                                    }
                                }
                            }
                            "μΈμ’νΉλ³μμΉμ" -> {
                                selectedDistrict[1] =
                                    binding.sigungu.getItemAtPosition(position) as String
                                selectedDistrict[2] = ""
                                when (binding.sigungu.getItemAtPosition(position)) {
                                    "μΈμ’νΉλ³μμΉμ" -> setDongAdapter(1241, 1260, district)
                                    else -> {
                                        selectedDistrict[1] = ""
                                        setMuAdapter(R.array.mu)
                                    }
                                }
                            }
                            "κ²½κΈ°λ" -> {
                                selectedDistrict[1] =
                                    binding.sigungu.getItemAtPosition(position) as String
                                selectedDistrict[2] = ""
                                when (binding.sigungu.getItemAtPosition(position)) {
                                    "μμμ μ₯μκ΅¬" -> setDongAdapter(1263, 1272, district)
                                    "μμμ κΆμ κ΅¬" -> setDongAdapter(1274, 1285, district)
                                    "μμμ νλ¬κ΅¬" -> setDongAdapter(1287, 1296, district)
                                    "μμμ μν΅κ΅¬" -> setDongAdapter(1298, 1309, district)
                                    "μ±λ¨μ μμ κ΅¬" -> setDongAdapter(1311, 1327, district)
                                    "μ±λ¨μ μ€μκ΅¬" -> setDongAdapter(1329, 1339, district)
                                    "μ±λ¨μ λΆλΉκ΅¬" -> setDongAdapter(1341, 1362, district)
                                    "μμ λΆμ" -> setDongAdapter(1364, 1377, district)
                                    "μμμ λ§μκ΅¬" -> setDongAdapter(1379, 1392, district)
                                    "μμμ λμκ΅¬" -> setDongAdapter(1394, 1410, district)
                                    "λΆμ²μ" -> setDongAdapter(1412, 1421, district)
                                    "κ΄λͺμ" -> setDongAdapter(1423, 1440, district)
                                    "ννμ" -> setDongAdapter(1442, 1464, district)
                                    "λλμ²μ" -> setDongAdapter(1466, 1473, district)
                                    "μμ°μ μλ‘κ΅¬" -> setDongAdapter(1475, 1487, district)
                                    "μμ°μ λ¨μκ΅¬" -> setDongAdapter(1489, 1500, district)
                                    "κ³ μμ λμκ΅¬" -> setDongAdapter(1502, 1520, district)
                                    "κ³ μμ μΌμ°λκ΅¬" -> setDongAdapter(1522, 1532, district)
                                    "κ³ μμ μΌμ°μκ΅¬" -> setDongAdapter(1534, 1542, district)
                                    "κ³Όμ²μ" -> setDongAdapter(1544, 1549, district)
                                    "κ΅¬λ¦¬μ" -> setDongAdapter(1551, 1558, district)
                                    "λ¨μμ£Όμ" -> setDongAdapter(1560, 1575, district)
                                    "μ€μ°μ" -> setDongAdapter(1577, 1582, district)
                                    "μν₯μ" -> setDongAdapter(1584, 1601, district)
                                    "κ΅°ν¬μ" -> setDongAdapter(1603, 1613, district)
                                    "μμμ" -> setDongAdapter(1615, 1620, district)
                                    "νλ¨μ" -> setDongAdapter(1622, 1635, district)
                                    "μ©μΈμ μ²μΈκ΅¬" -> setDongAdapter(1637, 1647, district)
                                    "μ©μΈμ κΈ°ν₯κ΅¬" -> setDongAdapter(1649, 1665, district)
                                    "μ©μΈμ μμ§κ΅¬" -> setDongAdapter(1667, 1675, district)
                                    "νμ£Όμ" -> setDongAdapter(1677, 1696, district)
                                    "μ΄μ²μ" -> setDongAdapter(1698, 1711, district)
                                    "μμ±μ" -> setDongAdapter(1713, 1727, district)
                                    "κΉν¬μ" -> setDongAdapter(1729, 1742, district)
                                    "νμ±μ" -> setDongAdapter(1744, 1771, district)
                                    "κ΄μ£Όμ" -> setDongAdapter(1773, 1785, district)
                                    "μμ£Όμ" -> setDongAdapter(1787, 1797, district)
                                    "ν¬μ²μ" -> setDongAdapter(1799, 1812, district)
                                    "μ¬μ£Όμ" -> setDongAdapter(1814, 1825, district)
                                    "μ°μ²κ΅°" -> setDongAdapter(1827, 1836, district)
                                    "κ°νκ΅°" -> setDongAdapter(1838, 1843, district)
                                    "μνκ΅°" -> setDongAdapter(1845, 1856, district)
                                    else -> {
                                        selectedDistrict[1] = ""
                                        setMuAdapter(R.array.mu)
                                    }
                                }
                            }
                            "κ°μλ" -> {
                                selectedDistrict[1] =
                                    binding.sigungu.getItemAtPosition(position) as String
                                selectedDistrict[2] = ""
                                when (binding.sigungu.getItemAtPosition(position)) {
                                    "μΆμ²μ" -> setDongAdapter(1859, 1883, district)
                                    "μμ£Όμ" -> setDongAdapter(1885, 1909, district)
                                    "κ°λ¦μ" -> setDongAdapter(1911, 1931, district)
                                    "λν΄μ" -> setDongAdapter(1933, 1942, district)
                                    "νλ°±μ" -> setDongAdapter(1944, 1951, district)
                                    "μμ΄μ" -> setDongAdapter(1953, 1960, district)
                                    "μΌμ²μ" -> setDongAdapter(1962, 1973, district)
                                    "νμ²κ΅°" -> setDongAdapter(1975, 1984, district)
                                    "ν‘μ±κ΅°" -> setDongAdapter(1986, 1994, district)
                                    "μμκ΅°" -> setDongAdapter(1996, 2004, district)
                                    "νμ°½κ΅°" -> setDongAdapter(2006, 2013, district)
                                    "μ μ κ΅°" -> setDongAdapter(2015, 2023, district)
                                    "μ² μκ΅°" -> setDongAdapter(2025, 2035, district)
                                    "νμ²κ΅°" -> setDongAdapter(2037, 2041, district)
                                    "μκ΅¬κ΅°" -> setDongAdapter(2043, 2047, district)
                                    "μΈμ κ΅°" -> setDongAdapter(2049, 2054, district)
                                    "κ³ μ±κ΅°" -> setDongAdapter(2056, 2061, district)
                                    "μμκ΅°" -> setDongAdapter(2063, 2068, district)
                                    else -> {
                                        selectedDistrict[1] = ""
                                        setMuAdapter(R.array.mu)
                                    }
                                }
                            }
                            "μΆ©μ²­λΆλ" -> {
                                selectedDistrict[1] =
                                    binding.sigungu.getItemAtPosition(position) as String
                                selectedDistrict[2] = ""
                                when (binding.sigungu.getItemAtPosition(position)) {
                                    "μ²­μ£Όμ μλΉκ΅¬" -> setDongAdapter(2071, 2083, district)
                                    "μ²­μ£Όμ μμκ΅¬" -> setDongAdapter(2085, 2095, district)
                                    "μ²­μ£Όμ ν₯λκ΅¬" -> setDongAdapter(2097, 2107, district)
                                    "μ²­μ£Όμ μ²­μκ΅¬" -> setDongAdapter(2109, 2116, district)
                                    "μΆ©μ£Όμ" -> setDongAdapter(2118, 2142, district)
                                    "μ μ²μ" -> setDongAdapter(2144, 2160, district)
                                    "λ³΄μκ΅°" -> setDongAdapter(2162, 2172, district)
                                    "μ₯μ²κ΅°" -> setDongAdapter(2174, 2182, district)
                                    "μλκ΅°" -> setDongAdapter(2184, 2194, district)
                                    "μ¦νκ΅°" -> setDongAdapter(2196, 2197, district)
                                    "μ§μ²κ΅°" -> setDongAdapter(2199, 2205, district)
                                    "κ΄΄μ°κ΅°" -> setDongAdapter(2207, 2217, district)
                                    "μμ±κ΅°" -> setDongAdapter(2219, 2227, district)
                                    "λ¨μκ΅°" -> setDongAdapter(2229, 2236, district)
                                    else -> {
                                        selectedDistrict[1] = ""
                                        setMuAdapter(R.array.mu)
                                    }
                                }
                            }
                            "μΆ©μ²­λ¨λ" -> {
                                selectedDistrict[1] =
                                    binding.sigungu.getItemAtPosition(position) as String
                                selectedDistrict[2] = ""
                                when (binding.sigungu.getItemAtPosition(position)) {
                                    "μ²μμ λλ¨κ΅¬" -> setDongAdapter(2239, 2255, district)
                                    "μ²μμ μλΆκ΅¬" -> setDongAdapter(2257, 2269, district)
                                    "κ³΅μ£Όμ" -> setDongAdapter(2271, 2286, district)
                                    "λ³΄λ Ήμ" -> setDongAdapter(2288, 2304, district)
                                    "μμ°μ" -> setDongAdapter(2306, 2322, district)
                                    "μμ°μ" -> setDongAdapter(2324, 2338, district)
                                    "λΌμ°μ" -> setDongAdapter(2340, 2354, district)
                                    "κ³λ£‘μ" -> setDongAdapter(2356, 2359, district)
                                    "λΉμ§μ" -> setDongAdapter(2361, 2374, district)
                                    "κΈμ°κ΅°" -> setDongAdapter(2376, 2385, district)
                                    "λΆμ¬κ΅°" -> setDongAdapter(2387, 2402, district)
                                    "μμ²κ΅°" -> setDongAdapter(2404, 2416, district)
                                    "μ²­μκ΅°" -> setDongAdapter(2418, 2427, district)
                                    "νμ±κ΅°" -> setDongAdapter(2429, 2439, district)
                                    "μμ°κ΅°" -> setDongAdapter(2441, 2452, district)
                                    "νμκ΅°" -> setDongAdapter(2454, 2461, district)
                                    else -> {
                                        selectedDistrict[1] = ""
                                        setMuAdapter(R.array.mu)
                                    }
                                }
                            }
                            "μ λΌλΆλ" -> {
                                selectedDistrict[1] =
                                    binding.sigungu.getItemAtPosition(position) as String
                                selectedDistrict[2] = ""
                                when (binding.sigungu.getItemAtPosition(position)) {
                                    "μ μ£Όμ μμ°κ΅¬" -> setDongAdapter(2464, 2482, district)
                                    "μ μ£Όμ λμ§κ΅¬" -> setDongAdapter(2484, 2499, district)
                                    "κ΅°μ°μ" -> setDongAdapter(2501, 2527, district)
                                    "μ΅μ°μ" -> setDongAdapter(2529, 2557, district)
                                    "μ μμ" -> setDongAdapter(2559, 2581, district)
                                    "λ¨μμ" -> setDongAdapter(2583, 2605, district)
                                    "κΉμ μ" -> setDongAdapter(2607, 2625, district)
                                    "μμ£Όκ΅°" -> setDongAdapter(2627, 2639, district)
                                    "μ§μκ΅°" -> setDongAdapter(2641, 2651, district)
                                    "λ¬΄μ£Όκ΅°" -> setDongAdapter(2653, 2658, district)
                                    "μ₯μκ΅°" -> setDongAdapter(2660, 2666, district)
                                    "μμ€κ΅°" -> setDongAdapter(2668, 2679, district)
                                    "μμ°½κ΅°" -> setDongAdapter(2681, 2691, district)
                                    "κ³ μ°½κ΅°" -> setDongAdapter(2693, 2706, district)
                                    "λΆμκ΅°" -> setDongAdapter(2708, 2720, district)
                                    else -> {
                                        selectedDistrict[1] = ""
                                        setMuAdapter(R.array.mu)
                                    }
                                }
                            }
                            "μ λΌλ¨λ" -> {
                                selectedDistrict[1] =
                                    binding.sigungu.getItemAtPosition(position) as String
                                selectedDistrict[2] = ""
                                when (binding.sigungu.getItemAtPosition(position)) {
                                    "λͺ©ν¬μ" -> setDongAdapter(2723, 2745, district)
                                    "μ¬μμ" -> setDongAdapter(2747, 2773, district)
                                    "μμ²μ" -> setDongAdapter(2775, 2798, district)
                                    "λμ£Όμ" -> setDongAdapter(2800, 2819, district)
                                    "κ΄μμ" -> setDongAdapter(2821, 2832, district)
                                    "λ΄μκ΅°" -> setDongAdapter(2834, 2845, district)
                                    "κ³‘μ±κ΅°" -> setDongAdapter(2847, 2857, district)
                                    "κ΅¬λ‘κ΅°" -> setDongAdapter(2859, 2866, district)
                                    "κ³ ν₯κ΅°" -> setDongAdapter(2868, 2883, district)
                                    "λ³΄μ±κ΅°" -> setDongAdapter(2885, 2896, district)
                                    "νμκ΅°" -> setDongAdapter(2898, 2910, district)
                                    "μ₯ν₯κ΅°" -> setDongAdapter(2912, 2921, district)
                                    "κ°μ§κ΅°" -> setDongAdapter(2923, 2933, district)
                                    "ν΄λ¨κ΅°" -> setDongAdapter(2935, 2948, district)
                                    "μμκ΅°" -> setDongAdapter(2950, 2960, district)
                                    "λ¬΄μκ΅°" -> setDongAdapter(2962, 2970, district)
                                    "ν¨νκ΅°" -> setDongAdapter(2972, 2980, district)
                                    "μκ΄κ΅°" -> setDongAdapter(2982, 2992, district)
                                    "μ₯μ±κ΅°" -> setDongAdapter(2994, 3004, district)
                                    "μλκ΅°" -> setDongAdapter(3006, 3017, district)
                                    "μ§λκ΅°" -> setDongAdapter(3019, 3025, district)
                                    "μ μκ΅°" -> setDongAdapter(3027, 3040, district)
                                    else -> {
                                        selectedDistrict[1] = ""
                                        setMuAdapter(R.array.mu)
                                    }
                                }
                            }
                            "κ²½μλΆλ" -> {
                                selectedDistrict[1] =
                                    binding.sigungu.getItemAtPosition(position) as String
                                selectedDistrict[2] = ""
                                when (binding.sigungu.getItemAtPosition(position)) {
                                    "ν¬ν­μ λ¨κ΅¬" -> setDongAdapter(3043, 3056, district)
                                    "ν¬ν­μ λΆκ΅¬" -> setDongAdapter(3058, 3072, district)
                                    "κ²½μ£Όμ" -> setDongAdapter(3074, 3096, district)
                                    "κΉμ²μ" -> setDongAdapter(3098, 3119, district)
                                    "μλμ" -> setDongAdapter(3121, 3144, district)
                                    "κ΅¬λ―Έμ" -> setDongAdapter(3146, 3171, district)
                                    "μμ£Όμ" -> setDongAdapter(3173, 3191, district)
                                    "μμ²μ" -> setDongAdapter(3193, 3208, district)
                                    "μμ£Όμ" -> setDongAdapter(3210, 3233, district)
                                    "λ¬Έκ²½μ" -> setDongAdapter(3235, 3248, district)
                                    "κ²½μ°μ" -> setDongAdapter(3250, 3264, district)
                                    "κ΅°μκ΅°" -> setDongAdapter(3266, 3273, district)
                                    "μμ±κ΅°" -> setDongAdapter(3275, 3292, district)
                                    "μ²­μ‘κ΅°" -> setDongAdapter(3294, 3301, district)
                                    "μμκ΅°" -> setDongAdapter(3303, 3308, district)
                                    "μλκ΅°" -> setDongAdapter(3310, 3318, district)
                                    "μ²­λκ΅°" -> setDongAdapter(3320, 3328, district)
                                    "κ³ λ Ήκ΅°" -> setDongAdapter(3330, 3337, district)
                                    "μ±μ£Όκ΅°" -> setDongAdapter(3339, 3348, district)
                                    "μΉ κ³‘κ΅°" -> setDongAdapter(3350, 3357, district)
                                    "μμ²κ΅°" -> setDongAdapter(3359, 3370, district)
                                    "λ΄νκ΅°" -> setDongAdapter(3372, 3381, district)
                                    "μΈμ§κ΅°" -> setDongAdapter(3383, 3392, district)
                                    "μΈλ¦κ΅°" -> setDongAdapter(3394, 3397, district)
                                    else -> {
                                        selectedDistrict[1] = ""
                                        setMuAdapter(R.array.mu)
                                    }
                                }
                            }
                            "κ²½μλ¨λ" -> {
                                selectedDistrict[1] =
                                    binding.sigungu.getItemAtPosition(position) as String
                                selectedDistrict[2] = ""
                                when (binding.sigungu.getItemAtPosition(position)) {
                                    "μ°½μμ μμ°½κ΅¬" -> setDongAdapter(3400, 3407, district)
                                    "μ°½μμ μ±μ°κ΅¬" -> setDongAdapter(3409, 3415, district)
                                    "μ°½μμ λ§μ°ν©ν¬κ΅¬" -> setDongAdapter(3417, 3431, district)
                                    "μ°½μμ λ§μ°νμκ΅¬" -> setDongAdapter(3433, 3444, district)
                                    "μ°½μμ μ§ν΄κ΅¬" -> setDongAdapter(3446, 3458, district)
                                    "μ§μ£Όμ" -> setDongAdapter(3460, 3489, district)
                                    "ν΅μμ" -> setDongAdapter(3491, 3505, district)
                                    "μ¬μ²μ" -> setDongAdapter(3507, 3520, district)
                                    "κΉν΄μ" -> setDongAdapter(3522, 3540, district)
                                    "λ°μμ" -> setDongAdapter(3542, 3557, district)
                                    "κ±°μ μ" -> setDongAdapter(3559, 3576, district)
                                    "μμ°μ" -> setDongAdapter(3578, 3590, district)
                                    "μλ Ήκ΅°" -> setDongAdapter(3592, 3604, district)
                                    "ν¨μκ΅°" -> setDongAdapter(3606, 3615, district)
                                    "μ°½λκ΅°" -> setDongAdapter(3617, 3630, district)
                                    "κ³ μ±κ΅°" -> setDongAdapter(3632, 3645, district)
                                    "λ¨ν΄κ΅°" -> setDongAdapter(3647, 3656, district)
                                    "νλκ΅°" -> setDongAdapter(3658, 3670, district)
                                    "μ°μ²­κ΅°" -> setDongAdapter(3672, 3682, district)
                                    "ν¨μκ΅°" -> setDongAdapter(3684, 3694, district)
                                    "κ±°μ°½κ΅°" -> setDongAdapter(3696, 3707, district)
                                    "ν©μ²κ΅°" -> setDongAdapter(3709, 3725, district)
                                    else -> {
                                        selectedDistrict[1] = ""
                                        setMuAdapter(R.array.mu)
                                    }
                                }
                            }
                            "μ μ£ΌνΉλ³μμΉλ" -> {
                                selectedDistrict[1] =
                                    binding.sigungu.getItemAtPosition(position) as String
                                selectedDistrict[2] = ""
                                when (binding.sigungu.getItemAtPosition(position)) {
                                    "μ μ£Όμ" -> setDongAdapter(3728, 3753, district)
                                    "μκ·ν¬μ" -> setDongAdapter(3755, 3771, district)
                                    else -> {
                                        selectedDistrict[1] = ""
                                        setMuAdapter(R.array.mu)
                                    }
                                }
                            }
                            else -> {
                                selectedDistrict[1] = ""
                                selectedDistrict[2] = ""
                            }
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                    }
                }

            binding.dong.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedDistrict[2] = binding.dong.getItemAtPosition(position) as String
                    if (binding.dong.getItemAtPosition(position) == "μ/λ©΄/λ") selectedDistrict[2] = ""
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }

            binding.nextButton.setOnClickListener {
                if (selectedDistrict[0] == "") {
                    Toast.makeText(application, "μμΉλ₯Ό μ€μ ν΄μ£ΌμΈμ", Toast.LENGTH_SHORT).show()
                } else if (binding.locationName.text.toString() == "") {
                    Toast.makeText(application, "μμΉ μ΄λ¦μ μμ±ν΄μ£ΌμΈμ.", Toast.LENGTH_SHORT).show()
                } else {
                    CoroutineScope(Dispatchers.IO).launch {
                        val getDistrict = db.districtDao.get(
                            selectedDistrict[0],
                            selectedDistrict[1],
                            selectedDistrict[2]
                        )

                        val sp = getSharedPreferences("currentLocation", Context.MODE_PRIVATE)

                        val lc = Loc(
                            lcName = binding.locationName.text.toString(),
                            name1 = selectedDistrict[0],
                            name2 = selectedDistrict[1],
                            name3 = selectedDistrict[2],
                            x = getDistrict?.x ?: 60,
                            y = getDistrict?.y ?: 127
                        )
                        db.locDao.insert(lc)

                        val lastId = db.locDao.getLast().id

                        val editor = sp.edit()
                        editor.putLong("currId", lastId)
                        editor.putString("currName", binding.locationName.text.toString())
                        editor.putInt("nx", getDistrict?.x ?: 0)
                        editor.putInt("ny", getDistrict?.y ?: 0)
                        editor.commit()
                    }
                    val sharedPreferences = getSharedPreferences("visitFirst", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putBoolean("first", false)
                    editor.commit()

                    finish()
                }
            }

            binding.saveButton.setOnClickListener {
                if (selectedDistrict[0] == "") {
                    Toast.makeText(application, "μμΉλ₯Ό μ€μ ν΄μ£ΌμΈμ", Toast.LENGTH_SHORT).show()
                } else if (binding.locationName.text.toString() == "") {
                    Toast.makeText(application, "μμΉ μ΄λ¦μ μμ±ν΄μ£ΌμΈμ.", Toast.LENGTH_SHORT).show()
                } else {
                    if (start == "locationList") { // ADDλ‘ λ€μ΄μ΄
                        CoroutineScope(Dispatchers.IO).launch {
                            val getDistrict = db.districtDao.get(
                                selectedDistrict[0],
                                selectedDistrict[1],
                                selectedDistrict[2]
                            )

                            val sp = getSharedPreferences("currentLocation", Context.MODE_PRIVATE)
                            val currId = sp.getLong("currId", 0)

                            if (currId != 0L) {
                                val currLoc = db.locDao.get(currId)
                                currLoc.current = 0
                                db.locDao.update(currLoc)
                            }

                            val lc = Loc(
                                lcName = binding.locationName.text.toString(),
                                name1 = selectedDistrict[0],
                                name2 = selectedDistrict[1],
                                name3 = selectedDistrict[2],
                                x = getDistrict?.x ?: 60,
                                y = getDistrict?.y ?: 127
                            )
                            db.locDao.insert(lc)

                            val lastId = db.locDao.getLast().id

                            val editor = sp.edit()
                            editor.putLong("currId", lastId)
                            editor.putString("currName", binding.locationName.text.toString())
                            editor.putInt("nx", getDistrict?.x ?: 0)
                            editor.putInt("ny", getDistrict?.y ?: 0)
                            editor.commit()
                        }
                        Toast.makeText(application, "μμΉλ₯Ό μΆκ°νμ΅λλ€.", Toast.LENGTH_SHORT).show()
                    } else if (start == "locationDetail") { // μμ μΌλ‘ λ€μ΄μ΄
                        CoroutineScope(Dispatchers.IO).launch {
                            val getDistrict = db.districtDao.get(
                                selectedDistrict[0],
                                selectedDistrict[1],
                                selectedDistrict[2]
                            )

                            val selectedId = intent.getLongExtra("id", -1)

                            val sp = getSharedPreferences("currentLocation", Context.MODE_PRIVATE)
                            val currId = sp.getLong("currId", 0)

                            val selectedLoc = db.locDao.get(selectedId)
                            selectedLoc.lcName = binding.locationName.text.toString()
                            selectedLoc.name1 = selectedDistrict[0]
                            selectedLoc.name2 = selectedDistrict[1]
                            selectedLoc.name3 = selectedDistrict[2]
                            selectedLoc.x = getDistrict?.x ?: 60
                            selectedLoc.y = getDistrict?.y ?: 127

                            db.locDao.update(selectedLoc)

                            if (currId == selectedId) {
                                val sp = getSharedPreferences("currentLocation", Context.MODE_PRIVATE)
                                val editor = sp.edit()
                                editor.putString("currName", binding.locationName.text.toString())
                                editor.putInt("nx", getDistrict?.x ?: 0)
                                editor.putInt("ny", getDistrict?.y ?: 0)
                                editor.commit()
                            }
                        }
                        Toast.makeText(application, "μμΉλ₯Ό μμ νμ΅λλ€.", Toast.LENGTH_SHORT).show()
                    }
                }
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
            binding.sigungu.adapter = adapter
        }

        setMuAdapter(R.array.mu)

        if (start == "locationDetail") {
            val name2 = intent.getStringExtra("name2") ?: ""

            if (name2 != "") {
                binding.sigungu.setSelection(getIndex(binding.sigungu, name2))
            }
        }
    }

    private fun setDongAdapter(strt: Int, end: Int, district: List<AdministrativeDistrict>) {
        val dongArray = ArrayList<String>()
        dongArray.add("μ/λ©΄/λ")
        for (i in strt..end) {
            dongArray.add(district[i].name3)
        }

        val arrayAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, dongArray)
        binding.dong.adapter = arrayAdapter

        if (start == "locationDetail") {
            val name3 = intent.getStringExtra("name3") ?: ""
            
            if (name3 != "") {
                binding.dong.setSelection(getIndex(binding.dong, name3))
            }
        }
    }

    private fun setMuAdapter(array: Int) {
        ArrayAdapter.createFromResource(
            this,
            array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.dong.adapter = adapter
        }
    }

    private fun getIndex(spinner: Spinner, name: String): Int {
        for (i in 0 until spinner.count) {
            if (spinner.getItemAtPosition(i).toString() == name) {
                return i
            }
        }
        return 0
    }
}