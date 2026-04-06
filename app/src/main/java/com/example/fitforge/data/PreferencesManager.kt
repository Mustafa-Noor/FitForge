package com.example.fitforge.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

private val Context.fitForgeDataStore by preferencesDataStore(name = "fitforge_prefs")

class PreferencesManager(private val context: Context) {
	private object Keys {
		val isFirstLaunch = booleanPreferencesKey("is_first_launch")
		val currentStreak = intPreferencesKey("current_streak")
		val bestStreak = intPreferencesKey("best_streak")
		val lastLoggedDate = stringPreferencesKey("last_logged_date")
		val roastNotifications = booleanPreferencesKey("roast_notifications")
		val dailyReminder = booleanPreferencesKey("daily_reminder")
	}

	val isFirstLaunch: Flow<Boolean> = context.fitForgeDataStore.data.map { prefs ->
		prefs[Keys.isFirstLaunch] ?: true
	}

	val currentStreak: Flow<Int> = context.fitForgeDataStore.data.map { prefs ->
		prefs[Keys.currentStreak] ?: 0
	}

	val bestStreak: Flow<Int> = context.fitForgeDataStore.data.map { prefs ->
		prefs[Keys.bestStreak] ?: 0
	}

	val lastLoggedDate: Flow<String?> = context.fitForgeDataStore.data.map { prefs ->
		prefs[Keys.lastLoggedDate]
	}

	val roastNotificationsEnabled: Flow<Boolean> = context.fitForgeDataStore.data.map { prefs ->
		prefs[Keys.roastNotifications] ?: true
	}

	val dailyReminderEnabled: Flow<Boolean> = context.fitForgeDataStore.data.map { prefs ->
		prefs[Keys.dailyReminder] ?: false
	}

	suspend fun setFirstLaunchComplete() {
		context.fitForgeDataStore.edit { prefs ->
			prefs[Keys.isFirstLaunch] = false
		}
	}

	suspend fun setRoastNotifications(enabled: Boolean) {
		context.fitForgeDataStore.edit { prefs ->
			prefs[Keys.roastNotifications] = enabled
		}
	}

	suspend fun setDailyReminder(enabled: Boolean) {
		context.fitForgeDataStore.edit { prefs ->
			prefs[Keys.dailyReminder] = enabled
		}
	}

	suspend fun updateStreakOnWorkoutLogged(logDate: LocalDate = LocalDate.now()) {
		context.fitForgeDataStore.edit { prefs ->
			val previousDate = prefs[Keys.lastLoggedDate]?.let { LocalDate.parse(it) }
			val current = prefs[Keys.currentStreak] ?: 0

			val nextStreak = when {
				previousDate == null -> 1
				previousDate == logDate -> current.coerceAtLeast(1)
				previousDate.plusDays(1) == logDate -> current + 1
				else -> 1
			}

			val best = (prefs[Keys.bestStreak] ?: 0).coerceAtLeast(nextStreak)
			prefs[Keys.currentStreak] = nextStreak
			prefs[Keys.bestStreak] = best
			prefs[Keys.lastLoggedDate] = logDate.toString()
		}
	}

	suspend fun clearAllProgress() {
		context.fitForgeDataStore.edit { prefs ->
			prefs.remove(Keys.currentStreak)
			prefs.remove(Keys.bestStreak)
			prefs.remove(Keys.lastLoggedDate)
		}
	}
}

