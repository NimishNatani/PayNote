package com.practicecoding.notetakingapp.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.practicecoding.notetakingapp.room.Note
import com.practicecoding.notetakingapp.room.Noterepository
import kotlinx.coroutines.launch

class Noteviewmodel(app:Application,private val noterepository:Noterepository):AndroidViewModel(app) {
    fun addNote(note: Note)= viewModelScope.launch {  noterepository.insertnote(note)}
    fun deleteNote(note: Note)= viewModelScope.launch {noterepository.deletenote(note)}
    fun updateNote(note: Note)= viewModelScope.launch {noterepository.updatenote(note)}
    fun getallNotes() =noterepository.getallnotes()
    fun searchNote(qyery:String) = noterepository.searchnote(qyery)
}