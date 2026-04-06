package com.example.fitforge.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.fitforge.navigation.FitRoutes
import com.example.fitforge.ui.components.EmptyState
import com.example.fitforge.ui.components.FitForgeTopBar
import com.example.fitforge.ui.components.RoastBanner
import com.example.fitforge.ui.components.StreakCard
import com.example.fitforge.ui.components.TopBarNavType
import com.example.fitforge.ui.theme.FitOrange
import com.example.fitforge.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
	navController: NavController,
	onOpenDrawer: () -> Unit,
	viewModel: HomeViewModel = viewModel()
) {
	val state by viewModel.uiState.collectAsState()
	val latestWorkout = state.latestWorkout

	Scaffold(
		topBar = {
			FitForgeTopBar(
				title = "FitForge",
				navType = TopBarNavType.Menu,
				onNavClick = onOpenDrawer,
				actionIcon = {
					IconButton(onClick = { }) {
						Icon(Icons.Default.Notifications, contentDescription = "Notifications")
					}
				}
			)
		},
		floatingActionButton = {
			ExtendedFloatingActionButton(
				text = { Text("Log Workout") },
				icon = { Text("+", style = MaterialTheme.typography.headlineSmall) },
				onClick = { navController.navigate(FitRoutes.LOG) },
				containerColor = FitOrange
			)
		}
	) { innerPadding ->
		LazyColumn(
			modifier = Modifier
				.fillMaxSize()
				.padding(innerPadding)
				.padding(16.dp),
			verticalArrangement = Arrangement.spacedBy(12.dp)
		) {
			item {
				StreakCard(streak = state.streak, lastLoggedLabel = state.lastLoggedLabel)
			}
			item {
				RoastBanner(message = state.bannerText)
			}
			item {
				Card(modifier = Modifier.fillMaxWidth()) {
					Column(modifier = Modifier.padding(14.dp)) {
						Text("TODAY'S WORKOUT", style = MaterialTheme.typography.labelLarge)
						if (latestWorkout != null) {
							Text("${latestWorkout.exerciseName}  ${latestWorkout.sets}x${latestWorkout.reps}")
							Text(
								"View History ->",
								style = MaterialTheme.typography.labelLarge,
								modifier = Modifier.padding(top = 8.dp)
							)
						} else {
							EmptyState(
								icon = "🏋",
								title = "Nothing here.",
								subtitle = "The gym waited. It left.",
								buttonLabel = "Log Your First Workout",
								onButtonClick = { navController.navigate(FitRoutes.LOG) }
							)
						}
					}
				}
			}
			item {
				Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
					StatCard("Total", state.stats.totalWorkouts.toString(), "workouts", Modifier.weight(1f))
					StatCard("This wk", state.stats.thisWeekSessions.toString(), "sessions", Modifier.weight(1f))
					StatCard("Best", "🔥${state.stats.bestStreak}d", "streak", Modifier.weight(1f))
				}
			}
		}
	}
}

@Composable
private fun StatCard(title: String, value: String, subtitle: String, modifier: Modifier = Modifier) {
	Card(modifier = modifier) {
		Column(modifier = Modifier.padding(12.dp)) {
			Text(title, style = MaterialTheme.typography.labelMedium)
			Text(value, style = MaterialTheme.typography.headlineSmall)
			Text(subtitle, style = MaterialTheme.typography.bodySmall)
		}
	}
}

