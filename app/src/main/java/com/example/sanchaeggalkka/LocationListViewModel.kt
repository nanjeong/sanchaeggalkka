package com.example.sanchaeggalkka

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.sanchaeggalkka.db.LocDao

class LocationListViewModel(dataSource: LocDao, application: Application) :
    AndroidViewModel(application) {

    val database = dataSource

    val locations = database.getAllLocation()

    fun onLocationClicked(id: Long) {
        _navigateToLocationDetail.value = id
    }

    private val _navigateToLocationDetail = MutableLiveData<Long?>()
    val navigateToLocationDetail
        get() = _navigateToLocationDetail
}