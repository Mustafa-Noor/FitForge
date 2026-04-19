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
		
		// If user HAS NOT logged today, send a notification
		val today = LocalDate.now().toString()
		val lastDateStr = prefs.getLastLoggedDate()
		
		if (lastDateStr != today) {
			if (prefs.isReminderEnabled()) {
				FitNotificationManager.sendReminderNotification(context)
			}
			
			// If it's been 2+ days, roast them
			lastDateStr?.let {
				val lastDate = runCatching { LocalDate.parse(it) }.getOrNull()
				if (lastDate != null) {
					val daysSince = java.time.temporal.ChronoUnit.DAYS.between(lastDate, LocalDate.now())
					if (daysSince >= 2 && prefs.isRoastEnabled()) {
						prefs.setStreak(0)
						FitNotificationManager.sendRoastNotification(context, RoastStrings.getMissedDayRoast())
					}
				}
			}
		}
	}
}
