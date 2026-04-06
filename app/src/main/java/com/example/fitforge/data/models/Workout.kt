package com.example.fitforge.data.models

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Workout(
	val id: String = UUID.randomUUID().toString(),
	val exerciseName: String,
	val muscleGroup: String,
	val sets: Int,
	val reps: Int,
	val weightKg: Float,
	val notes: String = "",
	val dateMillis: Long = System.currentTimeMillis()
)

data class WorkoutStats(
	val totalWorkouts: Int = 0,
	val thisWeekSessions: Int = 0,
	val bestStreak: Int = 0
)

