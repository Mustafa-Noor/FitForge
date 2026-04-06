package com.example.fitforge.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitforge.data.PreferencesManager
import com.example.fitforge.data.WorkoutRepository
import com.example.fitforge.data.models.Workout
import com.example.fitforge.data.models.WorkoutStats
import com.example.fitforge.utils.DateUtils
import com.example.fitforge.utils.RoastStrings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeUiState(
	val streak: Int = 0,
	val bestStreak: Int = 0,
	val lastLoggedLabel: String = "Never",
	val bannerText: String = "",
	val latestWorkout: Workout? = null,
	val stats: WorkoutStats = WorkoutStats()
)

class HomeViewModel(application: Application) : AndroidViewModel(application) {
	private val preferencesManager = PreferencesManager(application)
	private val uiStateMutable = MutableStateFlow(HomeUiState())
	val uiState = uiStateMutable

	init {
		viewModelScope.launch {
			combine(
				preferencesManager.currentStreak,
				preferencesManager.bestStreak,
				preferencesManager.lastLoggedDate,
				WorkoutRepository.observeWorkouts()
			) { streak, bestStreak, lastLoggedDate, workouts ->
				val latest = workouts.maxByOrNull { it.timestamp }
				val total = workouts.size
				val thisWeek = workouts.count {
					!it.timestamp.toLocalDate().isBefore(java.time.LocalDate.now().with(java.time.DayOfWeek.MONDAY))
				}
				HomeUiState(
					streak = streak,
					bestStreak = bestStreak,
					lastLoggedLabel = DateUtils.relativeLastLoggedLabel(lastLoggedDate),
					bannerText = RoastStrings.getHomeBanner(streak, total),
					latestWorkout = latest,
					stats = WorkoutStats(totalWorkouts = total, thisWeekSessions = thisWeek, bestStreak = bestStreak)
				)
			}.collect { newState ->
				uiStateMutable.update { newState }
			}
		}
	}
}

