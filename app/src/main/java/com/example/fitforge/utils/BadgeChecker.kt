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

        // Helper to handle unlocking and point awarding
        fun tryUnlock(id: String, name: String, desc: String, req: String, emoji: String) {
            if (!prefs.isBadgeUnlocked(id)) {
                prefs.unlockBadge(id)
                newlyUnlocked.add(Badge(id, name, desc, req, emoji))
            }
        }

        // Original Badges
        if (totalWorkouts >= 3) {
            tryUnlock("baby_gains", "Baby Gains", "First 3 workouts logged", "3 workouts", "🏅")
        }
        if (currentStreak >= 7) {
            tryUnlock("gym_rat", "Certified Gym Rat", "7 days straight. Built different.", "7-day streak", "🐀")
        }
        if (lastMuscleGroup == "Legs" && previousMuscleGroup == "Legs") {
            tryUnlock("leg_day", "Leg Day Respecter", "Legs two sessions in a row.", "2x Legs", "🦵")
        }
        if (totalWorkouts >= 30) {
            tryUnlock("built_diff", "Built Different", "30 total workouts. Certified.", "30 workouts", "💪")
        }

        // New Badges
        if (currentStreak >= 3) {
            tryUnlock("momentum", "Building Momentum", "3 days in a row. Don't stop now.", "3-day streak", "🚀")
        }

        if (totalWorkouts >= 10) {
            tryUnlock("double_digits", "Double Digits", "10 total workouts. You're getting serious.", "10 workouts", "🔟")
        }

        if (currentStreak >= 14) {
            tryUnlock("discipline", "Pure Discipline", "14 days straight. Most people would have quit.", "14-day streak", "🧘")
        }

        if (totalWorkouts >= 50) {
            tryUnlock("half_century", "The Half Century", "50 total workouts. Elite level consistency.", "50 workouts", "🏛️")
        }

        if (lastMuscleGroup == "Cardio") {
            tryUnlock("heart_health", "Heart of Gold", "Completed a cardio session. The engine is running.", "1x Cardio", "❤️")
        }

        return newlyUnlocked
    }
}
