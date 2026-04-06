package com.example.fitforge.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.fitforge.data.models.Badge
import com.example.fitforge.ui.components.FitForgeTopBar
import com.example.fitforge.ui.components.TopBarNavType
import com.example.fitforge.ui.theme.FitTextDisabled
import com.example.fitforge.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(
	navController: NavController,
	onOpenDrawer: () -> Unit,
	viewModel: ProfileViewModel = viewModel()
) {
	val state by viewModel.uiState.collectAsState()
	var selectedBadge by remember { mutableStateOf<Badge?>(null) }
	val unlockedCount = state.badges.count { it.unlocked }

	Scaffold(
		topBar = {
			FitForgeTopBar(title = "My Profile", navType = TopBarNavType.Menu, onNavClick = onOpenDrawer)
		}
	) { innerPadding ->
		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(innerPadding)
				.padding(16.dp),
			verticalArrangement = Arrangement.spacedBy(12.dp)
		) {
			Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
				ProfileStatCard("Total", state.totalWorkouts.toString(), "Workouts", Modifier.weight(1f))
				ProfileStatCard("Current", "🔥 ${state.currentStreak}", "Streak", Modifier.weight(1f))
				ProfileStatCard("Best", "🔥 ${state.bestStreak}", "Streak", Modifier.weight(1f))
			}

			Text("Achievements", style = MaterialTheme.typography.headlineSmall)
			Text("$unlockedCount of ${state.badges.size} unlocked", style = MaterialTheme.typography.bodySmall)

			LazyVerticalGrid(
				columns = GridCells.Fixed(2),
				horizontalArrangement = Arrangement.spacedBy(8.dp),
				verticalArrangement = Arrangement.spacedBy(8.dp),
				modifier = Modifier.fillMaxSize()
			) {
				items(state.badges, key = { it.id }) { badge ->
					Card(
						modifier = Modifier
							.fillMaxWidth()
							.clickable { selectedBadge = badge }
					) {
						Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
							Text(if (badge.unlocked) badge.icon else "🔒")
							Text(
								badge.title,
								color = if (badge.unlocked) MaterialTheme.colorScheme.onSurface else FitTextDisabled
							)
							Text(
								badge.description,
								style = MaterialTheme.typography.bodySmall,
								color = if (badge.unlocked) MaterialTheme.colorScheme.onSurfaceVariant else FitTextDisabled
							)
							badge.unlockedDate?.let {
								Text("Unlocked $it", style = MaterialTheme.typography.labelSmall)
							}
						}
					}
				}
			}
		}
	}

	if (selectedBadge != null) {
		val badge = selectedBadge ?: return
		AlertDialog(
			onDismissRequest = { selectedBadge = null },
			title = { Text(badge.title) },
			text = { Text("${badge.description}\n\nCondition: ${badge.unlockCondition}") },
			confirmButton = {
				TextButton(onClick = { selectedBadge = null }) {
					Text("Close")
				}
			}
		)
	}
}

@Composable
private fun ProfileStatCard(title: String, value: String, subtitle: String, modifier: Modifier = Modifier) {
	Card(modifier = modifier) {
		Column(modifier = Modifier.padding(12.dp)) {
			Text(title, style = MaterialTheme.typography.labelMedium)
			Text(value, style = MaterialTheme.typography.headlineSmall)
			Text(subtitle, style = MaterialTheme.typography.bodySmall)
		}
	}
}

