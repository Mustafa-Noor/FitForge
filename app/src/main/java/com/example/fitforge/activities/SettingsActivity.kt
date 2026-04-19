package com.example.fitforge.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.fitforge.R
import com.example.fitforge.data.SharedPreferencesManager
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.navigation.NavigationView

class SettingsActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var prefs: SharedPreferencesManager
    private lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        prefs = SharedPreferencesManager(this)
        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)
        navView.setNavigationItemSelectedListener(this)

        updateNavHeader()

        findViewById<ImageButton>(R.id.btnMenu).setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        val switchRoast: SwitchCompat = findViewById(R.id.switchRoast)
        val switchReminder: SwitchCompat = findViewById(R.id.switchReminder)
        val seekBarGoal: SeekBar = findViewById(R.id.seekBarGoal)
        val tvGoalValue: TextView = findViewById(R.id.tvGoalValue)

        // Initial States
        switchRoast.isChecked = prefs.isRoastEnabled()
        switchReminder.isChecked = prefs.isReminderEnabled()
        
        val savedGoal = prefs.getWeeklyGoal()
        seekBarGoal.progress = savedGoal
        tvGoalValue.text = savedGoal.toString()

        // Listeners
        switchRoast.setOnCheckedChangeListener { _, isChecked ->
            prefs.setRoastEnabled(isChecked)
            Toast.makeText(this, if (isChecked) "Roasts active. Watch out." else "Roasts disabled. Soft.", Toast.LENGTH_SHORT).show()
        }

        switchReminder.setOnCheckedChangeListener { _, isChecked ->
            prefs.setReminderEnabled(isChecked)
        }

        seekBarGoal.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val value = if (progress == 0) 1 else progress
                tvGoalValue.text = value.toString()
                prefs.setWeeklyGoal(value)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        findViewById<android.widget.Button>(R.id.btnClearData).setOnClickListener {
            showClearDataDialog()
        }
    }

    private fun updateNavHeader() {
        val headerView = navView.getHeaderView(0)
        val tvNavUsername: TextView = headerView.findViewById(R.id.tvNavUsername)
        val ivNavProfilePic: ShapeableImageView = headerView.findViewById(R.id.ivNavProfilePic)

        tvNavUsername.text = prefs.getUsername()
        
        val savedUri = prefs.getProfileImageUri()
        if (savedUri != null) {
            try {
                ivNavProfilePic.setImageURI(android.net.Uri.parse(savedUri))
            } catch (e: Exception) {
                ivNavProfilePic.setImageResource(R.drawable.ff_gym_logo)
            }
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
