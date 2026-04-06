package com.example.fitforge.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.fitforge.data.PreferencesManager
import com.example.fitforge.utils.RoastStrings
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.time.LocalDate

class RoastReceiver : BroadcastReceiver() {
	override fun onReceive(context: Context, intent: Intent?) {
		val preferencesManager = PreferencesManager(context)
		val manager = FitNotificationManager(context)
		runBlocking {
			val lastLogged = preferencesManager.getLastLoggedDate()?.let { runCatching { LocalDate.parse(it) }.getOrNull() }
			val today = LocalDate.now()
			val daysSince = lastLogged?.let { java.time.temporal.ChronoUnit.DAYS.between(it, today).toInt() } ?: Int.MAX_VALUE

			when {
				daysSince >= 2 && (preferencesManager.roastNotificationsEnabled.first()) -> {
					manager.sendRoastNotification(RoastStrings.getMissedDayRoast())
					preferencesManager.setStreak(0)
				}
				daysSince == 1 && (preferencesManager.dailyReminderEnabled.first()) -> {
					manager.sendReminderNotification("You haven't logged today. 7 PM check-in time.")
				}
			}
		}
	}
}
