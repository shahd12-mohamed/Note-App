package com.example.notesapp.ui.theme.main.view

import android.content.Intent
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.notesapp.R
import com.example.notesapp.data.models.Note
import com.example.notesapp.databinding.ActivityMainBinding
import com.example.notesapp.ui.theme.AddNote.View.AddNoteActivity
import com.example.notesapp.ui.theme.main.adapter.NoteAdapter
import com.example.notesapp.ui.theme.main.Viewmodel.MainViewModel

class MainActivity : ComponentActivity() {

    lateinit var binding: ActivityMainBinding
    private val notesAdapter: NoteAdapter by lazy { NoteAdapter(emptyList()) } // only one instance of the adapter
    val viewModel: MainViewModel by viewModels()
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        // تLoad the theme before `setContentView`
        sharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE)
        val isDarkMode = sharedPreferences.getBoolean("DarkMode", false)

        if (isDarkMode) {
            setTheme(R.style.Theme_NotesApp_Dark) // theme dark
        } else {
            setTheme(R.style.Theme_NotesApp_Light) // theme light
        }

        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        // RecyclerView setup   List<Note> notes
        binding.rvNotes.adapter = notesAdapter

        // Add a new note
        binding.floatingActionButton.setOnClickListener {
            val isDarkMode = sharedPreferences.getBoolean("DarkMode", false)
            val newFabColor = if (isDarkMode) {
                ContextCompat.getColor(this, R.color.white)
            } else {
                ContextCompat.getColor(this, R.color.mainColor)
            }

            binding.floatingActionButton.backgroundTintList = ColorStateList.valueOf(newFabColor)

            // Save the current color in SharedPreferences
            sharedPreferences.edit().putInt("FabColor", newFabColor).apply()

            startActivity(Intent(this, AddNoteActivity::class.java))
        }


        // Definition of the toggle button inside the tulle bar
        val themeSwitch = binding.toolbar.themeSwitch.findViewById<ImageView>(R.id.themeSwitch)

        // Update the icon based on the current theme
        updateThemeUI(themeSwitch, isDarkMode)

        // When you press the edit
        themeSwitch.setOnClickListener {
            val newDarkModeState = !isDarkMode
            sharedPreferences.edit().putBoolean("DarkMode", newDarkModeState).apply()

            // Restart the Activity to apply the new theme
            recreate()
        }
    }

    // select the current theme and update the icon and colors accordingly
    private fun updateThemeUI(themeSwitch: ImageView, isDarkMode: Boolean) {
        if (isDarkMode) {
            themeSwitch.setImageResource(R.drawable.moon) // Moon icon for night mode
        } else {
            themeSwitch.setImageResource(R.drawable.light) //  light mode
        }
    }

    override fun onResume() {
        super.onResume()

        viewModel.getAllNotes()

        viewModel.notes.observe(this, Observer {
            notesAdapter.updateNotes(it) // Update notes list
            for (i in it.indices) println(it[i].id)
        })
    }
}

//        val notesList = mutableListOf(
//            Note(
//                "Product Meeting",
//                "Review of previous action items, product development update, user feedback and customer insights, competitive analysis, roadmap discussion."
//            ),
//            Note(
//                "To-do list",
//                "Reply to emails, prepare presentation slides for the marketing meeting, conduct research on competitor products, schedule and plan customer interviews, take a break and recharge."
//            ),
//            Note("Shopping list", "Rice, pasta, cereal, yogurt, cheese, butter."),
//            Note(
//                "Project Update",
//                "Submit the project report, discuss findings with the team, plan next steps."
//            ),
//            Note(
//                "Lecture Notes",
//                "Introduction to Android development, basics of Kotlin, understanding layouts, handling user inputs."
//            ),
//            Note("Workout Plan", "Warm-up, cardio exercises, strength training, cool down."),
//            Note("Weekend Plans", "Go hiking, visit the museum, dinner with friends."),
//            Note(
//                "Reading List",
//                "Complete 'Clean Code', start 'Effective Java', read articles on design patterns."
//            ),
//            Note(
//                "Meal Prep",
//                "Prepare meals for the week, grocery shopping for fresh produce, cook and store meals."
//            ),
//            Note("Travel Itinerary", "Book flights, reserve hotel, plan daily activities."),
//            Note(
//                "Product Meeting",
//                "Review of previous action items, product development update, user feedback and customer insights, competitive analysis, roadmap discussion."
//            ),
//            Note(
//                "To-do list",
//                "Reply to emails, prepare presentation slides for the marketing meeting, conduct research on competitor products, schedule and plan customer interviews, take a break and recharge."
//            ),
//            Note("Shopping list", "Rice, pasta, cereal, yogurt, cheese, butter.")
//        )

//        lifecycleScope.launch(Dispatchers.IO) {
//            val noteDao = AppDatabase.DatabaseBuilder.getInstance(this@MainActivity).noteDao()
//            //noteDao.addNote(Note(id=0,title = "To-do list", note = "Reply to emails, prepare presentation slides for the marketing meeting, conduct research on competitor products, schedule and plan customer interviews, take a break and recharge."))

//            val notes: List<Note> = noteDao.getNotes()
//            withContext(Dispatchers.Main){
//                notesAdapter = NotesAdapter(notes)
//                binding.rvNotes.adapter = notesAdapter
//            }
//        }
