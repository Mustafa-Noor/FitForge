package com.example.fitforge

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fitforge.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadProfileData()

        binding.btnSave.setOnClickListener {
            saveProfileData()
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun loadProfileData() {
        val sharedPrefs = getSharedPreferences("FitForge", Context.MODE_PRIVATE)
        val userName = sharedPrefs.getString("user_name", "Fitness Champion") ?: "Fitness Champion"
        val userAge = sharedPrefs.getInt("user_age", 0)
        val userWeight = sharedPrefs.getString("user_weight", "Enter weight") ?: "Enter weight"
        val userGoal = sharedPrefs.getString("user_goal", "Build muscle") ?: "Build muscle"

        binding.etName.setText(userName)
        binding.etAge.setText(if (userAge > 0) userAge.toString() else "")
        binding.etWeight.setText(userWeight)
        binding.etGoal.setText(userGoal)
    }

    private fun saveProfileData() {
        val name = binding.etName.text.toString()
        val age = binding.etAge.text.toString()
        val weight = binding.etWeight.text.toString()
        val goal = binding.etGoal.text.toString()

        if (name.isEmpty()) {
            Toast.makeText(this, "Name cannot be empty!", Toast.LENGTH_SHORT).show()
            return
        }

        val sharedPrefs = getSharedPreferences("FitForge", Context.MODE_PRIVATE)
        sharedPrefs.edit().apply {
            putString("user_name", name)
            putInt("user_age", age.toIntOrNull() ?: 0)
            putString("user_weight", weight)
            putString("user_goal", goal)
        }.apply()

        Toast.makeText(this, "Profile saved!", Toast.LENGTH_SHORT).show()
    }
}
