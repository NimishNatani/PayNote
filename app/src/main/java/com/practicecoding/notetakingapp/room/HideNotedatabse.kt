package com.practicecoding.notetakingapp.room

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [NoteHide::class],version =1

)
abstract class HideNotedatabse: RoomDatabase() {

    abstract fun gethidenotedao():NotedaoHide
    companion object {
        @Volatile
        private var instance1: HideNotedatabse? = null
        private val Lock = Any()
        operator fun invoke(context: Context) = instance1 ?: synchronized(Lock)
        {
            instance1 ?: createdatabase(context).also {
                instance1 = it
            }
        }

        private fun createdatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext, HideNotedatabse::class.java,
            "note_db3"
        ).build()


    }


}