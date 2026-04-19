package com.example.fitforge.data.models

import kotlinx.serialization.Serializable

@Serializable
data class Challenge(
    val id: String,
    val title: String,
    val description: String,
    val totalDays: Int,
    val imageResId: Int, // Local asset image
    val days: List<ChallengeDay>
)

@Serializable
data class ChallengeDay(
    val dayNumber: Int,
    val exercises: List<ChallengeExercise>,
    var isCompleted: Boolean = false
)

@Serializable
data class ChallengeExercise(
    val exerciseName: String,
    val muscleGroup: String,
    val sets: Int,
    val reps: Int,
    val durationSeconds: Int = 0 // For timed exercises
)
