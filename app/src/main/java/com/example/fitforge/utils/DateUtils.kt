package com.example.fitforge.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateUtils {
	private val historyFormatter = SimpleDateFormat("EEE, MMM d", Locale.getDefault())

	fun formatHistoryDate(dateMillis: Long): String = historyFormatter.format(Date(dateMillis))

	fun relativeLastLoggedLabel(lastLoggedDate: String?): String {
		if (lastLoggedDate.isNullOrBlank()) return "Never"
		return when (lastLoggedDate) {
			java.time.LocalDate.now().toString() -> "Today"
			java.time.LocalDate.now().minusDays(1).toString() -> "Yesterday"
			else -> lastLoggedDate
		}
	}
}
