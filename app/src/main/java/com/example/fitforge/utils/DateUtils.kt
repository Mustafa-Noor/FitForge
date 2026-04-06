package com.example.fitforge.utils

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object DateUtils {
	private val historyFormatter = DateTimeFormatter.ofPattern("EEE, MMM d")

	fun formatHistoryDate(dateTime: LocalDateTime): String = dateTime.format(historyFormatter)

	fun relativeLastLoggedLabel(lastLoggedDate: String?): String {
		if (lastLoggedDate.isNullOrBlank()) return "Never"
		val loggedDate = runCatching { LocalDate.parse(lastLoggedDate) }.getOrNull() ?: return "Never"
		val today = LocalDate.now()
		return when {
			loggedDate == today -> "Today"
			loggedDate == today.minusDays(1) -> "Yesterday"
			else -> loggedDate.format(DateTimeFormatter.ofPattern("MMM d"))
		}
	}
}

