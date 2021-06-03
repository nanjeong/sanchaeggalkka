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
            binding.pageName.text = "위치 수정"

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
                            "서울특별시" -> setSigunguAdapter(R.array.seoul_array)
                            "부산광역시" -> setSigunguAdapter(R.array.busan_array)
                            "대구광역시" -> setSigunguAdapter(R.array.daegu_array)
                            "인천광역시" -> setSigunguAdapter(R.array.incheon_array)
                            "광주광역시" -> setSigunguAdapter(R.array.gwangju_array)
                            "대전광역시" -> setSigunguAdapter(R.array.daejeon_array)
                            "울산광역시" -> setSigunguAdapter(R.array.ulsan_array)
                            "세종특별자치시" -> setSigunguAdapter(R.array.sejong_array)
                            "경기도" -> setSigunguAdapter(R.array.gyeonggi_array)
                            "강원도" -> setSigunguAdapter(R.array.gangwon_array)
                            "충청북도" -> setSigunguAdapter(R.array.chungcheongbuk_array)
                            "충청남도" -> setSigunguAdapter(R.array.chungcheongnam_array)
                            "전라북도" -> setSigunguAdapter(R.array.jeollabuk_array)
                            "전라남도" -> setSigunguAdapter(R.array.jeollanam_array)
                            "경상북도" -> setSigunguAdapter(R.array.gyeongsangbuk_array)
                            "경상남도" -> setSigunguAdapter(R.array.gyeongsangnam_array)
                            "제주특별자치도" -> setSigunguAdapter(R.array.jeju_array)
                            "이어도" -> {
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
                            "서울특별시" -> {
                                selectedDistrict[1] =
                                    binding.sigungu.getItemAtPosition(position) as String
                                selectedDistrict[2] = ""
                                when (binding.sigungu.getItemAtPosition(position)) {
                                    "종로구" -> setDongAdapter(2, 18, district)
                                    "중구" -> setDongAdapter(20, 34, district)
                                    "용산구" -> setDongAdapter(36, 51, district)
                                    "성동구" -> setDongAdapter(53, 69, district)
                                    "광진구" -> setDongAdapter(71, 85, district)
                                    "동대문구" -> setDongAdapter(87, 100, district)
                                    "중랑구" -> setDongAdapter(102, 117, district)
                                    "성북구" -> setDongAdapter(119, 138, district)
                                    "강북구" -> setDongAdapter(140, 152, district)
                                    "도봉구" -> setDongAdapter(154, 167, district)
                                    "노원구" -> setDongAdapter(169, 187, district)
                                    "은평구" -> setDongAdapter(189, 204, district)
                                    "서대문구" -> setDongAdapter(206, 219, district)
                                    "마포구" -> setDongAdapter(221, 236, district)
                                    "양천구" -> setDongAdapter(238, 255, district)
                                    "강서구" -> setDongAdapter(257, 276, district)
                                    "구로구" -> setDongAdapter(278, 293, district)
                                    "금천구" -> setDongAdapter(295, 304, district)
                                    "영등포구" -> setDongAdapter(306, 323, district)
                                    "동작구" -> setDongAdapter(325, 339, district)
                                    "관악구" -> setDongAdapter(341, 361, district)
                                    "서초구" -> setDongAdapter(363, 380, district)
                                    "강남구" -> setDongAdapter(382, 403, district)
                                    "송파구" -> setDongAdapter(405, 431, district)
                                    "강동구" -> setDongAdapter(433, 450, district)
                                    else -> {
                                        selectedDistrict[1] = ""
                                        setMuAdapter(R.array.mu)
                                    }
                                }
                            }
                            "부산광역시" -> {
                                selectedDistrict[1] =
                                    binding.sigungu.getItemAtPosition(position) as String
                                selectedDistrict[2] = ""
                                when (binding.sigungu.getItemAtPosition(position)) {
                                    "중구" -> setDongAdapter(453, 461, district)
                                    "서구" -> setDongAdapter(463, 475, district)
                                    "동구" -> setDongAdapter(477, 488, district)
                                    "영도구" -> setDongAdapter(490, 500, district)
                                    "부산진구" -> setDongAdapter(502, 521, district)
                                    "동래구" -> setDongAdapter(523, 535, district)
                                    "남구" -> setDongAdapter(537, 553, district)
                                    "북구" -> setDongAdapter(555, 567, district)
                                    "해운대구" -> setDongAdapter(569, 586, district)
                                    "사하구" -> setDongAdapter(588, 603, district)
                                    "금정구" -> setDongAdapter(605, 620, district)
                                    "강서구" -> setDongAdapter(622, 629, district)
                                    "연제구" -> setDongAdapter(631, 642, district)
                                    "수영구" -> setDongAdapter(644, 653, district)
                                    "사상구" -> setDongAdapter(655, 666, district)
                                    "기장군" -> setDongAdapter(668, 672, district)
                                    else -> {
                                        selectedDistrict[1] = ""
                                        setMuAdapter(R.array.mu)
                                    }
                                }
                            }
                            "대구광역시" -> {
                                selectedDistrict[1] =
                                    binding.sigungu.getItemAtPosition(position) as String
                                selectedDistrict[2] = ""
                                when (binding.sigungu.getItemAtPosition(position)) {
                                    "중구" -> setDongAdapter(675, 686, district)
                                    "동구" -> setDongAdapter(688, 710, district)
                                    "서구" -> setDongAdapter(712, 728, district)
                                    "남구" -> setDongAdapter(730, 742, district)
                                    "북구" -> setDongAdapter(744, 766, district)
                                    "수성구" -> setDongAdapter(768, 790, district)
                                    "달서구" -> setDongAdapter(792, 813, district)
                                    "달성군" -> setDongAdapter(815, 823, district)
                                    else -> {
                                        selectedDistrict[1] = ""
                                        setMuAdapter(R.array.mu)
                                    }
                                }
                            }
                            "인천광역시" -> {
                                selectedDistrict[1] =
                                    binding.sigungu.getItemAtPosition(position) as String
                                selectedDistrict[2] = ""
                                when (binding.sigungu.getItemAtPosition(position)) {
                                    "중구" -> setDongAdapter(826, 837, district)
                                    "동구" -> setDongAdapter(839, 849, district)
                                    "미추홀구" -> setDongAdapter(851, 871, district)
                                    "연수구" -> setDongAdapter(873, 887, district)
                                    "남동구" -> setDongAdapter(889, 908, district)
                                    "부평구" -> setDongAdapter(910, 931, district)
                                    "계양구" -> setDongAdapter(933, 944, district)
                                    "서구" -> setDongAdapter(946, 967, district)
                                    "강화군" -> setDongAdapter(969, 981, district)
                                    "옹진군" -> setDongAdapter(983, 989, district)
                                    else -> {
                                        selectedDistrict[1] = ""
                                        setMuAdapter(R.array.mu)
                                    }
                                }
                            }
                            "광주광역시" -> {
                                selectedDistrict[1] =
                                    binding.sigungu.getItemAtPosition(position) as String
                                selectedDistrict[2] = ""
                                when (binding.sigungu.getItemAtPosition(position)) {
                                    "동구" -> setDongAdapter(992, 1004, district)
                                    "서구" -> setDongAdapter(1006, 1023, district)
                                    "남구" -> setDongAdapter(1025, 1040, district)
                                    "북구" -> setDongAdapter(1042, 1069, district)
                                    "광산구" -> setDongAdapter(1071, 1091, district)
                                    else -> {
                                        selectedDistrict[1] = ""
                                        setMuAdapter(R.array.mu)
                                    }
                                }
                            }
                            "대전광역시" -> {
                                selectedDistrict[1] =
                                    binding.sigungu.getItemAtPosition(position) as String
                                selectedDistrict[2] = ""
                                when (binding.sigungu.getItemAtPosition(position)) {
                                    "동구" -> setDongAdapter(1094, 1109, district)
                                    "중구" -> setDongAdapter(1111, 1127, district)
                                    "서구" -> setDongAdapter(1129, 1151, district)
                                    "유성구" -> setDongAdapter(1153, 1163, district)
                                    "대덕구" -> setDongAdapter(1165, 1176, district)
                                    else -> {
                                        selectedDistrict[1] = ""
                                        setMuAdapter(R.array.mu)
                                    }
                                }
                            }
                            "울산광역시" -> {
                                selectedDistrict[1] =
                                    binding.sigungu.getItemAtPosition(position) as String
                                selectedDistrict[2] = ""
                                when (binding.sigungu.getItemAtPosition(position)) {
                                    "중구" -> setDongAdapter(1179, 1191, district)
                                    "남구" -> setDongAdapter(1193, 1206, district)
                                    "동구" -> setDongAdapter(1208, 1216, district)
                                    "북구" -> setDongAdapter(1218, 1225, district)
                                    "울주군" -> setDongAdapter(1227, 1238, district)
                                    else -> {
                                        selectedDistrict[1] = ""
                                        setMuAdapter(R.array.mu)
                                    }
                                }
                            }
                            "세종특별자치시" -> {
                                selectedDistrict[1] =
                                    binding.sigungu.getItemAtPosition(position) as String
                                selectedDistrict[2] = ""
                                when (binding.sigungu.getItemAtPosition(position)) {
                                    "세종특별자치시" -> setDongAdapter(1241, 1260, district)
                                    else -> {
                                        selectedDistrict[1] = ""
                                        setMuAdapter(R.array.mu)
                                    }
                                }
                            }
                            "경기도" -> {
                                selectedDistrict[1] =
                                    binding.sigungu.getItemAtPosition(position) as String
                                selectedDistrict[2] = ""
                                when (binding.sigungu.getItemAtPosition(position)) {
                                    "수원시 장안구" -> setDongAdapter(1263, 1272, district)
                                    "수원시 권선구" -> setDongAdapter(1274, 1285, district)
                                    "수원시 팔달구" -> setDongAdapter(1287, 1296, district)
                                    "수원시 영통구" -> setDongAdapter(1298, 1309, district)
                                    "성남시 수정구" -> setDongAdapter(1311, 1327, district)
                                    "성남시 중원구" -> setDongAdapter(1329, 1339, district)
                                    "성남시 분당구" -> setDongAdapter(1341, 1362, district)
                                    "의정부시" -> setDongAdapter(1364, 1377, district)
                                    "안양시 만안구" -> setDongAdapter(1379, 1392, district)
                                    "안양시 동안구" -> setDongAdapter(1394, 1410, district)
                                    "부천시" -> setDongAdapter(1412, 1421, district)
                                    "광명시" -> setDongAdapter(1423, 1440, district)
                                    "평택시" -> setDongAdapter(1442, 1464, district)
                                    "동두천시" -> setDongAdapter(1466, 1473, district)
                                    "안산시 상록구" -> setDongAdapter(1475, 1487, district)
                                    "안산시 단원구" -> setDongAdapter(1489, 1500, district)
                                    "고양시 덕양구" -> setDongAdapter(1502, 1520, district)
                                    "고양시 일산동구" -> setDongAdapter(1522, 1532, district)
                                    "고양시 일산서구" -> setDongAdapter(1534, 1542, district)
                                    "과천시" -> setDongAdapter(1544, 1549, district)
                                    "구리시" -> setDongAdapter(1551, 1558, district)
                                    "남양주시" -> setDongAdapter(1560, 1575, district)
                                    "오산시" -> setDongAdapter(1577, 1582, district)
                                    "시흥시" -> setDongAdapter(1584, 1601, district)
                                    "군포시" -> setDongAdapter(1603, 1613, district)
                                    "의왕시" -> setDongAdapter(1615, 1620, district)
                                    "하남시" -> setDongAdapter(1622, 1635, district)
                                    "용인시 처인구" -> setDongAdapter(1637, 1647, district)
                                    "용인시 기흥구" -> setDongAdapter(1649, 1665, district)
                                    "용인시 수지구" -> setDongAdapter(1667, 1675, district)
                                    "파주시" -> setDongAdapter(1677, 1696, district)
                                    "이천시" -> setDongAdapter(1698, 1711, district)
                                    "안성시" -> setDongAdapter(1713, 1727, district)
                                    "김포시" -> setDongAdapter(1729, 1742, district)
                                    "화성시" -> setDongAdapter(1744, 1771, district)
                                    "광주시" -> setDongAdapter(1773, 1785, district)
                                    "양주시" -> setDongAdapter(1787, 1797, district)
                                    "포천시" -> setDongAdapter(1799, 1812, district)
                                    "여주시" -> setDongAdapter(1814, 1825, district)
                                    "연천군" -> setDongAdapter(1827, 1836, district)
                                    "가평군" -> setDongAdapter(1838, 1843, district)
                                    "양평군" -> setDongAdapter(1845, 1856, district)
                                    else -> {
                                        selectedDistrict[1] = ""
                                        setMuAdapter(R.array.mu)
                                    }
                                }
                            }
                            "강원도" -> {
                                selectedDistrict[1] =
                                    binding.sigungu.getItemAtPosition(position) as String
                                selectedDistrict[2] = ""
                                when (binding.sigungu.getItemAtPosition(position)) {
                                    "춘천시" -> setDongAdapter(1859, 1883, district)
                                    "원주시" -> setDongAdapter(1885, 1909, district)
                                    "강릉시" -> setDongAdapter(1911, 1931, district)
                                    "동해시" -> setDongAdapter(1933, 1942, district)
                                    "태백시" -> setDongAdapter(1944, 1951, district)
                                    "속초시" -> setDongAdapter(1953, 1960, district)
                                    "삼척시" -> setDongAdapter(1962, 1973, district)
                                    "홍천군" -> setDongAdapter(1975, 1984, district)
                                    "횡성군" -> setDongAdapter(1986, 1994, district)
                                    "영월군" -> setDongAdapter(1996, 2004, district)
                                    "평창군" -> setDongAdapter(2006, 2013, district)
                                    "정선군" -> setDongAdapter(2015, 2023, district)
                                    "철원군" -> setDongAdapter(2025, 2035, district)
                                    "화천군" -> setDongAdapter(2037, 2041, district)
                                    "양구군" -> setDongAdapter(2043, 2047, district)
                                    "인제군" -> setDongAdapter(2049, 2054, district)
                                    "고성군" -> setDongAdapter(2056, 2061, district)
                                    "양양군" -> setDongAdapter(2063, 2068, district)
                                    else -> {
                                        selectedDistrict[1] = ""
                                        setMuAdapter(R.array.mu)
                                    }
                                }
                            }
                            "충청북도" -> {
                                selectedDistrict[1] =
                                    binding.sigungu.getItemAtPosition(position) as String
                                selectedDistrict[2] = ""
                                when (binding.sigungu.getItemAtPosition(position)) {
                                    "청주시 상당구" -> setDongAdapter(2071, 2083, district)
                                    "청주시 서원구" -> setDongAdapter(2085, 2095, district)
                                    "청주시 흥덕구" -> setDongAdapter(2097, 2107, district)
                                    "청주시 청원구" -> setDongAdapter(2109, 2116, district)
                                    "충주시" -> setDongAdapter(2118, 2142, district)
                                    "제천시" -> setDongAdapter(2144, 2160, district)
                                    "보은군" -> setDongAdapter(2162, 2172, district)
                                    "옥천군" -> setDongAdapter(2174, 2182, district)
                                    "영동군" -> setDongAdapter(2184, 2194, district)
                                    "증평군" -> setDongAdapter(2196, 2197, district)
                                    "진천군" -> setDongAdapter(2199, 2205, district)
                                    "괴산군" -> setDongAdapter(2207, 2217, district)
                                    "음성군" -> setDongAdapter(2219, 2227, district)
                                    "단양군" -> setDongAdapter(2229, 2236, district)
                                    else -> {
                                        selectedDistrict[1] = ""
                                        setMuAdapter(R.array.mu)
                                    }
                                }
                            }
                            "충청남도" -> {
                                selectedDistrict[1] =
                                    binding.sigungu.getItemAtPosition(position) as String
                                selectedDistrict[2] = ""
                                when (binding.sigungu.getItemAtPosition(position)) {
                                    "천안시 동남구" -> setDongAdapter(2239, 2255, district)
                                    "천안시 서북구" -> setDongAdapter(2257, 2269, district)
                                    "공주시" -> setDongAdapter(2271, 2286, district)
                                    "보령시" -> setDongAdapter(2288, 2304, district)
                                    "아산시" -> setDongAdapter(2306, 2322, district)
                                    "서산시" -> setDongAdapter(2324, 2338, district)
                                    "논산시" -> setDongAdapter(2340, 2354, district)
                                    "계룡시" -> setDongAdapter(2356, 2359, district)
                                    "당진시" -> setDongAdapter(2361, 2374, district)
                                    "금산군" -> setDongAdapter(2376, 2385, district)
                                    "부여군" -> setDongAdapter(2387, 2402, district)
                                    "서천군" -> setDongAdapter(2404, 2416, district)
                                    "청양군" -> setDongAdapter(2418, 2427, district)
                                    "홍성군" -> setDongAdapter(2429, 2439, district)
                                    "예산군" -> setDongAdapter(2441, 2452, district)
                                    "태안군" -> setDongAdapter(2454, 2461, district)
                                    else -> {
                                        selectedDistrict[1] = ""
                                        setMuAdapter(R.array.mu)
                                    }
                                }
                            }
                            "전라북도" -> {
                                selectedDistrict[1] =
                                    binding.sigungu.getItemAtPosition(position) as String
                                selectedDistrict[2] = ""
                                when (binding.sigungu.getItemAtPosition(position)) {
                                    "전주시 완산구" -> setDongAdapter(2464, 2482, district)
                                    "전주시 덕진구" -> setDongAdapter(2484, 2499, district)
                                    "군산시" -> setDongAdapter(2501, 2527, district)
                                    "익산시" -> setDongAdapter(2529, 2557, district)
                                    "정읍시" -> setDongAdapter(2559, 2581, district)
                                    "남원시" -> setDongAdapter(2583, 2605, district)
                                    "김제시" -> setDongAdapter(2607, 2625, district)
                                    "완주군" -> setDongAdapter(2627, 2639, district)
                                    "진안군" -> setDongAdapter(2641, 2651, district)
                                    "무주군" -> setDongAdapter(2653, 2658, district)
                                    "장수군" -> setDongAdapter(2660, 2666, district)
                                    "임실군" -> setDongAdapter(2668, 2679, district)
                                    "순창군" -> setDongAdapter(2681, 2691, district)
                                    "고창군" -> setDongAdapter(2693, 2706, district)
                                    "부안군" -> setDongAdapter(2708, 2720, district)
                                    else -> {
                                        selectedDistrict[1] = ""
                                        setMuAdapter(R.array.mu)
                                    }
                                }
                            }
                            "전라남도" -> {
                                selectedDistrict[1] =
                                    binding.sigungu.getItemAtPosition(position) as String
                                selectedDistrict[2] = ""
                                when (binding.sigungu.getItemAtPosition(position)) {
                                    "목포시" -> setDongAdapter(2723, 2745, district)
                                    "여수시" -> setDongAdapter(2747, 2773, district)
                                    "순천시" -> setDongAdapter(2775, 2798, district)
                                    "나주시" -> setDongAdapter(2800, 2819, district)
                                    "광양시" -> setDongAdapter(2821, 2832, district)
                                    "담양군" -> setDongAdapter(2834, 2845, district)
                                    "곡성군" -> setDongAdapter(2847, 2857, district)
                                    "구례군" -> setDongAdapter(2859, 2866, district)
                                    "고흥군" -> setDongAdapter(2868, 2883, district)
                                    "보성군" -> setDongAdapter(2885, 2896, district)
                                    "화순군" -> setDongAdapter(2898, 2910, district)
                                    "장흥군" -> setDongAdapter(2912, 2921, district)
                                    "강진군" -> setDongAdapter(2923, 2933, district)
                                    "해남군" -> setDongAdapter(2935, 2948, district)
                                    "영암군" -> setDongAdapter(2950, 2960, district)
                                    "무안군" -> setDongAdapter(2962, 2970, district)
                                    "함평군" -> setDongAdapter(2972, 2980, district)
                                    "영광군" -> setDongAdapter(2982, 2992, district)
                                    "장성군" -> setDongAdapter(2994, 3004, district)
                                    "완도군" -> setDongAdapter(3006, 3017, district)
                                    "진도군" -> setDongAdapter(3019, 3025, district)
                                    "신안군" -> setDongAdapter(3027, 3040, district)
                                    else -> {
                                        selectedDistrict[1] = ""
                                        setMuAdapter(R.array.mu)
                                    }
                                }
                            }
                            "경상북도" -> {
                                selectedDistrict[1] =
                                    binding.sigungu.getItemAtPosition(position) as String
                                selectedDistrict[2] = ""
                                when (binding.sigungu.getItemAtPosition(position)) {
                                    "포항시 남구" -> setDongAdapter(3043, 3056, district)
                                    "포항시 북구" -> setDongAdapter(3058, 3072, district)
                                    "경주시" -> setDongAdapter(3074, 3096, district)
                                    "김천시" -> setDongAdapter(3098, 3119, district)
                                    "안동시" -> setDongAdapter(3121, 3144, district)
                                    "구미시" -> setDongAdapter(3146, 3171, district)
                                    "영주시" -> setDongAdapter(3173, 3191, district)
                                    "영천시" -> setDongAdapter(3193, 3208, district)
                                    "상주시" -> setDongAdapter(3210, 3233, district)
                                    "문경시" -> setDongAdapter(3235, 3248, district)
                                    "경산시" -> setDongAdapter(3250, 3264, district)
                                    "군위군" -> setDongAdapter(3266, 3273, district)
                                    "의성군" -> setDongAdapter(3275, 3292, district)
                                    "청송군" -> setDongAdapter(3294, 3301, district)
                                    "영양군" -> setDongAdapter(3303, 3308, district)
                                    "영덕군" -> setDongAdapter(3310, 3318, district)
                                    "청도군" -> setDongAdapter(3320, 3328, district)
                                    "고령군" -> setDongAdapter(3330, 3337, district)
                                    "성주군" -> setDongAdapter(3339, 3348, district)
                                    "칠곡군" -> setDongAdapter(3350, 3357, district)
                                    "예천군" -> setDongAdapter(3359, 3370, district)
                                    "봉화군" -> setDongAdapter(3372, 3381, district)
                                    "울진군" -> setDongAdapter(3383, 3392, district)
                                    "울릉군" -> setDongAdapter(3394, 3397, district)
                                    else -> {
                                        selectedDistrict[1] = ""
                                        setMuAdapter(R.array.mu)
                                    }
                                }
                            }
                            "경상남도" -> {
                                selectedDistrict[1] =
                                    binding.sigungu.getItemAtPosition(position) as String
                                selectedDistrict[2] = ""
                                when (binding.sigungu.getItemAtPosition(position)) {
                                    "창원시 의창구" -> setDongAdapter(3400, 3407, district)
                                    "창원시 성산구" -> setDongAdapter(3409, 3415, district)
                                    "창원시 마산합포구" -> setDongAdapter(3417, 3431, district)
                                    "창원시 마산회원구" -> setDongAdapter(3433, 3444, district)
                                    "창원시 진해구" -> setDongAdapter(3446, 3458, district)
                                    "진주시" -> setDongAdapter(3460, 3489, district)
                                    "통영시" -> setDongAdapter(3491, 3505, district)
                                    "사천시" -> setDongAdapter(3507, 3520, district)
                                    "김해시" -> setDongAdapter(3522, 3540, district)
                                    "밀양시" -> setDongAdapter(3542, 3557, district)
                                    "거제시" -> setDongAdapter(3559, 3576, district)
                                    "양산시" -> setDongAdapter(3578, 3590, district)
                                    "의령군" -> setDongAdapter(3592, 3604, district)
                                    "함안군" -> setDongAdapter(3606, 3615, district)
                                    "창녕군" -> setDongAdapter(3617, 3630, district)
                                    "고성군" -> setDongAdapter(3632, 3645, district)
                                    "남해군" -> setDongAdapter(3647, 3656, district)
                                    "하동군" -> setDongAdapter(3658, 3670, district)
                                    "산청군" -> setDongAdapter(3672, 3682, district)
                                    "함양군" -> setDongAdapter(3684, 3694, district)
                                    "거창군" -> setDongAdapter(3696, 3707, district)
                                    "합천군" -> setDongAdapter(3709, 3725, district)
                                    else -> {
                                        selectedDistrict[1] = ""
                                        setMuAdapter(R.array.mu)
                                    }
                                }
                            }
                            "제주특별자치도" -> {
                                selectedDistrict[1] =
                                    binding.sigungu.getItemAtPosition(position) as String
                                selectedDistrict[2] = ""
                                when (binding.sigungu.getItemAtPosition(position)) {
                                    "제주시" -> setDongAdapter(3728, 3753, district)
                                    "서귀포시" -> setDongAdapter(3755, 3771, district)
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
                    if (binding.dong.getItemAtPosition(position) == "읍/면/동") selectedDistrict[2] = ""
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }

            binding.nextButton.setOnClickListener {
                if (selectedDistrict[0] == "") {
                    Toast.makeText(application, "위치를 설정해주세요", Toast.LENGTH_SHORT).show()
                } else if (binding.locationName.text.toString() == "") {
                    Toast.makeText(application, "위치 이름을 작성해주세요.", Toast.LENGTH_SHORT).show()
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
                    Toast.makeText(application, "위치를 설정해주세요", Toast.LENGTH_SHORT).show()
                } else if (binding.locationName.text.toString() == "") {
                    Toast.makeText(application, "위치 이름을 작성해주세요.", Toast.LENGTH_SHORT).show()
                } else {
                    if (start == "locationList") { // ADD로 들어옴
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
                        Toast.makeText(application, "위치를 추가했습니다.", Toast.LENGTH_SHORT).show()
                    } else if (start == "locationDetail") { // 수정으로 들어옴
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
                        Toast.makeText(application, "위치를 수정했습니다.", Toast.LENGTH_SHORT).show()
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
        dongArray.add("읍/면/동")
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