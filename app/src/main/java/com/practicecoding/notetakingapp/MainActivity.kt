package com.practicecoding.notetakingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.practicecoding.notetakingapp.databinding.ActivityMainBinding
import com.practicecoding.notetakingapp.model.Notefactory
import com.practicecoding.notetakingapp.model.Noteviewmodel
import com.practicecoding.notetakingapp.room.Notedatabase
import com.practicecoding.notetakingapp.room.Noterepository

class MainActivity : AppCompatActivity() {
     lateinit var noteViewmodel:Noteviewmodel
      lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)
   setupviewmodel()}

    private fun setupviewmodel() {
        val noteRepository = Noterepository(Notedatabase(this))
        val viewmodelproviderfactory = Notefactory(application,noteRepository)
        noteViewmodel = ViewModelProvider(
            this,
            viewmodelproviderfactory)
            .get(Noteviewmodel::class.java)
    }

}