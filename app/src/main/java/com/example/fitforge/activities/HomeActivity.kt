package com.example.fitforge.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitforge.R
import com.example.fitforge.adapters.WorkoutAdapter
import com.example.fitforge.data.SharedPreferencesManager
import com.example.fitforge.data.models.Workout
import com.example.fitforge.utils.RoastStrings
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.navigation.NavigationView
import java.time.LocalDate

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, WorkoutAdapter.OnWorkoutActionListener {

    private lateinit var prefs: SharedPreferencesManager
    private lateinit var tvStreakNumber: TextView
    private lateinit var tvLastLogged: TextView
    private lateinit var tvTotalWorkouts: TextView
    private lateinit var tvBestStreak: TextView
    private lateinit var tvTodayEmpty: TextView
    private lateinit var rvTodayWorkouts: RecyclerView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    
    private lateinit var todayAdapter: WorkoutAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        prefs = SharedPreferencesManager(this)

        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)
        navView.setNavigationItemSelectedListener(this)

        updateNavHeader(navView)

        tvStreakNumber   = findViewById(R.id.tvStreakNumber)
        tvLastLogged     = findViewById(R.id.tvLastLogged)
        tvTotalWorkouts  = findViewById(R.id.tvTotalWorkouts)
        tvBestStreak     = findViewById(R.id.tvBestStreak)
        tvTodayEmpty     = findViewById(R.id.tvTodayEmpty)
        rvTodayWorkouts  = findViewById(R.id.rvTodayWorkouts)

        findViewById<ImageButton>(R.id.btnMenu).setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        findViewById<android.widget.Button>(R.id.btnLogWorkout).setOnClickListener {
            startActivity(Intent(this, LogWorkoutActivity::class.java))
        }
        findViewById<android.widget.Button>(R.id.btnHistory).setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }
        findViewById<android.widget.Button>(R.id.btnProfile).setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        // Setup Today's Activity RecyclerView with an empty mutable list initially
        todayAdapter = WorkoutAdapter(mutableListOf(), this)
        rvTodayWorkouts.layoutManager = LinearLayoutManager(this)
        rvTodayWorkouts.adapter = todayAdapter

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START)
                } else {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })
    }

    private fun updateNavHeader(navView: NavigationView) {
        val headerView = navView.getHeaderView(0)
        val tvNavUsername: TextView = headerView.findViewById(R.id.tvNavUsername)
        val ivNavProfilePic: ShapeableImageView = headerView.findViewById(R.id.ivNavProfilePic)

        tvNavUsername.text = prefs.getUsername()
        
        val savedUri = prefs.getProfileImageUri()
        if (savedUri != null) {
            try {
                ivNavProfilePic.setImageURI(Uri.parse(savedUri))
            } catch (e: Exception) {
                ivNavProfilePic.setImageResource(R.drawable.ff_gym_logo)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadData()
        updateNavHeader(findViewById(R.id.nav_view))
    }

    private fun loadData() {
        val streak = prefs.getCurrentStreak()
        val total  = prefs.getTotalWorkouts()
        val best   = prefs.getBestStreak()
        val lastDate = prefs.getLastLoggedDate() ?: "Never"

        tvStreakNumber.text  = streak.toString()
        tvLastLogged.text    = "Last logged: $lastDate"
        tvTotalWorkouts.text = total.toString()
        tvBestStreak.text    = "🔥${best}d"

        // Load Today's Workouts
        val today = LocalDate.now().toString()
        val allWorkouts = prefs.getWorkouts()
        val todayLogs = allWorkouts.filter { 
            runCatching { java.time.Instant.ofEpochMilli(it.dateMillis).atZone(java.time.ZoneId.systemDefault()).toLocalDate().toString() == today }.getOrDefault(false)
        }

        todayAdapter.setData(todayLogs)

        if (todayLogs.isEmpty()) {
            tvTodayEmpty.visibility = View.VISIBLE
            rvTodayWorkouts.visibility = View.GONE
        } else {
            tvTodayEmpty.visibility = View.GONE
            rvTodayWorkouts.visibility = View.VISIBLE
        }
    }

    override fun onEditClick(workout: Workout, position: Int) {
        val intent = Intent(this, LogWorkoutActivity::class.java)
        intent.putExtra("edit_workout_id", workout.id)
        startActivity(intent)
    }

    override fun onDeleteClick(workout: Workout, position: Int) {
        androidx.appcompat.app.AlertDialog.Builder(this, R.style.FitAlertDialogTheme)
            .setTitle("Delete today's log?")
            .setMessage("Removing this won't reset your streak unless it's your only log for today.")
            .setPositiveButton("Delete") { _, _ ->
                prefs.deleteWorkout(workout.id)
                loadData() // Refresh everything
            }
            .setNegativeButton("Keep it", null)
            .show()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val dest = when (item.itemId) {
            R.id.nav_log_workout -> LogWorkoutActivity::class.java
            R.id.nav_history -> HistoryActivity::class.java
            R.id.nav_exercise_library -> ExerciseLibraryActivity::class.java
            R.id.nav_profile -> ProfileActivity::class.java
            R.id.nav_settings -> SettingsActivity::class.java
            else -> null
        }
        
        dest?.let { startActivity(Intent(this, it)) }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}
