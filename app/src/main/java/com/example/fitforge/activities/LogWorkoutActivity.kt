package com.example.fitforge.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.fitforge.R
import com.example.fitforge.data.SharedPreferencesManager
import com.example.fitforge.data.models.Workout
import com.example.fitforge.notifications.FitNotificationManager
import com.example.fitforge.utils.BadgeChecker
import com.example.fitforge.data.models.Badge
import com.google.android.material.navigation.NavigationView

class LogWorkoutActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var etExerciseName: EditText
    private lateinit var spinnerMuscleGroup: Spinner
    private lateinit var etSets: EditText
    private lateinit var etReps: EditText
    private lateinit var etWeight: EditText
    private lateinit var etNotes: EditText
    private lateinit var btnSaveWorkout: Button
    private lateinit var btnRestTimer: Button
    private lateinit var tvTitle: TextView
    private lateinit var prefs: SharedPreferencesManager
    private lateinit var drawerLayout: DrawerLayout

    private var isEditMode = false
    private var workoutToEdit: Workout? = null

    private val muscleGroups = listOf("Chest", "Back", "Legs", "Shoulders", "Arms", "Core", "Cardio", "Full Body")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_workout)

        prefs = SharedPreferencesManager(this)
        drawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        navView.setNavigationItemSelectedListener(this)

        findViewById<ImageButton>(R.id.btnMenu).setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        etExerciseName  = findViewById(R.id.etExerciseName)
        spinnerMuscleGroup = findViewById(R.id.spinnerMuscleGroup)
        etSets          = findViewById(R.id.etSets)
        etReps          = findViewById(R.id.etReps)
        etWeight        = findViewById(R.id.etWeight)
        etNotes         = findViewById(R.id.etNotes)
        btnSaveWorkout  = findViewById(R.id.btnSaveWorkout)
        btnRestTimer    = findViewById(R.id.btnRestTimer)
        tvTitle         = findViewById(R.id.tvTitle)

        val adapter = ArrayAdapter(this, R.layout.fit_spinner_item, muscleGroups)
        adapter.setDropDownViewResource(R.layout.fit_spinner_dropdown_item)
        spinnerMuscleGroup.adapter = adapter

        // Check Mode
        val editWorkoutId = intent.getStringExtra("edit_workout_id")
        val prefilledExercise = intent.getStringExtra("exercise_name")

        if (editWorkoutId != null) {
            isEditMode = true
            workoutToEdit = prefs.getWorkouts().find { it.id == editWorkoutId }
            workoutToEdit?.let { setupEditMode(it) }
        } else if (!prefilledExercise.isNullOrEmpty()) {
            etExerciseName.setText(prefilledExercise)
        }

        btnSaveWorkout.setOnClickListener { saveWorkout() }
        btnRestTimer.setOnClickListener { showRestTimerDialog() }
        findViewById<Button>(R.id.btn_cancel).setOnClickListener { finish() }
    }

    private fun setupEditMode(workout: Workout) {
        tvTitle.text = "Edit Workout"
        btnSaveWorkout.text = "Update Workout"
        
        etExerciseName.setText(workout.exerciseName)
        etSets.setText(workout.sets.toString())
        etReps.setText(workout.reps.toString())
        etWeight.setText(workout.weightKg.toString())
        etNotes.setText(workout.notes)
        
        val muscleIndex = muscleGroups.indexOf(workout.muscleGroup)
        if (muscleIndex != -1) spinnerMuscleGroup.setSelection(muscleIndex)
    }

    private fun saveWorkout() {
        val name   = etExerciseName.text.toString().trim()
        val muscle = spinnerMuscleGroup.selectedItem.toString()
        val sets   = etSets.text.toString().trim()
        val reps   = etReps.text.toString().trim()
        val weight = etWeight.text.toString().trim()
        val notes  = etNotes.text.toString().trim()

        if (name.isEmpty()) { etExerciseName.error = "Exercise name is required"; return }
        if (sets.isEmpty())  { etSets.error = "Sets required"; return }
        if (reps.isEmpty())  { etReps.error = "Reps required"; return }

        val workout = if (isEditMode && workoutToEdit != null) {
            workoutToEdit!!.copy(
                exerciseName = name,
                muscleGroup  = muscle,
                sets         = sets.toInt(),
                reps         = reps.toInt(),
                weightKg     = weight.toFloatOrNull() ?: 0f,
                notes        = notes
            )
        } else {
            Workout(
                exerciseName = name,
                muscleGroup  = muscle,
                sets         = sets.toInt(),
                reps         = reps.toInt(),
                weightKg     = weight.toFloatOrNull() ?: 0f,
                notes        = notes
            )
        }

        if (isEditMode) {
            prefs.updateWorkout(workout)
            Toast.makeText(this, "Workout updated.", Toast.LENGTH_SHORT).show()
        } else {
            prefs.saveWorkout(workout)
            prefs.updateStreakAfterWorkout()
            prefs.setLastMuscleGroup(muscle)

            val newBadges = BadgeChecker.checkBadges(
                totalWorkouts       = prefs.getTotalWorkouts(),
                currentStreak       = prefs.getCurrentStreak(),
                lastMuscleGroup     = muscle,
                previousMuscleGroup = prefs.getLastMuscleGroup() ?: "",
                prefs               = prefs
            )
            newBadges.forEach { badge -> showBadgeDialog(badge) }
            FitNotificationManager.sendWorkoutLoggedNotification(this)
            Toast.makeText(this, "Logged. Built different.", Toast.LENGTH_SHORT).show()
        }
        
        finish()
    }

    private fun showRestTimerDialog() {
        var seconds = 90
        val dialogView = layoutInflater.inflate(R.layout.dialog_rest_timer, null)
        val tvTimer   = dialogView.findViewById<TextView>(R.id.tvTimerDisplay)
        val btnMinus  = dialogView.findViewById<Button>(R.id.btnMinus15)
        val btnPlus   = dialogView.findViewById<Button>(R.id.btnPlus15)

        fun updateDisplay() { tvTimer.text = "${seconds / 60} min ${seconds % 60} sec" }
        updateDisplay()
        btnMinus.setOnClickListener { seconds = maxOf(0, seconds - 15); updateDisplay() }
        btnPlus.setOnClickListener  { seconds += 15; updateDisplay() }

        AlertDialog.Builder(this, R.style.FitAlertDialogTheme)
            .setTitle("Set Rest Timer")
            .setView(dialogView)
            .setPositiveButton("Start Timer") { _, _ ->
                Toast.makeText(this, "Rest timer: $seconds seconds", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showBadgeDialog(badge: Badge) {
        AlertDialog.Builder(this, R.style.FitAlertDialogTheme)
            .setTitle("${badge.emoji} ${badge.name}")
            .setMessage(badge.description)
            .setPositiveButton("Let's go!", null)
            .show()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val dest = when (item.itemId) {
            R.id.nav_home -> HomeActivity::class.java
            R.id.nav_log_workout -> null
            R.id.nav_history -> HistoryActivity::class.java
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
