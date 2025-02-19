package com.example.notesapp.ui.theme.EditNote.Viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.notesapp.data.local.AppDatabase
import com.example.notesapp.data.models.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditNoteViewModel (val app: Application) : AndroidViewModel(app) {

    val _editNote: MutableLiveData<Unit> = MutableLiveData()
    val editNote: LiveData<Unit> = _editNote
    val _deleteNote: MutableLiveData<Unit> = MutableLiveData()
    val deleteNote: LiveData<Unit> = _deleteNote

    fun editNote(note: Note){
        viewModelScope.launch(Dispatchers.IO) {
            val noteDao = AppDatabase.DatabaseBuilder.getInstance(app.applicationContext).noteDao()

            val result = noteDao.updateNote(note)
            withContext(Dispatchers.Main){
                _editNote.postValue(result)
            }
        }
    }
    fun deleteNote(note: Note){
        viewModelScope.launch(Dispatchers.IO) {
            val noteDao = AppDatabase.DatabaseBuilder.getInstance(app.applicationContext).noteDao()

            val result = noteDao.deleteNote(note)
            withContext(Dispatchers.Main){
                _deleteNote.postValue(result)
            }
        }
    }
}