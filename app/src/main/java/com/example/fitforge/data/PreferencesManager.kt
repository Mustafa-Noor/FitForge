package com.example.fitforge.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

internal val Context.fitForgeDataStore by preferencesDataStore(name = "fitforge_prefs")

object PrefKeys {
	val IS_FIRST_LAUNCH = booleanPreferencesKey("is_first_launch")
	val CURRENT_STREAK = intPreferencesKey("current_streak")
	val BEST_STREAK = intPreferencesKey("best_streak")
	val TOTAL_WORKOUTS = intPreferencesKey("total_workouts")
	val LAST_LOGGED_DATE = stringPreferencesKey("last_logged_date")
	val LAST_MUSCLE_GROUP = stringPreferencesKey("last_muscle_group")
	val ROAST_ENABLED = booleanPreferencesKey("roast_enabled")
	val REMINDER_ENABLED = booleanPreferencesKey("reminder_enabled")
	val WORKOUTS_JSON = stringPreferencesKey("workouts_json")
	val BADGE_BABY_GAINS = booleanPreferencesKey("badge_baby_gains")
	val BADGE_GYM_RAT = booleanPreferencesKey("badge_gym_rat")
	val BADGE_LEG_DAY = booleanPreferencesKey("badge_leg_day")
	val BADGE_NO_CHILL = booleanPreferencesKey("badge_no_chill")
	val BADGE_BUILT_DIFF = booleanPreferencesKey("badge_built_diff")
	val BADGE_GHOST = booleanPreferencesKey("badge_ghost")
}

class PreferencesManager(private val context: Context) {
	val isFirstLaunch: Flow<Boolean> = context.fitForgeDataStore.data.map { prefs ->
		prefs[PrefKeys.IS_FIRST_LAUNCH] ?: true
	}

	val currentStreak: Flow<Int> = context.fitForgeDataStore.data.map { prefs ->
		prefs[PrefKeys.CURRENT_STREAK] ?: 0
	}

	val bestStreak: Flow<Int> = context.fitForgeDataStore.data.map { prefs ->
		prefs[PrefKeys.BEST_STREAK] ?: 0
	}

	val lastLoggedDate: Flow<String?> = context.fitForgeDataStore.data.map { prefs ->
		prefs[PrefKeys.LAST_LOGGED_DATE]
	}

	val lastMuscleGroup: Flow<String?> = context.fitForgeDataStore.data.map { prefs ->
		prefs[PrefKeys.LAST_MUSCLE_GROUP]
	}

	val totalWorkouts: Flow<Int> = context.fitForgeDataStore.data.map { prefs ->
		prefs[PrefKeys.TOTAL_WORKOUTS] ?: 0
	}

	val roastNotificationsEnabled: Flow<Boolean> = context.fitForgeDataStore.data.map { prefs ->
		prefs[PrefKeys.ROAST_ENABLED] ?: true
	}

	val dailyReminderEnabled: Flow<Boolean> = context.fitForgeDataStore.data.map { prefs ->
		prefs[PrefKeys.REMINDER_ENABLED] ?: false
	}

	val badgeStates: Flow<Map<String, Boolean>> = context.fitForgeDataStore.data.map { prefs ->
		mapOf(
			"baby_gains" to (prefs[PrefKeys.BADGE_BABY_GAINS] ?: false),
			"gym_rat" to (prefs[PrefKeys.BADGE_GYM_RAT] ?: false),
			"leg_day" to (prefs[PrefKeys.BADGE_LEG_DAY] ?: false),
			"no_chill" to (prefs[PrefKeys.BADGE_NO_CHILL] ?: false),
			"built_diff" to (prefs[PrefKeys.BADGE_BUILT_DIFF] ?: false),
			"ghost" to (prefs[PrefKeys.BADGE_GHOST] ?: false)
		)
	}

	suspend fun setFirstLaunchComplete() {
		context.fitForgeDataStore.edit { prefs ->
			prefs[PrefKeys.IS_FIRST_LAUNCH] = false
		}
	}

	suspend fun setRoastNotifications(enabled: Boolean) {
		context.fitForgeDataStore.edit { prefs ->
			prefs[PrefKeys.ROAST_ENABLED] = enabled
		}
	}

