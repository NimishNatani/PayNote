package com.practicecoding.notetakingapp.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.practicecoding.notetakingapp.room.HideNoteRepository
import com.practicecoding.notetakingapp.room.Note
import com.practicecoding.notetakingapp.room.NoteHide
import kotlinx.coroutines.launch

class HideNoteviewmodel(app: Application, private val notehiderepository: HideNoteRepository): AndroidViewModel(app) {
    fun addhideNote(note: NoteHide)= viewModelScope.launch {  notehiderepository.inserthidenote(note)}
    fun deletehideNote(note: NoteHide)= viewModelScope.launch {notehiderepository.deletehidenote(note)}
    fun updatehideNote(note: NoteHide)= viewModelScope.launch {notehiderepository.updatehidenote(note)}
    fun getallhideNotes() =notehiderepository.getallhidenotes()
    fun searchhideNote(qyery:String) = notehiderepository.searchhidenote(qyery)
}