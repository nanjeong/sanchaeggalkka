package com.example.sanchaeggalkka

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sanchaeggalkka.db.LocDao
import java.lang.IllegalArgumentException

class LocationListViewModelFactory(
    private val dataSource: LocDao,
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LocationListViewModel::class.java)) {
            return LocationListViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}