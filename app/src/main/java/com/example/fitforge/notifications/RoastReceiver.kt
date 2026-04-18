package com.example.fitforge.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.fitforge.data.SharedPreferencesManager
import com.example.fitforge.utils.RoastStrings
import java.time.LocalDate

class RoastReceiver : BroadcastReceiver() {
	override fun onReceive(context: Context, intent: Intent?) {
		val prefs = SharedPreferencesManager(context)
		val lastDateStr = prefs.getLastLoggedDate() ?: return

		val lastDate = runCatching { java.time.LocalDate.parse(lastDateStr) }.getOrNull() ?: return
		val today    = java.time.LocalDate.now()
		val daysSince = java.time.temporal.ChronoUnit.DAYS.between(lastDate, today)

		when {
			daysSince >= 2 && prefs.isRoastEnabled() -> {
				// Reset streak
				prefs.setStreak(0)
				FitNotificationManager.sendRoastNotification(context, RoastStrings.getMissedDayRoast())
			}
			daysSince == 1L && prefs.isReminderEnabled() -> {
				FitNotificationManager.sendReminderNotification(context)
			}
		}
	}
}
