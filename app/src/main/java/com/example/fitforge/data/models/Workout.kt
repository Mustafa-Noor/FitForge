package com.example.fitforge.data.models

import java.time.LocalDateTime
import java.util.UUID

data class Workout(
	val id: String = UUID.randomUUID().toString(),
	val exerciseName: String,
	val muscleGroup: String,
	val sets: Int,
	val reps: Int,
	val weightKg: Int?,
	val notes: String?,
	val timestamp: LocalDateTime = LocalDateTime.now()
)

data class WorkoutStats(
	val totalWorkouts: Int = 0,
	val thisWeekSessions: Int = 0,
	val bestStreak: Int = 0
)

