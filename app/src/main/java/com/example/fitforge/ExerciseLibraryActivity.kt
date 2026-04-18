package com.example.fitforge

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitforge.adapter.ExerciseAdapter
import com.example.fitforge.data.Exercise
import com.example.fitforge.databinding.ActivityExerciseLibraryBinding

class ExerciseLibraryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityExerciseLibraryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Sample exercise data
        val exercises = listOf(
            Exercise(1, "Bench Press", "Chest", "Intermediate", "Classic upper body strength exercise"),
            Exercise(2, "Squats", "Legs", "Beginner", "Fundamental leg movement for strength"),
            Exercise(3, "Deadlifts", "Back/Legs", "Advanced", "Full body compound movement"),
            Exercise(4, "Pull-ups", "Back", "Intermediate", "Upper body pull exercise"),
            Exercise(5, "Barbell Rows", "Back", "Intermediate", "Great for back thickness"),
            Exercise(6, "Overhead Press", "Shoulders", "Intermediate", "Standing shoulder press exercise")
        )

        val adapter = ExerciseAdapter(exercises)
        binding.rvExercises.layoutManager = LinearLayoutManager(this)
        binding.rvExercises.adapter = adapter

        // Back button
        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}
