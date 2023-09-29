package com.practicecoding.notetakingapp.room

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "hidenotes")
@Parcelize
data class NoteHide(  @PrimaryKey(autoGenerate = true)
                      val id:Int,
                      val noteTitle:String,
                      val noteBody:String
                      ,
                      @ColumnInfo(name = "noteAmount", defaultValue = "0")
                      val noteAmount:String
                      ,
                      @ColumnInfo(name = "timeStamp", defaultValue = "0")
                      val timeStamp:String):Parcelable
