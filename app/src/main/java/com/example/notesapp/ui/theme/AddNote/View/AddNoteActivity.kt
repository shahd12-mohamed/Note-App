package com.example.notesapp.ui.theme.AddNote.View

import android.content.Context
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.notesapp.R
import com.example.notesapp.data.models.Note
import com.example.notesapp.databinding.ActivityAddNoteBinding
import com.example.notesapp.ui.theme.AddNote.Viewmodel.AddNoteViewModel
import com.google.android.material.snackbar.Snackbar

class AddNoteActivity : ComponentActivity() {

    private lateinit var binding: ActivityAddNoteBinding
    private val viewModel: AddNoteViewModel by viewModels()

    // Variable to load application settings
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {

        loadTheme()

        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_note)

        // Adjust interface colors based on the current theme
        applyThemeColors()

        binding.btAdd.setOnClickListener {
            if (!binding.etTitle.text.isNullOrEmpty() && !binding.etNote.text.isNullOrEmpty()) {
                viewModel.addNote(
                    Note(
                        0, binding.etTitle.text.toString(),
                        binding.etNote.text.toString()
                    )
                )
            }
        }

        viewModel.addNote.observe(this, Observer {
            binding.btAdd.hideKeyboard()

            val isDarkMode = sharedPreferences.getBoolean("DarkMode", false)

            val snackbar = Snackbar.make(binding.main, R.string.note_added, Snackbar.LENGTH_LONG)
                .setAction(R.string.dismiss) {
                    finish()
                }

            // Change the background color according to the theme
            snackbar.view.setBackgroundColor(
                ContextCompat.getColor(this, if (isDarkMode) R.color.mainColor else R.color.darkorange)
            )

            // Change the color of message text
            snackbar.setTextColor(ContextCompat.getColor(this, R.color.white))

            // Change color dismiss
            snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.white))

            snackbar.show()
        })

    }

    // Download the theme from settings and apply it
    private fun loadTheme() {
        sharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE)
        val isDarkMode = sharedPreferences.getBoolean("DarkMode", true)

        if (isDarkMode) {
            setTheme(R.style.Theme_NotesApp_Dark) // Apply the dark theme
        } else {
            setTheme(R.style.Theme_NotesApp_Light) // Apply the light theme
        }
    }

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

        // hint في TextInputLayout
        binding.titleTextField.defaultHintTextColor = ColorStateList.valueOf(hintColor)
        binding.noteTextField.defaultHintTextColor = ColorStateList.valueOf(hintColor)

        binding.titleTextField.boxBackgroundColor = boxBackgroundColor
        //binding.titleTextField.setBoxBackgroundColor(boxBackgroundColor)
        binding.noteTextField.setBoxBackgroundColor(boxBackgroundColor)


        binding.btAdd.setTextColor(ContextCompat.getColor(this, if (isDarkMode) R.color.white else R.color.black    ))

        // Change the color of the button while keeping the shape
        binding.btAdd.background = ContextCompat.getDrawable(this, R.drawable.button_shape)?.apply {
            setTint(buttonColor)
        }
    }





    // hide keyboard
    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}
