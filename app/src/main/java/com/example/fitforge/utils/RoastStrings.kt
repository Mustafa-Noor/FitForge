package com.example.fitforge.utils

import kotlin.random.Random

object RoastStrings {
	private val coldStart = listOf(
		"No logs yet. The dumbbells feel ghosted.",
		"Zero streak. Zero excuses accepted.",
		"Your future PR is waiting for your first set."
	)

	private val activeStreak = listOf(
		"Bro you actually showed up? Respect.",
		"Consistency looking kinda dangerous.",
		"Keep stacking days. Beast mode loading."
	)

	private val milestoneStreak = listOf(
		"Milestone streak unlocked. Built different confirmed.",
		"Your discipline has main-character energy.",
		"At this point the gym owes you rent."
	)

	private val missedDayRoasts = listOf(
		"You skipped. The barbell noticed.",
		"No session today? Your streak filed a complaint.",
		"Gym missed you. Not in a good way."
	)

	fun getHomeBanner(streak: Int, totalWorkouts: Int): String {
		val pool = when {
			totalWorkouts == 0 || streak == 0 -> coldStart
			streak >= 7 || totalWorkouts >= 25 -> milestoneStreak
			else -> activeStreak
		}
		return pool.random(Random(System.currentTimeMillis()))
	}

	fun getMissedDayRoast(): String = missedDayRoasts.random(Random(System.currentTimeMillis()))
}

