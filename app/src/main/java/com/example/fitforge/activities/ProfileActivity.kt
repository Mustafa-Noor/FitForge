package com.example.fitforge.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitforge.R
import com.example.fitforge.adapters.BadgeAdapter
import com.example.fitforge.data.SharedPreferencesManager
import com.example.fitforge.data.models.Badge
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.navigation.NavigationView

class ProfileActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var tvTotalWorkouts: TextView
    private lateinit var tvCurrentStreak: TextView
    private lateinit var tvBestStreak: TextView
    private lateinit var tvProfileName: TextView
    private lateinit var recyclerViewBadges: RecyclerView
    private lateinit var ivProfilePicture: ShapeableImageView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var prefs: SharedPreferencesManager
    private lateinit var navView: NavigationView

    private val imagePickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            contentResolver.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            ivProfilePicture.setImageURI(it)
            prefs.setProfileImageUri(it.toString())
            updateNavHeader() // Sync sidebar immediately
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        prefs = SharedPreferencesManager(this)
        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)
        navView.setNavigationItemSelectedListener(this)

        updateNavHeader()

        tvTotalWorkouts = findViewById(R.id.tvTotalWorkouts)
        tvCurrentStreak = findViewById(R.id.tvCurrentStreak)
        tvBestStreak = findViewById(R.id.tvBestStreak)
        tvProfileName = findViewById(R.id.tvProfileName)
        recyclerViewBadges = findViewById(R.id.recyclerViewBadges)
        ivProfilePicture = findViewById(R.id.ivProfilePicture)

        findViewById<ImageButton>(R.id.btnMenu).setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        findViewById<ImageButton>(R.id.btnEditProfilePic).setOnClickListener {
            imagePickerLauncher.launch("image/*")
        }

        findViewById<ImageButton>(R.id.btnEditName).setOnClickListener {
            showEditNameDialog()
        }

        tvProfileName.text = prefs.getUsername()

        val savedUri = prefs.getProfileImageUri()
        if (savedUri != null) {
            try {
                ivProfilePicture.setImageURI(Uri.parse(savedUri))
            } catch (e: Exception) {
                ivProfilePicture.setImageResource(R.drawable.ff_gym_logo)
            }
        }

        loadStats()
        loadBadges()
    }

    private fun updateNavHeader() {
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

    private fun showEditNameDialog() {
        val editText = EditText(this)
        editText.setText(tvProfileName.text)
        editText.setSelection(editText.text.length)

        AlertDialog.Builder(this, R.style.FitAlertDialogTheme)
            .setTitle("Change Name")
            .setView(editText)
            .setPositiveButton("Save") { _, _ ->
                val newName = editText.text.toString().trim()
                if (newName.isNotEmpty()) {
                    tvProfileName.text = newName
                    prefs.setUsername(newName)
                    updateNavHeader() // Sync sidebar immediately
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun loadStats() {
        val total = prefs.getTotalWorkouts()
        val current = prefs.getCurrentStreak()
        val best = prefs.getBestStreak()

        tvTotalWorkouts.text = total.toString()
        tvCurrentStreak.text = current.toString()
        tvBestStreak.text = best.toString()
    }

    private fun loadBadges() {
        val allBadges = listOf(
            Badge("momentum", "Building Momentum", "3 days in a row. Don't stop now.", "3-day streak", "🚀", prefs.isBadgeUnlocked("momentum")),
            Badge("baby_gains", "Baby Gains", "First 3 workouts logged", "3 workouts", "🏅", prefs.isBadgeUnlocked("baby_gains")),
            Badge("gym_rat", "Certified Gym Rat", "7 days straight. Built different.", "7-day streak", "🐀", prefs.isBadgeUnlocked("gym_rat")),
            Badge("double_digits", "Double Digits", "10 total workouts. You're getting serious.", "10 workouts", "🔟", prefs.isBadgeUnlocked("double_digits")),
            Badge("leg_day", "Leg Day Respecter", "Legs two sessions in a row.", "2x Legs", "🦵", prefs.isBadgeUnlocked("leg_day")),
            Badge("discipline", "Pure Discipline", "14 days straight. Most people would have quit.", "14-day streak", "🧘", prefs.isBadgeUnlocked("discipline")),
            Badge("built_diff", "Built Different", "30 total workouts. Certified.", "30 workouts", "💪", prefs.isBadgeUnlocked("built_diff")),
            Badge("half_century", "The Half Century", "50 total workouts. Elite level consistency.", "50 workouts", "🏛️", prefs.isBadgeUnlocked("half_century")),
            Badge("heart_health", "Heart of Gold", "Completed a cardio session. The engine is running.", "1x Cardio", "❤️", prefs.isBadgeUnlocked("heart_health"))
        )

        val adapter = BadgeAdapter(allBadges)
        recyclerViewBadges.layoutManager = LinearLayoutManager(this)
        recyclerViewBadges.adapter = adapter
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val dest = when (item.itemId) {
            R.id.nav_home -> HomeActivity::class.java
            R.id.nav_log_workout -> LogWorkoutActivity::class.java
            R.id.nav_history -> HistoryActivity::class.java
            R.id.nav_exercise_library -> ExerciseLibraryActivity::class.java
            R.id.nav_profile -> null
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
