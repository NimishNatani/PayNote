package com.practicecoding.notetakingapp.room

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Noterepository(private val db:Notedatabase) {

     suspend fun insertnote(note: Note){
         withContext(Dispatchers.IO) {db.getnotedao().insertnote(note)}
     }

    suspend fun deletenote(note: Note) {
        withContext(Dispatchers.IO) {db.getnotedao().notedelete(note)}
    }

    suspend fun updatenote(note: Note ){
        withContext(Dispatchers.IO) {db.getnotedao().updatenote(note)}
    }

    fun getallnotes() =db.getnotedao().getallnotes()

    fun searchnote(query:String) =db.getnotedao().searchnote(query)
}