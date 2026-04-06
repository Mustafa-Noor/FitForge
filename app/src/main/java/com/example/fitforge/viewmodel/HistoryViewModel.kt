package com.example.fitforge.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitforge.data.WorkoutRepository
import com.example.fitforge.data.models.Workout
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted

class HistoryViewModel : ViewModel() {
	private val selectedFilter = MutableStateFlow("All")

	val workouts = combine(
		WorkoutRepository.observeWorkouts(),
		selectedFilter
	) { workouts, filter ->
		if (filter == "All") workouts.sortedByDescending { it.timestamp }
		else workouts.filter { it.muscleGroup == filter }.sortedByDescending { it.timestamp }
	}.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

	val activeFilter = selectedFilter

	fun setFilter(filter: String) {
		selectedFilter.value = filter
	}

	fun deleteWorkout(workout: Workout) {
		WorkoutRepository.deleteWorkout(workout.id)
	}
}

