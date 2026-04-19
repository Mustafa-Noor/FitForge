package com.example.fitforge.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.time.LocalDate

object DateUtils {
	private val historyFormatter = SimpleDateFormat("EEE, MMM d", Locale.getDefault())

	fun formatHistoryDate(dateMillis: Long): String = historyFormatter.format(Date(dateMillis))

	fun getTodayString(): String = LocalDate.now().toString()

	fun relativeLastLoggedLabel(lastLoggedDate: String?): String {
		if (lastLoggedDate.isNullOrBlank()) return "Never"
		val today = getTodayString()
		val yesterday = LocalDate.now().minusDays(1).toString()
		return when (lastLoggedDate) {
			today -> "Today"
			yesterday -> "Yesterday"
			else -> lastLoggedDate
		}
	}
}
