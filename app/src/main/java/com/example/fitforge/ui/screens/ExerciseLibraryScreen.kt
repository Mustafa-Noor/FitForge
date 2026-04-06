package com.example.fitforge.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fitforge.data.ExerciseData
import com.example.fitforge.data.models.Exercise
import com.example.fitforge.navigation.FitRoutes
import com.example.fitforge.ui.components.FitForgeTopBar
import com.example.fitforge.ui.components.MuscleChip
import com.example.fitforge.ui.components.TopBarNavType
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseLibraryScreen(navController: NavController, onOpenDrawer: () -> Unit) {
	var searchMode by remember { mutableStateOf(false) }
	var query by remember { mutableStateOf("") }
	var selectedFilter by remember { mutableStateOf("All") }
	var selectedExercise by remember { mutableStateOf<Exercise?>(null) }

	val filtered = ExerciseData.exercises.filter { exercise ->
		val filterMatch = selectedFilter == "All" || exercise.muscleGroup == selectedFilter
		val queryMatch = query.isBlank() || exercise.name.contains(query, ignoreCase = true)
		filterMatch && queryMatch
	}

	Scaffold(
		topBar = {
			FitForgeTopBar(
				title = "Exercise Library",
				navType = TopBarNavType.Menu,
				onNavClick = onOpenDrawer,
				actionIcon = {
					IconButton(onClick = { searchMode = !searchMode }) {
						Icon(Icons.Default.Search, contentDescription = "Search")
					}
				}
			)
		}
	) { innerPadding ->
		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(innerPadding)
				.padding(16.dp)
		) {
			if (searchMode) {
				OutlinedTextField(
					value = query,
					onValueChange = { query = it },
					modifier = Modifier.fillMaxWidth(),
					label = { Text("Search exercises") }
				)
			}

			Row(
				modifier = Modifier
					.fillMaxWidth()
					.padding(vertical = 12.dp)
					.horizontalScroll(rememberScrollState()),
				horizontalArrangement = Arrangement.spacedBy(8.dp)
			) {
				ExerciseData.muscleFilters.forEach { filter ->
					MuscleChip(label = filter, selected = selectedFilter == filter, onClick = { selectedFilter = filter })
				}
			}

			LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
				items(filtered, key = { it.name }) { exercise ->
					Card(
						modifier = Modifier
							.fillMaxWidth()
							.clickable { selectedExercise = exercise }
					) {
						Row(
							modifier = Modifier.padding(12.dp),
							verticalAlignment = Alignment.CenterVertically
						) {
							Icon(Icons.Default.FitnessCenter, contentDescription = null)
							Column(modifier = Modifier.padding(start = 10.dp)) {
								Text(exercise.name, style = MaterialTheme.typography.labelLarge)
								Text("${exercise.muscleGroup}  •  ${exercise.type}", style = MaterialTheme.typography.bodySmall)
							}
						}
					}
				}
			}
		}
	}

	if (selectedExercise != null) {
		ModalBottomSheet(onDismissRequest = { selectedExercise = null }) {
			val exercise = selectedExercise ?: return@ModalBottomSheet
			Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
				Text(exercise.name, style = MaterialTheme.typography.headlineMedium)
				Text("${exercise.muscleGroup} • ${exercise.type}")
				Text(exercise.description)
				Button(
					onClick = {
						val encoded = URLEncoder.encode(exercise.name, StandardCharsets.UTF_8.toString())
						navController.navigate("${FitRoutes.LOG}?exercise=$encoded")
						selectedExercise = null
					},
					modifier = Modifier.fillMaxWidth()
				) {
					Text("Log This Exercise")
				}
			}
		}
	}
}

