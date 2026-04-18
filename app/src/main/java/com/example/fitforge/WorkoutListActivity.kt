package com.example.fitforge

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitforge.adapter.WorkoutAdapter
import com.example.fitforge.data.Workout
import com.example.fitforge.databinding.ActivityWorkoutListBinding

class WorkoutListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWorkoutListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkoutListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Sample data
        val workouts = listOf(
            Workout(1, "Bench Press", 4, 8, "225 lbs", "Apr 17, 2026", 45),
            Workout(2, "Squats", 4, 6, "315 lbs", "Apr 16, 2026", 50),
            Workout(3, "Deadlifts", 3, 5, "405 lbs", "Apr 15, 2026", 40),
            Workout(4, "Pull-ups", 5, 10, "Bodyweight", "Apr 14, 2026", 30)
        )

        val adapter = WorkoutAdapter(workouts)
        binding.rvWorkouts.layoutManager = LinearLayoutManager(this)
        binding.rvWorkouts.adapter = adapter

        // Back button
        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}
