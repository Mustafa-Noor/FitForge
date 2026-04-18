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
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitforge.R
import com.example.fitforge.adapters.WorkoutAdapter
import com.example.fitforge.data.SharedPreferencesManager
import com.example.fitforge.data.models.Workout
import com.google.android.material.navigation.NavigationView

class HistoryActivity : AppCompatActivity(), WorkoutAdapter.OnWorkoutActionListener, NavigationView.OnNavigationItemSelectedListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var spinnerFilter: Spinner
    private lateinit var tvEmpty: TextView
    private lateinit var prefs: SharedPreferencesManager
    private lateinit var adapter: WorkoutAdapter
    private lateinit var drawerLayout: DrawerLayout
    private val workoutList = mutableListOf<Workout>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        prefs        = SharedPreferencesManager(this)
        drawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        navView.setNavigationItemSelectedListener(this)

        findViewById<ImageButton>(R.id.btnMenu).setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        recyclerView = findViewById(R.id.recyclerViewHistory)
        spinnerFilter = findViewById(R.id.spinnerFilter)
        tvEmpty      = findViewById(R.id.tvEmpty)

        adapter = WorkoutAdapter(workoutList, this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        val filters = listOf("All", "Chest", "Back", "Legs", "Shoulders", "Arms", "Core", "Cardio")
        val spinnerAdapter = ArrayAdapter(this, R.layout.fit_spinner_item, filters)
        spinnerAdapter.setDropDownViewResource(R.layout.fit_spinner_dropdown_item)
        spinnerFilter.adapter = spinnerAdapter

        spinnerFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                loadWorkouts(filters[position])
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        loadWorkouts("All")
    }

    private fun loadWorkouts(filter: String) {
        val all = prefs.getWorkouts()
        val filtered = if (filter == "All") all else all.filter { it.muscleGroup == filter }
        workoutList.clear()
        workoutList.addAll(filtered)
        adapter.notifyDataSetChanged()
        tvEmpty.visibility = if (workoutList.isEmpty()) View.VISIBLE else View.GONE
        recyclerView.visibility = if (workoutList.isEmpty()) View.GONE else View.VISIBLE
    }

    override fun onDeleteClick(workout: Workout, position: Int) {
        AlertDialog.Builder(this)
            .setTitle("Delete this workout?")
            .setMessage("This action cannot be undone.")
            .setPositiveButton("Yeah, delete it") { _, _ ->
                prefs.deleteWorkout(workout.id)
                workoutList.removeAt(position)
                adapter.notifyItemRemoved(position)
                Toast.makeText(this, "Workout deleted", Toast.LENGTH_SHORT).show()
                if (workoutList.isEmpty()) {
                    tvEmpty.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                }
            }
            .setNegativeButton("Wait no", null)
            .show()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val dest = when (item.itemId) {
            R.id.nav_home -> HomeActivity::class.java
            R.id.nav_log_workout -> LogWorkoutActivity::class.java
            R.id.nav_history -> null
            R.id.nav_exercise_library -> ExerciseLibraryActivity::class.java
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
