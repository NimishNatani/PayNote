package com.practicecoding.notetakingapp.model

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.practicecoding.notetakingapp.room.Noterepository

class Notefactory(val app:Application,
    private val noterepository: Noterepository):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return Noteviewmodel(app,noterepository)as T
    }
}