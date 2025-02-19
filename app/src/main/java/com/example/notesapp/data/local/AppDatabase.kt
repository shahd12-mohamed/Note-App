package com.example.notesapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.notesapp.data.models.Note

@Database(entities = [Note::class], version = 1)
abstract class AppDatabase : RoomDatabase(){
    abstract fun noteDao(): NoteDao
    object DatabaseBuilder {
        private var INSTANCE: AppDatabase? = null
        fun getInstance(applicationContext: Context): AppDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    applicationContext,
                    AppDatabase::class.java,
                    "note_db"
                ).build()
            }
            return INSTANCE!!
        }
    }
}