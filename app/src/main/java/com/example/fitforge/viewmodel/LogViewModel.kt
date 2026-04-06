package com.example.fitforge.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitforge.data.PreferencesManager
import com.example.fitforge.data.WorkoutRepository
import com.example.fitforge.notifications.FitNotificationManager
import com.example.fitforge.utils.BadgeChecker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LogFormState(
	val exerciseName: String = "",
	val muscleGroup: String = "Chest",
	val sets: String = "",
	val reps: String = "",
	val weight: String = "",
	val notes: String = "",
	val exerciseNameError: String? = null,
	val setsError: String? = null,
	val repsError: String? = null,
	val restSeconds: Int = 90
)

class LogViewModel(application: Application) : AndroidViewModel(application) {
	private val preferencesManager = PreferencesManager(application)
	private val notificationManager = FitNotificationManager(application)

	val formState = MutableStateFlow(LogFormState())

	fun setPrefilledExerciseName(name: String?) {
		if (!name.isNullOrBlank() && formState.value.exerciseName.isBlank()) {
			formState.update { it.copy(exerciseName = name) }
		}
	}

	fun updateExerciseName(value: String) = formState.update { it.copy(exerciseName = value, exerciseNameError = null) }
	fun updateMuscleGroup(value: String) = formState.update { it.copy(muscleGroup = value) }
	fun updateSets(value: String) = formState.update { it.copy(sets = value, setsError = null) }
	fun updateReps(value: String) = formState.update { it.copy(reps = value, repsError = null) }
	fun updateWeight(value: String) = formState.update { it.copy(weight = value) }
	fun updateNotes(value: String) = formState.update { it.copy(notes = value) }
	fun updateRestSeconds(seconds: Int) = formState.update { it.copy(restSeconds = seconds.coerceAtLeast(0)) }

	fun saveWorkout(onResult: (Boolean, String) -> Unit) {
		val state = formState.value
		val sets = state.sets.toIntOrNull()
		val reps = state.reps.toIntOrNull()
		val nameError = if (state.exerciseName.isBlank()) "Exercise name is required" else null
		val setsError = if (sets == null || sets <= 0) "Sets required" else null
		val repsError = if (reps == null || reps <= 0) "Reps required" else null

		if (nameError != null || setsError != null || repsError != null) {
			formState.update {
				it.copy(
					exerciseNameError = nameError,
					setsError = setsError,
					repsError = repsError
				)
			}
			onResult(false, "Please fix form errors")
			return
		}

		viewModelScope.launch {
			val workout = WorkoutRepository.createWorkout(
				exerciseName = state.exerciseName.trim(),
				muscleGroup = state.muscleGroup,
				sets = sets ?: 0,
				reps = reps ?: 0,
				weightKg = state.weight.toIntOrNull(),
				notes = state.notes.trim().ifBlank { null }
			)
			WorkoutRepository.addWorkout(workout)
			preferencesManager.updateStreakOnWorkoutLogged()

			val total = WorkoutRepository.currentCount()
			val streakValue = preferencesManager.currentStreak.first()
			val bestValue = preferencesManager.bestStreak.first()
			BadgeChecker.checkBadges(totalWorkouts = total, currentStreak = streakValue, bestStreak = bestValue)

			notificationManager.sendWorkoutLoggedNotification()
			onResult(true, "Logged. Built different.")
		}
	}
}

