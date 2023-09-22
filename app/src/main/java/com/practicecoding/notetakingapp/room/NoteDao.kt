package com.practicecoding.notetakingapp.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insertnote(note:Note)

    @Update
     fun updatenote(note:Note)

    @Delete
     fun notedelete(note:Note)

    @Query("SELECT * FROM NOTES ORDER BY id DESC")
    fun getallnotes():LiveData<List<Note>>

    @Query("SELECT * FROM notes WHERE noteTitle LIKE '%' || :query || '%' OR noteBody LIKE '%' || :query || '%' ")
//            "OR paynote LIKE '%' || :query || '%'")
    fun searchnote(query:String):LiveData<List<Note>>
}