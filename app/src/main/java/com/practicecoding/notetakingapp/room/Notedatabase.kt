package com.practicecoding.notetakingapp.room

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Note::class],version =3
    , autoMigrations = [AutoMigration(from = 1, to = 2)
                                                                ,AutoMigration(from = 2, to = 3)]
//                                                                ,
//    AutoMigration(from = 3, to = 4)]
    )
abstract class Notedatabase:RoomDatabase() {

    abstract fun getnotedao():NoteDao
    companion object {
        @Volatile
        private var instance: Notedatabase? = null
        private val Lock = Any()
        operator fun invoke(context: Context) = instance ?: synchronized(Lock)
        {
            instance ?: createdatabase(context).also {
                instance = it
            }
        }

        private fun createdatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext, Notedatabase::class.java,
            "note_db2"
        ).build()


    }


}