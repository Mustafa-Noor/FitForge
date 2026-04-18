package com.example.fitforge

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.fitforge.databinding.ActivityStreakHistoryBinding

class StreakHistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStreakHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStreakHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadStreakData()

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun loadStreakData() {
        val sharedPrefs = getSharedPreferences("FitForge", Context.MODE_PRIVATE)
        val currentStreak = sharedPrefs.getInt("workout_streak", 0)
        val longestStreak = sharedPrefs.getInt("longest_streak", 0)
        val totalWorkouts = sharedPrefs.getInt("total_workouts", 0)

        binding.tvCurrentStreak.text = "$currentStreak Days"
        binding.tvLongestStreak.text = "$longestStreak Days"
        binding.tvTotalWorkouts.text = "$totalWorkouts"

        // Detailed message
        val message = when {
            currentStreak >= 30 -> "🔥 Absolute Beast! 30+ day streak!"
            currentStreak >= 14 -> "💪 Amazing dedication! 2+ weeks!"
            currentStreak >= 7 -> "🚀 Great work! A week of consistency!"
            currentStreak > 0 -> "💯 Nice! Keep the momentum going!"
            else -> "⚠️ Time to start fresh! No streak yet."
        }
        
        binding.tvMessage.text = message
    }
}
