package com.example.fitforge.ui.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
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
import com.example.fitforge.data.ExerciseData
import com.example.fitforge.data.models.Workout
import com.example.fitforge.navigation.FitRoutes
import com.example.fitforge.ui.components.EmptyState
import com.example.fitforge.ui.components.FitForgeTopBar
import com.example.fitforge.ui.components.MuscleChip
import com.example.fitforge.ui.components.TopBarNavType
import com.example.fitforge.ui.components.WorkoutCard
import com.example.fitforge.viewmodel.HistoryViewModel

@Composable
fun HistoryScreen(
	navController: NavController,
	onOpenDrawer: () -> Unit,
	viewModel: HistoryViewModel = viewModel()
) {
	val workouts by viewModel.workouts.collectAsState()
	val activeFilter by viewModel.activeFilter.collectAsState()
	var pendingDelete by remember { mutableStateOf<Workout?>(null) }

	Scaffold(
		topBar = {
			FitForgeTopBar(
				title = "Workout History",
				navType = TopBarNavType.Menu,
				onNavClick = onOpenDrawer
			)
		}
	) { innerPadding ->
		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(innerPadding)
				.padding(16.dp)
		) {
			androidx.compose.foundation.layout.Row(
				modifier = Modifier
					.fillMaxWidth()
					.horizontalScroll(rememberScrollState()),
				horizontalArrangement = Arrangement.spacedBy(8.dp)
			) {
				ExerciseData.muscleFilters.forEach { filter ->
					MuscleChip(label = filter, selected = filter == activeFilter, onClick = { viewModel.setFilter(filter) })
				}
			}

			if (workouts.isEmpty()) {
				EmptyState(
					icon = "🏋",
					title = "Nothing here.",
					subtitle = "The gym waited. It left.",
					buttonLabel = "Log Your First Workout",
					onButtonClick = { navController.navigate(FitRoutes.LOG) },
					modifier = Modifier.weight(1f)
				)
			} else {
				LazyColumn(
					modifier = Modifier
						.fillMaxSize()
						.padding(top = 12.dp),
					verticalArrangement = Arrangement.spacedBy(8.dp)
				) {
					items(workouts, key = { it.id }) { workout ->
						WorkoutCard(workout = workout, onDeleteClick = { pendingDelete = workout })
					}
				}
			}
		}
	}

	if (pendingDelete != null) {
		AlertDialog(
			onDismissRequest = { pendingDelete = null },
			title = { Text("Delete this workout?") },
			text = { Text("This action cannot be undone.", style = MaterialTheme.typography.bodySmall) },
			confirmButton = {
				TextButton(onClick = {
					pendingDelete?.let(viewModel::deleteWorkout)
					pendingDelete = null
				}) {
					Text("Yeah, delete it")
				}
			},
			dismissButton = {
				TextButton(onClick = { pendingDelete = null }) {
					Text("Wait no")
				}
			}
		)
	}
}

