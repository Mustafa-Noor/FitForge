package com.example.fitforge.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitforge.R
import com.example.fitforge.adapters.ExerciseAdapter
import com.example.fitforge.data.ExerciseData
import com.example.fitforge.data.models.Exercise
import com.google.android.material.navigation.NavigationView

class ExerciseLibraryActivity : AppCompatActivity(), ExerciseAdapter.OnExerciseClickListener, NavigationView.OnNavigationItemSelectedListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var spinnerFilter: Spinner
    private lateinit var tvEmpty: TextView
    private lateinit var adapter: ExerciseAdapter
    private lateinit var drawerLayout: DrawerLayout
    private val exerciseList = mutableListOf<Exercise>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise_library)

        drawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        navView.setNavigationItemSelectedListener(this)

        findViewById<ImageButton>(R.id.btnMenu).setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        recyclerView  = findViewById(R.id.recyclerViewExercises)
        spinnerFilter = findViewById(R.id.spinnerFilter)
        tvEmpty       = findViewById(R.id.tvEmpty)

        adapter = ExerciseAdapter(exerciseList, this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        val filters = listOf("All", "Chest", "Back", "Legs", "Shoulders", "Arms", "Core", "Cardio")
        val spinnerAdapter = ArrayAdapter(this, R.layout.fit_spinner_item, filters)
        spinnerAdapter.setDropDownViewResource(R.layout.fit_spinner_dropdown_item)
        spinnerFilter.adapter = spinnerAdapter

        spinnerFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                loadExercises(filters[position])
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        loadExercises("All")
    }

    private fun loadExercises(filter: String) {
        val all = ExerciseData.exercises
        val filtered = if (filter == "All") all else all.filter { it.muscleGroup == filter }
        exerciseList.clear()
        exerciseList.addAll(filtered)
        adapter.notifyDataSetChanged()
        tvEmpty.visibility = if (exerciseList.isEmpty()) View.VISIBLE else View.GONE
        recyclerView.visibility = if (exerciseList.isEmpty()) View.GONE else View.VISIBLE
    }

    override fun onExerciseClick(exercise: Exercise) {
        AlertDialog.Builder(this, R.style.FitAlertDialogTheme)
            .setTitle(exercise.name)
            .setMessage("${exercise.muscleGroup}\n\n${exercise.description}")
            .setPositiveButton("Log This Exercise") { _, _ ->
                val intent = Intent(this, LogWorkoutActivity::class.java)
                intent.putExtra("exercise_name", exercise.name)
                startActivity(intent)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val dest = when (item.itemId) {
            R.id.nav_home -> HomeActivity::class.java
            R.id.nav_log_workout -> LogWorkoutActivity::class.java
            R.id.nav_history -> HistoryActivity::class.java
            R.id.nav_exercise_library -> null
            R.id.nav_profile -> ProfileActivity::class.java
            R.id.nav_settings -> SettingsActivity::class.java
            else -> null
        }
        dest?.let { 
            startActivity(Intent(this, it))
            finish()
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}
