package com.practicecoding.notetakingapp.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface NotedaoHide {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun inserthidenote(notes:NoteHide)

    @Update
    fun updatehidenote(notes:NoteHide)

    @Delete
    fun hidenotedelete(notes:NoteHide)

    @Query("SELECT * FROM hidenotes ORDER BY id DESC")
    fun getallnotesinhide(): LiveData<List<NoteHide>>

    @Query("SELECT * FROM hidenotes WHERE noteTitle LIKE '%' || :query || '%' OR noteBody LIKE '%' || :query || '%' ")
//            "OR paynote LIKE '%' || :query || '%'")
    fun searchnoteinhide(query:String): LiveData<List<NoteHide>>
}