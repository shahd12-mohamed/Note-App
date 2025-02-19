package com.example.notesapp.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.notesapp.data.models.Note

@Dao
interface NoteDao {
    @Query("SELECT * FROM notes")
    fun getAllNotes(): List<Note> //read
    @Insert
    suspend fun insertNote(note: Note) //create
    @Update
    suspend fun updateNote(note: Note) //update
    @Delete
    suspend fun deleteNote(note: Note) //delete
}