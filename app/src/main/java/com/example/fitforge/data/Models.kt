package com.example.fitforge.data

data class Workout(
    val id: Int,
    val exerciseName: String,
    val sets: Int,
    val reps: Int,
    val weight: String,
    val date: String,
    val duration: Int // in minutes
)

data class Exercise(
    val id: Int,
    val name: String,
    val muscleGroup: String,
    val difficulty: String,
    val description: String
)
