package com.example.fitforge

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.fitforge.databinding.ActivityMainBinding
import com.example.fitforge.viewmodel.RoastViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var roastViewModel: RoastViewModel
    private lateinit var workoutReceiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize ViewModel
        roastViewModel = ViewModelProvider(this).get(RoastViewModel::class.java)

        // Setup BroadcastReceiver
        setupBroadcastReceiver()

        // Load persisted data
        loadUserData()

        // Setup all button click listeners
        setupClickListeners()
    }

    private fun setupBroadcastReceiver() {
        workoutReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val snarkyMessage = roastViewModel.getSnarkyFeedback()
                Toast.makeText(context, snarkyMessage, Toast.LENGTH_SHORT).show()
            }
        }

        val intentFilter = IntentFilter("com.example.fitforge.WORKOUT_LOGGED")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(workoutReceiver, intentFilter, Context.RECEIVER_EXPORTED)
        } else {
            registerReceiver(workoutReceiver, intentFilter)
        }
    }

    private fun loadUserData() {
        val sharedPrefs = getSharedPreferences("FitForge", Context.MODE_PRIVATE)
        val userName = sharedPrefs.getString("user_name", "Fitness Champion") ?: "Fitness Champion"
        val streak = sharedPrefs.getInt("workout_streak", 0)

        binding.tvGreeting.text = "Hey $userName! 💪"
        binding.tvStreak.text = "Current Streak: $streak days"
        binding.tvRoast.text = roastViewModel.generateRoast(streak)
    }

    private fun setupClickListeners() {
        // FAB for quick workout logging
        binding.fabLogWorkout.setOnClickListener {
            navigateToActivity(LogWorkoutActivity::class.java)
        }

        // Main buttons
        binding.btnLogWorkout.setOnClickListener {
            navigateToActivity(LogWorkoutActivity::class.java)
        }

        binding.btnWorkoutHistory.setOnClickListener {
            navigateToActivity(WorkoutListActivity::class.java)
        }

        binding.btnExercises.setOnClickListener {
            navigateToActivity(ExerciseLibraryActivity::class.java)
        }

        binding.btnStreak.setOnClickListener {
            navigateToActivity(StreakHistoryActivity::class.java)
        }

        binding.btnProfile.setOnClickListener {
            navigateToActivity(ProfileActivity::class.java)
        }
    }

    private fun navigateToActivity(activityClass: Class<*>) {
        val intent = Intent(this, activityClass)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(workoutReceiver)
    }
}
