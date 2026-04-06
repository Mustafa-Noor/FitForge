package com.example.fitforge.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.fitforge.data.PreferencesManager
import com.example.fitforge.data.WorkoutRepository
import com.example.fitforge.ui.components.FitForgeTopBar
import com.example.fitforge.ui.components.TopBarNavType
import com.example.fitforge.ui.theme.FitError
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(navController: NavController) {
	val context = LocalContext.current
	val preferences = remember { PreferencesManager(context) }
	val roastEnabled by preferences.roastNotificationsEnabled.collectAsState(initial = true)
	val reminderEnabled by preferences.dailyReminderEnabled.collectAsState(initial = false)
	val scope = rememberCoroutineScope()
	var showClearDialog by remember { mutableStateOf(false) }

	Scaffold(
		topBar = {
			FitForgeTopBar(
				title = "Settings",
				navType = TopBarNavType.Back,
				onNavClick = { navController.popBackStack() }
			)
		}
	) { innerPadding ->
		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(innerPadding)
				.padding(16.dp),
			verticalArrangement = Arrangement.spacedBy(12.dp)
		) {
			Text("NOTIFICATIONS", style = MaterialTheme.typography.labelLarge)
			ToggleCard(
				title = "Roast Notifications",
				subtitle = "Get roasted when you skip",
				checked = roastEnabled,
				onCheckedChange = { enabled -> scope.launch { preferences.setRoastNotifications(enabled) } }
			)
			ToggleCard(
				title = "Daily Reminder",
				subtitle = "Nudge at 7:00 PM if not logged",
				checked = reminderEnabled,
				onCheckedChange = { enabled -> scope.launch { preferences.setDailyReminder(enabled) } }
			)

			Text("ABOUT", style = MaterialTheme.typography.labelLarge)
			Card(modifier = Modifier.fillMaxWidth()) {
				Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
					Text("Version 1.0.0")
					Text("FitForge - Semester Project", style = MaterialTheme.typography.bodySmall)
				}
			}

			Text("DANGER ZONE", style = MaterialTheme.typography.labelLarge, color = FitError)
			Card(
				modifier = Modifier
					.fillMaxWidth()
					.clickable { showClearDialog = true }
			) {
				Row(
					modifier = Modifier.padding(14.dp),
					horizontalArrangement = Arrangement.SpaceBetween,
					verticalAlignment = Alignment.CenterVertically
				) {
					Text("Clear All Data")
					Text(">", style = MaterialTheme.typography.headlineSmall)
				}
			}
		}
	}

	if (showClearDialog) {
		AlertDialog(
			onDismissRequest = { showClearDialog = false },
			title = { Text("Clear all data?") },
			text = { Text("This will remove workouts and streak progress.") },
			confirmButton = {
				TextButton(onClick = {
					scope.launch {
						WorkoutRepository.clearAll()
						preferences.clearAllProgress()
						showClearDialog = false
					}
				}) {
					Text("Confirm")
				}
			},
			dismissButton = {
				TextButton(onClick = { showClearDialog = false }) {
					Text("Cancel")
				}
			}
		)
	}
}

@Composable
private fun ToggleCard(
	title: String,
	subtitle: String,
	checked: Boolean,
	onCheckedChange: (Boolean) -> Unit
) {
	Card(modifier = Modifier.fillMaxWidth()) {
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.padding(14.dp),
			horizontalArrangement = Arrangement.SpaceBetween,
			verticalAlignment = Alignment.CenterVertically
		) {
			Column(modifier = Modifier.weight(1f)) {
				Text(title)
				Text(subtitle, style = MaterialTheme.typography.bodySmall)
			}
			Switch(checked = checked, onCheckedChange = onCheckedChange)
		}
	}
}

