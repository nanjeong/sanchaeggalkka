package com.example.sanchaeggalkka

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.sanchaeggalkka.db.LocDao

class LocationListViewModel(database: LocDao, application: Application) : AndroidViewModel(application) {

    val locations = database.getAllLocation()
}