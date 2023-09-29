package com.practicecoding.notetakingapp.model

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.practicecoding.notetakingapp.room.HideNoteRepository

class HideNotefactory(val app: Application,
                  private val notehiderepository: HideNoteRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HideNoteviewmodel(app,notehiderepository)as T
    }
}