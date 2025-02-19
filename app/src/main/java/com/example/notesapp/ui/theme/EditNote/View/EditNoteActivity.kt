package com.example.notesapp.ui.theme.EditNote.View

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.notesapp.R
import com.example.notesapp.data.models.Note
import com.example.notesapp.databinding.ActivityEditNoteBinding
import com.example.notesapp.ui.theme.EditNote.Viewmodel.EditNoteViewModel
import com.google.android.material.snackbar.Snackbar

class EditNoteActivity : AppCompatActivity() {

    lateinit var binding: ActivityEditNoteBinding
    val viewModel: EditNoteViewModel by viewModels()

    private var noteId: Int = -1  // initialize id

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_note)

        //receive data from intent
        noteId = intent.getIntExtra("note_id", -1)
        val noteTitle = intent.getStringExtra("note_title")
        val noteContent = intent.getStringExtra("note_content")

        // display the note data
        binding.etTitle.setText(noteTitle ?: "")
        binding.etNote.setText(noteContent ?: "")

        // when the user clicks on the edit button
        binding.btEdit.setOnClickListener {
            if (!binding.etTitle.text.isNullOrEmpty() && !binding.etNote.text.isNullOrEmpty()) {
                val updatedNote = Note(
                    id = noteId,  // update the note with the same id
                    title = binding.etTitle.text.toString(),
                    content = binding.etNote.text.toString()
                )
                viewModel.editNote(updatedNote)
            }
        }

        // when the user clicks on the delete button
        binding.ivDelete.setOnClickListener {
            showDeleteConfirmationDialog()
        }

        // observe the editNote and deleteNote LiveData
        viewModel.editNote.observe(this, Observer {
            binding.btEdit.hideKeyboard()
            Snackbar.make(binding.root, R.string.note_edited, Snackbar.LENGTH_LONG)
                .setAction(R.string.dismiss) {
                    finish()
                }.show()
        })

        // observe the deleteNote LiveData
        viewModel.deleteNote.observe(this, Observer {
            Snackbar.make(binding.root, R.string.note_deleted, Snackbar.LENGTH_LONG)
                .setAction(R.string.dismiss) {
                    finish()
                }.show()
        })
    }

    // display a dialog to confirm the deletion of the note
    private fun showDeleteConfirmationDialog() {
        val alertDialogBuilder = AlertDialog.Builder(this)
            .setMessage(R.string.delete_note_message)
            .setCancelable(false)
            .setPositiveButton(R.string.confirm_delete) { dialog, _ ->
                viewModel.deleteNote(Note(id = noteId, title = "", content = "")) // delete the note
                dialog.dismiss()
            }
            .setNegativeButton(R.string.cancel_delete) { dialog, _ ->
                dialog.dismiss()
            }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    // hide the keyboard
    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}
