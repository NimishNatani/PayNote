package com.practicecoding.notetakingapp.room

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HideNoteRepository(private val db:HideNotedatabse) {

    suspend fun inserthidenote(note: NoteHide){
        withContext(Dispatchers.IO) {db.gethidenotedao().inserthidenote(note)}
    }

    suspend fun deletehidenote(note: NoteHide) {
        withContext(Dispatchers.IO) {db.gethidenotedao().hidenotedelete(note)}
    }

    suspend fun updatehidenote(note: NoteHide ){
        withContext(Dispatchers.IO) {db.gethidenotedao().updatehidenote(note)}
    }

    fun getallhidenotes() =db.gethidenotedao().getallnotesinhide()

    fun searchhidenote(query:String) =db.gethidenotedao().searchnoteinhide(query)
}