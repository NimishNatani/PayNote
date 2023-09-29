package com.practicecoding.notetakingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.practicecoding.notetakingapp.databinding.ActivityMainBinding
import com.practicecoding.notetakingapp.model.HideNotefactory
import com.practicecoding.notetakingapp.model.HideNoteviewmodel
import com.practicecoding.notetakingapp.model.Notefactory
import com.practicecoding.notetakingapp.model.Noteviewmodel
import com.practicecoding.notetakingapp.room.HideNoteRepository
import com.practicecoding.notetakingapp.room.HideNotedatabse
import com.practicecoding.notetakingapp.room.Notedatabase
import com.practicecoding.notetakingapp.room.Noterepository

class MainActivity2 : AppCompatActivity() {
    lateinit var hideNoteviewmodel: HideNoteviewmodel
    lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupviewmodel()}

    private fun setupviewmodel() {
        val hidenoterepository = HideNoteRepository(HideNotedatabse(this))
        val hideviewmodelproviderfactory = HideNotefactory(application,hidenoterepository)

        hideNoteviewmodel = ViewModelProvider(this,hideviewmodelproviderfactory).get(HideNoteviewmodel::class.java)
    }
}