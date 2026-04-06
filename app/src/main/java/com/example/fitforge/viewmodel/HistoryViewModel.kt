package com.example.fitforge.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitforge.data.WorkoutRepository
import com.example.fitforge.data.models.Workout
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HistoryViewModel : ViewModel() {
	private val selectedFilter = MutableStateFlow("All")

	val workouts = combine(
		WorkoutRepository.getWorkoutsFlow(),
		selectedFilter
	) { workouts, filter ->
		if (filter == "All") workouts.sortedByDescending { it.dateMillis }
		else workouts.filter { it.muscleGroup == filter }.sortedByDescending { it.dateMillis }
	}.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

	val activeFilter = selectedFilter

	fun setFilter(filter: String) {
		selectedFilter.value = filter
	}

	fun deleteWorkout(workout: Workout) {
		viewModelScope.launch {
			WorkoutRepository.deleteWorkout(workout.id)
		}
	}
}