	suspend fun setDailyReminder(enabled: Boolean) {
		context.fitForgeDataStore.edit { prefs ->
			prefs[PrefKeys.REMINDER_ENABLED] = enabled
		}
	}

	suspend fun setStreak(streak: Int) {
		context.fitForgeDataStore.edit { prefs ->
			prefs[PrefKeys.CURRENT_STREAK] = streak
		}
	}

	suspend fun getCurrentStreak(): Int = context.fitForgeDataStore.data.first()[PrefKeys.CURRENT_STREAK] ?: 0
	suspend fun getBestStreak(): Int = context.fitForgeDataStore.data.first()[PrefKeys.BEST_STREAK] ?: 0
	suspend fun getLastLoggedDate(): String? = context.fitForgeDataStore.data.first()[PrefKeys.LAST_LOGGED_DATE]
	suspend fun getLastMuscleGroup(): String? = context.fitForgeDataStore.data.first()[PrefKeys.LAST_MUSCLE_GROUP]
	suspend fun getTotalWorkouts(): Int = context.fitForgeDataStore.data.first()[PrefKeys.TOTAL_WORKOUTS] ?: 0
	suspend fun getBadge(id: String): Boolean = context.fitForgeDataStore.data.first()[badgeKey(id)] ?: false

	suspend fun setBestStreak(streak: Int) {
		context.fitForgeDataStore.edit { prefs -> prefs[PrefKeys.BEST_STREAK] = streak }
	}

	suspend fun setLastLoggedDate(dateIso: String) {
		context.fitForgeDataStore.edit { prefs -> prefs[PrefKeys.LAST_LOGGED_DATE] = dateIso }
	}

	suspend fun setLastMuscleGroup(muscleGroup: String) {
		context.fitForgeDataStore.edit { prefs -> prefs[PrefKeys.LAST_MUSCLE_GROUP] = muscleGroup }
	}

	suspend fun setTotalWorkouts(total: Int) {
		context.fitForgeDataStore.edit { prefs -> prefs[PrefKeys.TOTAL_WORKOUTS] = total }
	}

	suspend fun unlockBadge(id: String) {
		context.fitForgeDataStore.edit { prefs -> prefs[badgeKey(id)] = true }
	}

	suspend fun updateStreakAfterWorkout(today: LocalDate = LocalDate.now()) {
		val lastDate = getLastLoggedDate()?.let { runCatching { LocalDate.parse(it) }.getOrNull() }
		val current = getCurrentStreak()
		val next = when {
			lastDate == null -> 1
			lastDate == today -> current
			lastDate == today.minusDays(1) -> current + 1
			else -> 1
		}
		setStreak(next)
		setBestStreak(maxOf(next, getBestStreak()))
		setLastLoggedDate(today.toString())
	}

	suspend fun clearAllProgress() {
		context.fitForgeDataStore.edit { prefs ->
			prefs.remove(PrefKeys.CURRENT_STREAK)
			prefs.remove(PrefKeys.BEST_STREAK)
			prefs.remove(PrefKeys.TOTAL_WORKOUTS)
			prefs.remove(PrefKeys.LAST_LOGGED_DATE)
			prefs.remove(PrefKeys.LAST_MUSCLE_GROUP)
			prefs.remove(PrefKeys.WORKOUTS_JSON)
			prefs.remove(PrefKeys.BADGE_BABY_GAINS)
			prefs.remove(PrefKeys.BADGE_GYM_RAT)
			prefs.remove(PrefKeys.BADGE_LEG_DAY)
			prefs.remove(PrefKeys.BADGE_NO_CHILL)
			prefs.remove(PrefKeys.BADGE_BUILT_DIFF)
			prefs.remove(PrefKeys.BADGE_GHOST)
		}
	}

	private fun badgeKey(id: String) = when (id) {
		"baby_gains" -> PrefKeys.BADGE_BABY_GAINS
		"gym_rat" -> PrefKeys.BADGE_GYM_RAT
		"leg_day" -> PrefKeys.BADGE_LEG_DAY
		"no_chill" -> PrefKeys.BADGE_NO_CHILL
		"built_diff" -> PrefKeys.BADGE_BUILT_DIFF
		"ghost" -> PrefKeys.BADGE_GHOST
		else -> booleanPreferencesKey("badge_$id")
	}
}

