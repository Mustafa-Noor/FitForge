package com.example.fitforge.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.fitforge.R
import com.example.fitforge.data.SharedPreferencesManager
import com.google.android.material.navigation.NavigationView

class SettingsActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var prefs: SharedPreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        prefs = SharedPreferencesManager(this)
        drawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        navView.setNavigationItemSelectedListener(this)

        findViewById<ImageButton>(R.id.btnMenu).setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        val switchRoast: SwitchCompat = findViewById(R.id.switchRoast)
        val switchReminder: SwitchCompat = findViewById(R.id.switchReminder)

        switchRoast.isChecked = prefs.isRoastEnabled()
        switchReminder.isChecked = prefs.isReminderEnabled()

        switchRoast.setOnCheckedChangeListener { _, isChecked ->
            prefs.setRoastEnabled(isChecked)
        }

        switchReminder.setOnCheckedChangeListener { _, isChecked ->
            prefs.setReminderEnabled(isChecked)
        }

        findViewById<android.widget.Button>(R.id.btnClearData).setOnClickListener {
            showClearDataDialog()
        }
    }

    private fun showClearDataDialog() {
        AlertDialog.Builder(this, R.style.FitAlertDialogTheme)
            .setTitle("Clear All Data?")
            .setMessage("Your streak, history, and badges will be gone forever. Built different? More like built nothing.")
            .setPositiveButton("I'm a quitter") { _, _ ->
                prefs.clearAllProgress()
                Toast.makeText(this, "Data wiped.", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, WelcomeActivity::class.java))
                finishAffinity()
            }
            .setNegativeButton("Wait, no!", null)
            .show()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val dest = when (item.itemId) {
            R.id.nav_home -> HomeActivity::class.java
            R.id.nav_log_workout -> LogWorkoutActivity::class.java
            R.id.nav_history -> HistoryActivity::class.java
            R.id.nav_exercise_library -> ExerciseLibraryActivity::class.java
            R.id.nav_profile -> ProfileActivity::class.java
            R.id.nav_settings -> null
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
