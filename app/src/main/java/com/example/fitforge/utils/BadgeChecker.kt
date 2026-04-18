package com.example.fitforge.utils

import com.example.fitforge.data.SharedPreferencesManager
import com.example.fitforge.data.models.Badge

object BadgeChecker {
	fun checkBadges(
		totalWorkouts: Int,
		currentStreak: Int,
		lastMuscleGroup: String,
		previousMuscleGroup: String,
		prefs: SharedPreferencesManager
	): List<Badge> {
		val newlyUnlocked = mutableListOf<Badge>()

		if (totalWorkouts >= 3 && !prefs.isBadgeUnlocked("baby_gains")) {
			prefs.unlockBadge("baby_gains")
			newlyUnlocked.add(Badge("baby_gains", "Baby Gains", "First 3 workouts logged", "3 workouts", "🏅"))
		}
		if (currentStreak >= 7 && !prefs.isBadgeUnlocked("gym_rat")) {
			prefs.unlockBadge("gym_rat")
			newlyUnlocked.add(Badge("gym_rat", "Certified Gym Rat", "7 days straight. Built different.", "7-day streak", "🐀"))
		}
		if (lastMuscleGroup == "Legs" && previousMuscleGroup == "Legs" && !prefs.isBadgeUnlocked("leg_day")) {
			prefs.unlockBadge("leg_day")
			newlyUnlocked.add(Badge("leg_day", "Leg Day Respecter", "Legs two sessions in a row.", "2x Legs", "🦵"))
		}
		if (totalWorkouts >= 30 && !prefs.isBadgeUnlocked("built_diff")) {
			prefs.unlockBadge("built_diff")
			newlyUnlocked.add(Badge("built_diff", "Built Different", "30 total workouts. Certified.", "30 workouts", "💪"))
		}

		return newlyUnlocked
	}
}
