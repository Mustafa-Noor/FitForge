package com.example.fitforge.data

import android.content.Context
import android.content.SharedPreferences
import com.example.fitforge.data.models.Workout
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.time.LocalDate

class SharedPreferencesManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("fitforge_prefs", Context.MODE_PRIVATE)

    // ── Streak ────────────────────────────────────────────
    fun getCurrentStreak(): Int = prefs.getInt("current_streak", 0)
    fun getBestStreak(): Int = prefs.getInt("best_streak", 0)
    fun getLastLoggedDate(): String? = prefs.getString("last_logged_date", null)
    fun getLastMuscleGroup(): String? = prefs.getString("last_muscle_group", null)
    fun getTotalWorkouts(): Int = prefs.getInt("total_workouts", 0)
    fun isFirstLaunch(): Boolean = prefs.getBoolean("is_first_launch", true)
    fun isRoastEnabled(): Boolean = prefs.getBoolean("roast_enabled", true)
    fun isReminderEnabled(): Boolean = prefs.getBoolean("reminder_enabled", false)

    fun setFirstLaunchComplete() = prefs.edit().putBoolean("is_first_launch", false).apply()
    fun setStreak(streak: Int) = prefs.edit().putInt("current_streak", streak).apply()
    fun setBestStreak(streak: Int) = prefs.edit().putInt("best_streak", streak).apply()
    fun setLastLoggedDate(date: String) = prefs.edit().putString("last_logged_date", date).apply()
    fun setLastMuscleGroup(mg: String) = prefs.edit().putString("last_muscle_group", mg).apply()
    fun setTotalWorkouts(total: Int) = prefs.edit().putInt("total_workouts", total).apply()
    fun setRoastEnabled(enabled: Boolean) = prefs.edit().putBoolean("roast_enabled", enabled).apply()
    fun setReminderEnabled(enabled: Boolean) = prefs.edit().putBoolean("reminder_enabled", enabled).apply()

    // ── Loyalty Points ────────────────────────────────────
    fun getLoyaltyPoints(): Int = prefs.getInt("loyalty_points", 0)
    
    fun addLoyaltyPoints(points: Int) {
        val current = getLoyaltyPoints()
        prefs.edit().putInt("loyalty_points", current + points).apply()
    }

    // ── Profile ───────────────────────────────────────────
    fun getProfileImageUri(): String? = prefs.getString("profile_image_uri", null)
    fun setProfileImageUri(uri: String) = prefs.edit().putString("profile_image_uri", uri).apply()
    fun getUsername(): String = prefs.getString("username", "FitForge User") ?: "FitForge User"
    fun setUsername(name: String) = prefs.edit().putString("username", name).apply()

    // ── Goals ─────────────────────────────────────────────
    fun getWeeklyGoal(): Int = prefs.getInt("weekly_goal", 3)
    fun setWeeklyGoal(goal: Int) = prefs.edit().putInt("weekly_goal", goal).apply()

    // ── Workouts (JSON list in SharedPreferences) ─────────
    fun saveWorkout(workout: Workout) {
        val existing = getWorkouts().toMutableList()
        existing.add(0, workout)  // newest first
        prefs.edit().putString("workouts_json", Json.encodeToString(existing)).apply()
    }

    fun getWorkouts(): List<Workout> {
        val json = prefs.getString("workouts_json", "[]") ?: "[]"
        return runCatching { Json.decodeFromString<List<Workout>>(json) }.getOrDefault(emptyList())
    }

    fun updateWorkout(updatedWorkout: Workout) {
        val existing = getWorkouts().toMutableList()
        val index = existing.indexOfFirst { it.id == updatedWorkout.id }
        if (index != -1) {
            existing[index] = updatedWorkout
            prefs.edit().putString("workouts_json", Json.encodeToString(existing)).apply()
        }
    }

    fun deleteWorkout(id: String) {
        val updated = getWorkouts().filter { it.id != id }
        prefs.edit().putString("workouts_json", Json.encodeToString(updated)).apply()
    }

    fun getLatestWorkout(): Workout? = getWorkouts().firstOrNull()

    // ── Challenges ────────────────────────────────────────
    fun getChallengeProgress(challengeId: String): Int {
        return prefs.getInt("challenge_progress_$challengeId", 0) // returns current day index (0-based)
    }

    fun completeChallengeDay(challengeId: String, dayIndex: Int) {
        val currentProgress = getChallengeProgress(challengeId)
        if (dayIndex >= currentProgress) {
            prefs.edit().putInt("challenge_progress_$challengeId", dayIndex + 1).apply()
            // Award higher points for challenge completion (e.g., 20 points per day)
            addLoyaltyPoints(20)
        }
    }

    // ── Badges ────────────────────────────────────────────
    fun isBadgeUnlocked(id: String): Boolean = prefs.getBoolean("badge_$id", false)
    
    fun unlockBadge(id: String) {
        if (!isBadgeUnlocked(id)) {
            prefs.edit().putBoolean("badge_$id", true).apply()
            // Award 50 points for unlocking a badge
            addLoyaltyPoints(50)
        }
    }

    // ── Streak update logic ───────────────────────────────
    fun updateStreakAfterWorkout() {
        val today = LocalDate.now().toString()
        val lastDate = getLastLoggedDate()
        val current = getCurrentStreak()
        var next = current
        
        when {
            lastDate == null -> next = 1
            lastDate == today -> { /* Stay same */ }
            lastDate == LocalDate.now().minusDays(1).toString() -> next = current + 1
            else -> next = 1
        }
        
        setStreak(next)
        setBestStreak(maxOf(next, getBestStreak()))
        setLastLoggedDate(today)
        setTotalWorkouts(getTotalWorkouts() + 1)
        
        // Check for 7-day streak badge
        if (next >= 7) {
            unlockBadge("7_day_streak")
        }
    }

    // ── Clear all ─────────────────────────────────────────
    fun clearAllProgress() {
        prefs.edit()
            .remove("current_streak").remove("best_streak")
            .remove("total_workouts").remove("last_logged_date")
            .remove("last_muscle_group").remove("workouts_json")
            .remove("loyalty_points")
            .remove("badge_baby_gains").remove("badge_gym_rat")
            .remove("badge_leg_day").remove("badge_no_chill")
            .remove("badge_built_diff").remove("badge_ghost")
            .remove("badge_7_day_streak")
            .remove("profile_image_uri")
            .remove("username")
            .remove("weekly_goal")
            .apply()
    }
}
