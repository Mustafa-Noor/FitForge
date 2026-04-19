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

    // ── Badges ────────────────────────────────────────────
    fun isBadgeUnlocked(id: String): Boolean = prefs.getBoolean("badge_$id", false)
    fun unlockBadge(id: String) = prefs.edit().putBoolean("badge_$id", true).apply()

    // ── Streak update logic ───────────────────────────────
    fun updateStreakAfterWorkout() {
        val today = LocalDate.now().toString()
        val lastDate = getLastLoggedDate()
        val current = getCurrentStreak()
        val next = when {
            lastDate == null -> 1
            lastDate == today -> current
            lastDate == LocalDate.now().minusDays(1).toString() -> current + 1
            else -> 1
        }
        setStreak(next)
        setBestStreak(maxOf(next, getBestStreak()))
        setLastLoggedDate(today)
        setTotalWorkouts(getTotalWorkouts() + 1)
    }

    // ── Clear all ─────────────────────────────────────────
    fun clearAllProgress() {
        prefs.edit()
            .remove("current_streak").remove("best_streak")
            .remove("total_workouts").remove("last_logged_date")
            .remove("last_muscle_group").remove("workouts_json")
            .remove("badge_baby_gains").remove("badge_gym_rat")
            .remove("badge_leg_day").remove("badge_no_chill")
            .remove("badge_built_diff").remove("badge_ghost")
            .remove("profile_image_uri")
            .remove("username")
            .remove("weekly_goal")
            .apply()
    }
}
