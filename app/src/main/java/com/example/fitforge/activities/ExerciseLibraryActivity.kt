package com.example.fitforge.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitforge.R
import com.example.fitforge.adapters.ExerciseAdapter
import com.example.fitforge.adapters.MuscleGroupAdapter
import com.example.fitforge.data.ExerciseData
import com.example.fitforge.data.models.Exercise
import com.google.android.material.navigation.NavigationView

class ExerciseLibraryActivity : AppCompatActivity(), ExerciseAdapter.OnExerciseClickListener, NavigationView.OnNavigationItemSelectedListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var tvEmpty: TextView
    private lateinit var tvTitle: TextView
    private lateinit var btnBackToCategories: ImageButton
    private lateinit var drawerLayout: DrawerLayout
    
    private val exerciseList = mutableListOf<Exercise>()
    private lateinit var exerciseAdapter: ExerciseAdapter
    private lateinit var muscleGroupAdapter: MuscleGroupAdapter
    
    private var showingCategories = true

    private val muscleGroupsWithEmojis = listOf(
        "Chest" to "💪",
        "Back" to "🚣",
        "Legs" to "🦵",
        "Shoulders" to "🏹",
        "Arms" to "🥊",
        "Core" to "🧘",
        "Cardio" to "🏃",
        "Full Body" to "🏋️"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise_library)

        drawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        navView.setNavigationItemSelectedListener(this)

        findViewById<ImageButton>(R.id.btnMenu).setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        recyclerView = findViewById(R.id.recyclerViewExercises)
        tvEmpty      = findViewById(R.id.tvEmpty)
        tvTitle      = findViewById(R.id.tvTitle)
        btnBackToCategories = findViewById(R.id.btnBackToCategories)

        exerciseAdapter = ExerciseAdapter(exerciseList, this)
        muscleGroupAdapter = MuscleGroupAdapter(muscleGroupsWithEmojis) { category ->
            showExercisesForCategory(category)
        }

        btnBackToCategories.setOnClickListener {
            showCategories()
        }

        showCategories()
    }

    private fun showCategories() {
        showingCategories = true
        tvTitle.text = "Exercise Library"
        btnBackToCategories.visibility = View.GONE
        
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = muscleGroupAdapter
        
        tvEmpty.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
    }

    private fun showExercisesForCategory(category: String) {
        showingCategories = false
        tvTitle.text = category
        btnBackToCategories.visibility = View.VISIBLE
        
        val filtered = ExerciseData.exercises.filter { it.muscleGroup == category }
        exerciseList.clear()
        exerciseList.addAll(filtered)
        
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = exerciseAdapter
        
        exerciseAdapter.notifyDataSetChanged()
        
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

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else if (!showingCategories) {
            showCategories()
        } else {
            super.onBackPressed()
        }
    }
}
