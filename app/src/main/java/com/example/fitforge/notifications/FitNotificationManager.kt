package com.example.fitforge.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.fitforge.R

class FitNotificationManager(private val context: Context) {
	companion object {
		private const val CHANNEL_ID = "fitforge_updates"
		private const val CHANNEL_NAME = "FitForge Updates"
		private const val CHANNEL_DESC = "Workout confirmations and roast reminders"
	}

	fun createChannelIfNeeded() {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
		val channel = NotificationChannel(
			CHANNEL_ID,
			CHANNEL_NAME,
			NotificationManager.IMPORTANCE_DEFAULT
		).apply {
			description = CHANNEL_DESC
		}

		val manager = context.getSystemService(NotificationManager::class.java)
		manager?.createNotificationChannel(channel)
	}

	fun sendWorkoutLoggedNotification() {
		createChannelIfNeeded()
		val notification = NotificationCompat.Builder(context, CHANNEL_ID)
			.setSmallIcon(R.drawable.ff_gym_logo)
			.setContentTitle("Workout logged")
			.setContentText("Logged. Built different.")
			.setPriority(NotificationCompat.PRIORITY_DEFAULT)
			.setAutoCancel(true)
			.build()

		NotificationManagerCompat.from(context).notify(1001, notification)
	}

	fun sendRoastNotification(message: String) {
		createChannelIfNeeded()
		val notification = NotificationCompat.Builder(context, CHANNEL_ID)
			.setSmallIcon(R.drawable.ff_gym_logo)
			.setContentTitle("FitForge")
			.setContentText(message)
			.setPriority(NotificationCompat.PRIORITY_DEFAULT)
			.setAutoCancel(true)
			.build()

		NotificationManagerCompat.from(context).notify(1002, notification)
	}
}

