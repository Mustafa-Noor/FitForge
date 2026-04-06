package com.example.fitforge.data

import com.example.fitforge.data.models.Workout
import com.example.fitforge.data.models.WorkoutStats
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime

object WorkoutRepository {
	private val workoutsState = MutableStateFlow<List<Workout>>(emptyList())

	fun observeWorkouts(): Flow<List<Workout>> = workoutsState

	fun observeLatest(): Flow<Workout?> = workoutsState.map { workouts ->
		workouts.maxByOrNull { it.timestamp }
	}

	fun observeStats(bestStreak: Int): Flow<WorkoutStats> = workoutsState.map { workouts ->
		val today = LocalDate.now()
		val weekStart = today.with(DayOfWeek.MONDAY)
		val thisWeekSessions = workouts.count { workout ->
			!workout.timestamp.toLocalDate().isBefore(weekStart)
		}
		WorkoutStats(
			totalWorkouts = workouts.size,
			thisWeekSessions = thisWeekSessions,
			bestStreak = bestStreak
		)
	}

	fun addWorkout(workout: Workout) {
		workoutsState.value = workoutsState.value + workout
	}

	fun deleteWorkout(workoutId: String) {
		workoutsState.value = workoutsState.value.filterNot { it.id == workoutId }
	}

	fun latestWorkout(): Workout? = workoutsState.value.maxByOrNull { it.timestamp }

	fun currentCount(): Int = workoutsState.value.size

	fun clearAll() {
		workoutsState.value = emptyList()
	}

	fun createWorkout(
		exerciseName: String,
		muscleGroup: String,
		sets: Int,
		reps: Int,
		weightKg: Int?,
		notes: String?
	): Workout {
		return Workout(
			exerciseName = exerciseName,
			muscleGroup = muscleGroup,
			sets = sets,
			reps = reps,
			weightKg = weightKg,
			notes = notes,
			timestamp = LocalDateTime.now()
		)
	}
}

