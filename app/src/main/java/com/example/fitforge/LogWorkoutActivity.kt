package com.example.fitforge

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fitforge.databinding.ActivityLogWorkoutBinding

class LogWorkoutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLogWorkoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogWorkoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSubmit.setOnClickListener {
            submitWorkout()
        }

        binding.btnCancel.setOnClickListener {
            finish()
        }
    }

    private fun submitWorkout() {
        val exerciseName = binding.etExerciseName.text.toString()
        val sets = binding.etSets.text.toString()
        val reps = binding.etReps.text.toString()
        val weight = binding.etWeight.text.toString()

        if (exerciseName.isEmpty() || sets.isEmpty() || reps.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields!", Toast.LENGTH_SHORT).show()
            return
        }

        // Broadcast workout logged event
        val intent = Intent("com.example.fitforge.WORKOUT_LOGGED")
        sendBroadcast(intent)

        Toast.makeText(this, "Workout logged! 💪", Toast.LENGTH_SHORT).show()
        finish()
    }
}
