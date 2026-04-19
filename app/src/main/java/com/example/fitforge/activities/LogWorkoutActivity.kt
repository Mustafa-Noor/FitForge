package com.example.fitforge.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.widget.ArrayAdapter
import android.widget.Button
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
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import android.text.TextWatcher
import com.google.android.material.imageview.ShapeableImageView

class LogWorkoutActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var etExerciseName: TextInputEditText
    private lateinit var layoutExercise: TextInputLayout
    private lateinit var spinnerMuscleGroup: Spinner
    private lateinit var etSets: TextInputEditText
    private lateinit var layoutSets: TextInputLayout
    private lateinit var etReps: TextInputEditText
    private lateinit var layoutReps: TextInputLayout
    private lateinit var etWeight: TextInputEditText
    private lateinit var layoutWeight: TextInputLayout
    private lateinit var etNotes: TextInputEditText
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

        updateNavHeader(navView)

        findViewById<ImageButton>(R.id.btnMenu).setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        etExerciseName  = findViewById(R.id.etExerciseName)
        layoutExercise  = findViewById(R.id.layout_exercise)
        spinnerMuscleGroup = findViewById(R.id.spinnerMuscleGroup)
        etSets          = findViewById(R.id.etSets)
        layoutSets      = findViewById(R.id.layout_sets)
        etReps          = findViewById(R.id.etReps)
        layoutReps      = findViewById(R.id.layout_reps)
        etWeight        = findViewById(R.id.etWeight)
        layoutWeight    = findViewById(R.id.layout_weight)
        etNotes         = findViewById(R.id.etNotes)
        btnSaveWorkout  = findViewById(R.id.btnSaveWorkout)
        btnRestTimer    = findViewById(R.id.btnRestTimer)
        tvTitle         = findViewById(R.id.tvTitle)

        val adapter = ArrayAdapter(this, R.layout.fit_spinner_item, muscleGroups)
        adapter.setDropDownViewResource(R.layout.fit_spinner_dropdown_item)
        spinnerMuscleGroup.adapter = adapter

        // Setup TextWatchers to clear errors
        setupValidationWatchers()

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

    private fun setupValidationWatchers() {
        etExerciseName.addTextChangedListener(SimpleTextWatcher { layoutExercise.error = null })
        etSets.addTextChangedListener(SimpleTextWatcher { layoutSets.error = null })
        etReps.addTextChangedListener(SimpleTextWatcher { layoutReps.error = null })
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
        val setsStr = etSets.text.toString().trim()
        val repsStr = etReps.text.toString().trim()
        val weightStr = etWeight.text.toString().trim()
        val notes  = etNotes.text.toString().trim()

        var isValid = true

        if (name.isEmpty()) {
            layoutExercise.error = "Exercise name is required"
            isValid = false
        }
        if (setsStr.isEmpty()) {
            layoutSets.error = "Required"
            isValid = false
        }
        if (repsStr.isEmpty()) {
            layoutReps.error = "Required"
            isValid = false
        }

        if (!isValid) return

        val sets = setsStr.toIntOrNull() ?: 0
        val reps = repsStr.toIntOrNull() ?: 0
        val weight = weightStr.toFloatOrNull() ?: 0f

        val workout = if (isEditMode && workoutToEdit != null) {
            workoutToEdit!!.copy(
                exerciseName = name,
                muscleGroup  = muscle,
                sets         = sets,
                reps         = reps,
                weightKg     = weight,
                notes        = notes
            )
        } else {
            Workout(
                exerciseName = name,
                muscleGroup  = muscle,
                sets         = sets,
                reps         = reps,
                weightKg     = weight,
                notes        = notes
            )
        }

        if (isEditMode) {
            prefs.updateWorkout(workout)
            Toast.makeText(this, "Workout updated.", Toast.LENGTH_SHORT).show()
        } else {
            prefs.saveWorkout(workout)
            prefs.updateStreakAfterWorkout()
            
            val previousMuscleGroup = prefs.getLastMuscleGroup() ?: ""
            prefs.setLastMuscleGroup(muscle)

            val newBadges = BadgeChecker.checkBadges(
                totalWorkouts       = prefs.getTotalWorkouts(),
                currentStreak       = prefs.getCurrentStreak(),
                lastMuscleGroup     = muscle,
                previousMuscleGroup = previousMuscleGroup,
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

    private class SimpleTextWatcher(private val onTextChanged: (String) -> Unit) : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            onTextChanged(s?.toString() ?: "")
        }
        override fun afterTextChanged(s: Editable?) {}
    }
}
