package com.example.fitforge.utils

import com.example.fitforge.data.models.Badge
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object BadgeChecker {
	private val unlockedDateLabel: String
		get() = LocalDate.now().format(DateTimeFormatter.ofPattern("MMM d"))

	fun checkBadges(totalWorkouts: Int, currentStreak: Int, bestStreak: Int): List<Badge> {
		val achievedDate = unlockedDateLabel
		return listOf(
			makeBadge("b1", "Baby Gains", "First 3 workouts", "Complete 3 workouts", "🏅", totalWorkouts >= 3, achievedDate),
			makeBadge("b2", "Momentum", "First 10 workouts", "Complete 10 workouts", "⚡", totalWorkouts >= 10, achievedDate),
			makeBadge("b3", "Certified Gym Rat", "7-day streak", "Reach a 7-day streak", "🔥", bestStreak >= 7, achievedDate),
			makeBadge("b4", "Unstoppable", "14-day streak", "Reach a 14-day streak", "🚀", bestStreak >= 14, achievedDate),
			makeBadge("b5", "Iron Mind", "25 total workouts", "Complete 25 workouts", "🧠", totalWorkouts >= 25, achievedDate),
			makeBadge("b6", "No Days Off-ish", "Current streak 3+", "Reach current streak of 3", "😤", currentStreak >= 3, achievedDate)
		)
	}

	private fun makeBadge(
		id: String,
		title: String,
		description: String,
		condition: String,
		icon: String,
		unlocked: Boolean,
		unlockedDate: String
	): Badge {
		return Badge(
			id = id,
			title = title,
			description = description,
			unlockCondition = condition,
			icon = icon,
			unlocked = unlocked,
			unlockedDate = if (unlocked) unlockedDate else null
		)
	}
}

