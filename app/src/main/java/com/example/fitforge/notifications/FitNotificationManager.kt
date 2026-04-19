package com.example.fitforge.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.fitforge.R

object FitNotificationManager {
	const val CHANNEL_ROAST = "fitforge_roast"
	const val CHANNEL_REMINDER = "fitforge_reminder"
	const val CHANNEL_HYPE = "fitforge_hype"

	fun createChannels(context: Context) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
		val manager = context.getSystemService(NotificationManager::class.java)
		listOf(
			NotificationChannel(CHANNEL_ROAST, "Roast Notifications", NotificationManager.IMPORTANCE_DEFAULT).apply {
				description = "Gets sent when you skip. You'll deserve it."
			},
			NotificationChannel(CHANNEL_REMINDER, "Daily Reminder", NotificationManager.IMPORTANCE_DEFAULT).apply {
				description = "Gentle nudge to keep your streak"
			},
			NotificationChannel(CHANNEL_HYPE, "Hype Notifications", NotificationManager.IMPORTANCE_DEFAULT).apply {
				description = "Post-workout celebration"
			}
		).forEach { manager?.createNotificationChannel(it) }
	}

	fun sendWorkoutLoggedNotification(context: Context) {
		val notification = NotificationCompat.Builder(context, CHANNEL_HYPE)
			.setSmallIcon(R.drawable.ff_gym_logo)
			.setContentTitle("Workout logged")
			.setContentText("Logged. Built different.")
			.setPriority(NotificationCompat.PRIORITY_DEFAULT)
			.setAutoCancel(true)
			.build()

		NotificationManagerCompat.from(context).notify(1001, notification)
	}

	fun sendRoastNotification(context: Context, message: String) {
		val notification = NotificationCompat.Builder(context, CHANNEL_ROAST)
			.setSmallIcon(R.drawable.ff_gym_logo)
			.setContentTitle("FitForge")
			.setContentText(message)
			.setPriority(NotificationCompat.PRIORITY_DEFAULT)
			.setAutoCancel(true)
			.build()

		NotificationManagerCompat.from(context).notify(1002, notification)
	}

	fun sendReminderNotification(context: Context) {
		val notification = NotificationCompat.Builder(context, CHANNEL_REMINDER)
			.setSmallIcon(R.drawable.ff_gym_logo)
			.setContentTitle("FitForge Reminder")
			.setContentText("You haven't logged today. Time to get to work! 💪")
			.setPriority(NotificationCompat.PRIORITY_HIGH) // Set to High priority for better visibility
			.setDefaults(NotificationCompat.DEFAULT_ALL)
			.setAutoCancel(true)
			.build()

		NotificationManagerCompat.from(context).notify(1003, notification)
	}
}
