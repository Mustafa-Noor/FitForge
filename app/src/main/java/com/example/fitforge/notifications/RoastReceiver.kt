package com.example.fitforge.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.fitforge.utils.RoastStrings

class RoastReceiver : BroadcastReceiver() {
	override fun onReceive(context: Context, intent: Intent?) {
		val manager = FitNotificationManager(context)
		manager.sendRoastNotification(RoastStrings.getMissedDayRoast())
	}
}

