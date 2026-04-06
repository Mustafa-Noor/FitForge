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
				WorkoutRepository.getWorkoutsFlow(),
				preferencesManager.currentStreak,
				preferencesManager.bestStreak,
				preferencesManager.badgeStates
			) { workouts, currentStreak, bestStreak, badgeStates ->
				val total = workouts.size
				val badges = BadgeChecker.allBadges().map { badge ->
					badge.copy(
						isUnlocked = badgeStates[badge.id] ?: false,
						unlockedDateMillis = if (badgeStates[badge.id] == true) System.currentTimeMillis() else null
					)
				}
				ProfileUiState(
					totalWorkouts = total,
					currentStreak = currentStreak,
					bestStreak = bestStreak,
					badges = badges
				)
			}.collect { state ->
				stateMutable.update { state }
			}
		}
	}
}
