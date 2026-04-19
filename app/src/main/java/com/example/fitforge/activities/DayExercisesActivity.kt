package com.example.fitforge.activities

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitforge.R
import com.example.fitforge.adapters.ChallengeExercisesAdapter
import com.example.fitforge.data.ChallengeData
import com.example.fitforge.data.SharedPreferencesManager
import com.example.fitforge.data.models.Workout

class DayExercisesActivity : AppCompatActivity() {

    private lateinit var prefs: SharedPreferencesManager
    private lateinit var challengeId: String
    private var dayNumber: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_day_exercises)

        prefs = SharedPreferencesManager(this)
        challengeId = intent.getStringExtra("challenge_id") ?: ""
        dayNumber = intent.getIntExtra("day_number", 1)

        val challenge = ChallengeData.challenges.find { it.id == challengeId } ?: return
        val day = challenge.days.find { it.dayNumber == dayNumber } ?: return

        findViewById<TextView>(R.id.tvDayTitle).text = "Day $dayNumber Exercises"
        findViewById<ImageButton>(R.id.btnBack).setOnClickListener { finish() }

        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewExercises)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = ChallengeExercisesAdapter(day.exercises)

        findViewById<Button>(R.id.btnCompleteDay).setOnClickListener {
            // Log all exercises in this day to history
            day.exercises.forEach { exercise ->
                val workout = Workout(
                    exerciseName = exercise.exerciseName,
                    muscleGroup = exercise.muscleGroup,
                    sets = exercise.sets,
                    reps = exercise.reps,
                    weightKg = 0f, // Challenges usually use bodyweight
                    notes = "Completed Day $dayNumber of ${challenge.title}"
                )
                prefs.saveWorkout(workout)
            }
            
            // Update streak and total workouts
            prefs.updateStreakAfterWorkout()
            
            // Mark challenge day as complete
            prefs.completeChallengeDay(challengeId, dayNumber - 1) // dayNumber is 1-based, storage is 0-based index
            
            Toast.makeText(this, "Day $dayNumber Completed and Logged! 🏋️", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
