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

        // Original Badges
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

        // New Badges
        if (currentStreak >= 3 && !prefs.isBadgeUnlocked("momentum")) {
            prefs.unlockBadge("momentum")
            newlyUnlocked.add(Badge("momentum", "Building Momentum", "3 days in a row. Don't stop now.", "3-day streak", "🚀"))
        }

        if (totalWorkouts >= 10 && !prefs.isBadgeUnlocked("double_digits")) {
            prefs.unlockBadge("double_digits")
            newlyUnlocked.add(Badge("double_digits", "Double Digits", "10 total workouts. You're getting serious.", "10 workouts", "🔟"))
        }

        if (currentStreak >= 14 && !prefs.isBadgeUnlocked("discipline")) {
            prefs.unlockBadge("discipline")
            newlyUnlocked.add(Badge("discipline", "Pure Discipline", "14 days straight. Most people would have quit.", "14-day streak", "🧘"))
        }

        if (totalWorkouts >= 50 && !prefs.isBadgeUnlocked("half_century")) {
            prefs.unlockBadge("half_century")
            newlyUnlocked.add(Badge("half_century", "The Half Century", "50 total workouts. Elite level consistency.", "50 workouts", "🏛️"))
        }

        if (lastMuscleGroup == "Cardio" && !prefs.isBadgeUnlocked("heart_health")) {
            prefs.unlockBadge("heart_health")
            newlyUnlocked.add(Badge("heart_health", "Heart of Gold", "Completed a cardio session. The engine is running.", "1x Cardio", "❤️"))
        }

        return newlyUnlocked
    }
}
