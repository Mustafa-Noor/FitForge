package com.example.fitforge.utils

import com.example.fitforge.data.PreferencesManager
import com.example.fitforge.data.models.Badge
import kotlinx.coroutines.flow.first
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

object BadgeChecker {
	object Badges {
		val BABY_GAINS = Badge("baby_gains", "Baby Gains", "First 3 workouts", "Complete 3 workouts", "🏅")
		val GYM_RAT = Badge("gym_rat", "Certified Gym Rat", "7-day streak", "Reach a 7-day streak", "🐀")
		val LEG_DAY = Badge("leg_day", "Leg Day Respecter", "Legs logged twice in a row", "Log Legs twice in a row", "🦵")
		val NO_CHILL = Badge("no_chill", "No Chill Mode", "Workout logged before 7am", "Log a workout before 7am", "❄️")
		val BUILT_DIFF = Badge("built_diff", "Built Different", "30 total workouts", "Complete 30 workouts", "💪")
		val GHOST = Badge("ghost", "Ghost Protocol", "Returned after 2+ weeks absent", "Come back after 2+ weeks away", "👻")
		val ALL = listOf(BABY_GAINS, GYM_RAT, LEG_DAY, NO_CHILL, BUILT_DIFF, GHOST)
	}

	suspend fun checkBadges(
		totalWorkouts: Int,
		currentStreak: Int,
		lastMuscleGroup: String,
		previousMuscleGroup: String?,
		prefsManager: PreferencesManager,
		workoutDateMillis: Long = System.currentTimeMillis()
	): List<Badge> {
		val newlyUnlocked = mutableListOf<Badge>()
		val previousDateIso = prefsManager.getLastLoggedDate()
		val previousDate = previousDateIso?.let { runCatching { LocalDate.parse(it) }.getOrNull() }
		val workoutHour = Instant.ofEpochMilli(workoutDateMillis).atZone(ZoneId.systemDefault()).hour

		if (totalWorkouts >= 3 && !prefsManager.getBadge("baby_gains")) {
			prefsManager.unlockBadge("baby_gains")
			newlyUnlocked.add(Badges.BABY_GAINS.copy(isUnlocked = true, unlockedDateMillis = workoutDateMillis))
		}
		if (currentStreak >= 7 && !prefsManager.getBadge("gym_rat")) {
			prefsManager.unlockBadge("gym_rat")
			newlyUnlocked.add(Badges.GYM_RAT.copy(isUnlocked = true, unlockedDateMillis = workoutDateMillis))
		}
		if (lastMuscleGroup == "Legs" && previousMuscleGroup == "Legs" && !prefsManager.getBadge("leg_day")) {
			prefsManager.unlockBadge("leg_day")
			newlyUnlocked.add(Badges.LEG_DAY.copy(isUnlocked = true, unlockedDateMillis = workoutDateMillis))
		}
		if (workoutHour < 7 && !prefsManager.getBadge("no_chill")) {
			prefsManager.unlockBadge("no_chill")
			newlyUnlocked.add(Badges.NO_CHILL.copy(isUnlocked = true, unlockedDateMillis = workoutDateMillis))
		}
		if (totalWorkouts >= 30 && !prefsManager.getBadge("built_diff")) {
			prefsManager.unlockBadge("built_diff")
			newlyUnlocked.add(Badges.BUILT_DIFF.copy(isUnlocked = true, unlockedDateMillis = workoutDateMillis))
		}
		if (previousDate != null && previousDate.isBefore(LocalDate.now().minusDays(14)) && !prefsManager.getBadge("ghost")) {
			prefsManager.unlockBadge("ghost")
			newlyUnlocked.add(Badges.GHOST.copy(isUnlocked = true, unlockedDateMillis = workoutDateMillis))
		}

		return newlyUnlocked
	}

	fun allBadges(): List<Badge> = Badges.ALL
}
