package com.example.notesapp.ui.theme.EditNote.View

import android.content.Context
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.notesapp.R
import com.example.notesapp.data.models.Note
import com.example.notesapp.databinding.ActivityEditNoteBinding
import com.example.notesapp.ui.theme.EditNote.Viewmodel.EditNoteViewModel
import com.google.android.material.snackbar.Snackbar

class EditNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditNoteBinding
    private val viewModel: EditNoteViewModel by viewModels()
    private lateinit var sharedPreferences: SharedPreferences
    private var noteId: Int = -1  // initialize id

    override fun onCreate(savedInstanceState: Bundle?) {
        loadTheme() // تحميل الثيم قبل setContentView
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_note)

        // تطبيق الألوان حسب الثيم
        applyThemeColors()

        // استلام البيانات من الـ Intent
        noteId = intent.getIntExtra("note_id", -1)
        val noteTitle = intent.getStringExtra("note_title")
        val noteContent = intent.getStringExtra("note_content")

        // عرض بيانات الملاحظة
        binding.etTitle.setText(noteTitle ?: "")
        binding.etNote.setText(noteContent ?: "")

        // عند الضغط على زر التعديل
        binding.btEdit.setOnClickListener {
            if (!binding.etTitle.text.isNullOrEmpty() && !binding.etNote.text.isNullOrEmpty()) {
                val updatedNote = Note(
                    id = noteId,
                    title = binding.etTitle.text.toString(),
                    content = binding.etNote.text.toString()
                )
                viewModel.editNote(updatedNote)
            }
        }

        // عند الضغط على زر الحذف
        binding.ivDelete.setOnClickListener {
            showDeleteConfirmationDialog()
        }

        // مراقبة التعديلات والحذف
        viewModel.editNote.observe(this, Observer {
            binding.btEdit.hideKeyboard()
            Snackbar.make(binding.root, R.string.note_edited, Snackbar.LENGTH_LONG)
                .setAction(R.string.dismiss) {
                    finish()
                }.show()
        })

        viewModel.deleteNote.observe(this, Observer {
            Snackbar.make(binding.root, R.string.note_deleted, Snackbar.LENGTH_LONG)
                .setAction(R.string.dismiss) {
                    finish()
                }.show()
        })
    }

    // تحميل الثيم من الإعدادات
    private fun loadTheme() {
        sharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE)
        val isDarkMode = sharedPreferences.getBoolean("DarkMode", true)

        if (isDarkMode) {
            setTheme(R.style.Theme_NotesApp_Dark)
        } else {
            setTheme(R.style.Theme_NotesApp_Light)
        }
    }

    // تطبيق الألوان حسب الثيم
    private fun applyThemeColors() {
        val isDarkMode = sharedPreferences.getBoolean("DarkMode", true)

        val backgroundColor = ContextCompat.getColor(this, if (isDarkMode) R.color.color_night else R.color.color_light)
        val textColor = ContextCompat.getColor(this, if (isDarkMode) R.color.text_dark else R.color.text_light)
        val hintColor = ContextCompat.getColor(this, if (isDarkMode) R.color.black else R.color.white)
        val buttonColor = ContextCompat.getColor(this, if (isDarkMode) R.color.mainColor else R.color.white)
        val boxBackgroundColor = ContextCompat.getColor(this, if (isDarkMode) R.color.white else R.color.black)

        binding.main.setBackgroundColor(backgroundColor)
        binding.etTitle.setTextColor(textColor)
        binding.etTitle.setHintTextColor(hintColor)
        binding.etNote.setTextColor(textColor)
        binding.etNote.setHintTextColor(hintColor)

        binding.titleTextField.defaultHintTextColor = ColorStateList.valueOf(hintColor)
        binding.noteTextField.defaultHintTextColor = ColorStateList.valueOf(hintColor)

        binding.titleTextField.boxBackgroundColor = boxBackgroundColor
        binding.noteTextField.boxBackgroundColor = boxBackgroundColor

        binding.btEdit.setTextColor(ContextCompat.getColor(this, if (isDarkMode) R.color.white else R.color.black))

        binding.btEdit.background = ContextCompat.getDrawable(this, R.drawable.button_shape)?.apply {
            setTint(buttonColor)
        }
        val deleteIcon = if (isDarkMode) R.drawable.delete else R.drawable.deletelight
        binding.ivDelete.setImageResource(deleteIcon)
    }

    // عرض مربع حوار لتأكيد الحذف
    private fun showDeleteConfirmationDialog() {
        val alertDialogBuilder = AlertDialog.Builder(this)
            .setMessage(R.string.delete_note_message)
            .setCancelable(false)
            .setPositiveButton(R.string.confirm_delete) { dialog, _ ->
                viewModel.deleteNote(Note(id = noteId, title = "", content = ""))
                dialog.dismiss()
            }
            .setNegativeButton(R.string.cancel_delete) { dialog, _ ->
                dialog.dismiss()
            }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    // إخفاء لوحة المفاتيح
    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}
