package com.example.fitforge.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitforge.data.PreferencesManager
import com.example.fitforge.data.WorkoutRepository
import com.example.fitforge.data.models.Badge
import com.example.fitforge.utils.BadgeChecker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProfileUiState(
	val totalWorkouts: Int = 0,
	val currentStreak: Int = 0,
	val bestStreak: Int = 0,
	val badges: List<Badge> = emptyList()
)

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
	private val preferencesManager = PreferencesManager(application)
	private val stateMutable = MutableStateFlow(ProfileUiState())
	val uiState = stateMutable

	init {
		viewModelScope.launch {
			combine(
				WorkoutRepository.observeWorkouts(),
				preferencesManager.currentStreak,
				preferencesManager.bestStreak
			) { workouts, currentStreak, bestStreak ->
				val total = workouts.size
				ProfileUiState(
					totalWorkouts = total,
					currentStreak = currentStreak,
					bestStreak = bestStreak,
					badges = BadgeChecker.checkBadges(total, currentStreak, bestStreak)
				)
			}.collect { state ->
				stateMutable.update { state }
			}
		}
	}
}

