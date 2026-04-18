package com.example.fitforge.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.fitforge.R
import com.example.fitforge.data.SharedPreferencesManager
import com.example.fitforge.utils.RoastStrings
import com.google.android.material.navigation.NavigationView

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var prefs: SharedPreferencesManager
    private lateinit var tvStreakNumber: TextView
    private lateinit var tvLastLogged: TextView
    private lateinit var tvRoastBanner: TextView
    private lateinit var tvTotalWorkouts: TextView
    private lateinit var tvBestStreak: TextView
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        prefs = SharedPreferencesManager(this)

        drawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        navView.setNavigationItemSelectedListener(this)

        tvStreakNumber   = findViewById(R.id.tvStreakNumber)
        tvLastLogged     = findViewById(R.id.tvLastLogged)
        tvRoastBanner    = findViewById(R.id.tvRoastBanner)
        tvTotalWorkouts  = findViewById(R.id.tvTotalWorkouts)
        tvBestStreak     = findViewById(R.id.tvBestStreak)

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

    override fun onResume() {
        super.onResume()
        loadData()
    }

    private fun loadData() {
        val streak = prefs.getCurrentStreak()
        val total  = prefs.getTotalWorkouts()
        val best   = prefs.getBestStreak()
        val lastDate = prefs.getLastLoggedDate() ?: "Never"

        tvStreakNumber.text  = streak.toString()
        tvLastLogged.text    = "Last logged: $lastDate"
        tvRoastBanner.text   = "\"${RoastStrings.getHomeBanner(streak, total)}\""
        tvTotalWorkouts.text = total.toString()
        tvBestStreak.text    = "🔥${best}d"
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
