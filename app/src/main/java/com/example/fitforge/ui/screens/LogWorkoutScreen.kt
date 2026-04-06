package com.example.fitforge.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.fitforge.data.ExerciseData
import com.example.fitforge.ui.components.FitForgeTopBar
import com.example.fitforge.ui.components.TopBarNavType
import com.example.fitforge.viewmodel.LogViewModel
import kotlinx.coroutines.launch
import androidx.compose.foundation.text.KeyboardOptions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogWorkoutScreen(
	navController: NavController,
	prefilledExercise: String? = null,
	viewModel: LogViewModel = viewModel()
) {
	val form by viewModel.formState.collectAsState()
	val snackbarHostState = remember { SnackbarHostState() }
	val scope = rememberCoroutineScope()
	var isMuscleExpanded by remember { mutableStateOf(false) }
	var showTimerDialog by remember { mutableStateOf(false) }
	var timerSeconds by remember { mutableStateOf(form.restSeconds) }

	LaunchedEffect(prefilledExercise) {
		viewModel.setPrefilledExerciseName(prefilledExercise)
	}

	Scaffold(
		topBar = {
			FitForgeTopBar(title = "Log Workout", navType = TopBarNavType.Back, onNavClick = { navController.popBackStack() })
		},
		snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
	) { innerPadding ->
		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(innerPadding)
				.verticalScroll(rememberScrollState())
				.padding(16.dp),
			verticalArrangement = Arrangement.spacedBy(12.dp)
		) {
			Text("EXERCISE", style = MaterialTheme.typography.labelLarge)
			OutlinedTextField(
				value = form.exerciseName,
				onValueChange = viewModel::updateExerciseName,
				modifier = Modifier.fillMaxWidth(),
				label = { Text("Exercise Name *") },
				placeholder = { Text("e.g. Bench Press") },
				isError = form.exerciseNameError != null,
				supportingText = { form.exerciseNameError?.let { Text(it) } }
			)

			ExposedDropdownMenuBox(expanded = isMuscleExpanded, onExpandedChange = { isMuscleExpanded = it }) {
				OutlinedTextField(
					value = form.muscleGroup,
					onValueChange = {},
					readOnly = true,
					modifier = Modifier
						.fillMaxWidth()
						.menuAnchor(),
					label = { Text("Muscle Group") },
					trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isMuscleExpanded) }
				)
				ExposedDropdownMenu(expanded = isMuscleExpanded, onDismissRequest = { isMuscleExpanded = false }) {
					ExerciseData.muscleFilters.filter { it != "All" }.forEach { filter ->
						DropdownMenuItem(
							text = { Text(filter) },
							onClick = {
								viewModel.updateMuscleGroup(filter)
								isMuscleExpanded = false
							}
						)
					}
				}
			}

			Text("SETS & REPS", style = MaterialTheme.typography.labelLarge)
			Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
				OutlinedTextField(
					value = form.sets,
					onValueChange = viewModel::updateSets,
					modifier = Modifier.weight(1f),
					label = { Text("Sets *") },
					keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
					isError = form.setsError != null
				)
				OutlinedTextField(
					value = form.reps,
					onValueChange = viewModel::updateReps,
					modifier = Modifier.weight(1f),
					label = { Text("Reps *") },
					keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
					isError = form.repsError != null
				)
				OutlinedTextField(
					value = form.weight,
					onValueChange = viewModel::updateWeight,
					modifier = Modifier.weight(1f),
					label = { Text("Weight") },
					keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
				)
			}

			Text("NOTES", style = MaterialTheme.typography.labelLarge)
			OutlinedTextField(
				value = form.notes,
				onValueChange = viewModel::updateNotes,
				modifier = Modifier.fillMaxWidth(),
				label = { Text("Notes (optional)") },
				maxLines = 3
			)

			TextButton(onClick = { showTimerDialog = true }) {
				Text("Set Rest Timer")
			}

			Button(
				onClick = {
					viewModel.saveWorkout { success, message ->
						scope.launch {
							snackbarHostState.showSnackbar(message)
							if (success) {
								navController.popBackStack()
							}
						}
					}
				},
				modifier = Modifier.fillMaxWidth()
			) {
				Text("SAVE WORKOUT")
			}
		}
	}

	if (showTimerDialog) {
		AlertDialog(
			onDismissRequest = { showTimerDialog = false },
			title = { Text("Set Rest Timer") },
			text = {
				Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
					Text("${timerSeconds / 60} min ${timerSeconds % 60} sec")
					Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
						TextButton(onClick = { timerSeconds = (timerSeconds - 15).coerceAtLeast(0) }) { Text("-15s") }
						TextButton(onClick = { timerSeconds += 15 }) { Text("+15s") }
					}
				}
			},
			confirmButton = {
				TextButton(onClick = {
					viewModel.updateRestSeconds(timerSeconds)
					showTimerDialog = false
				}) {
					Text("Start Timer")
				}
			},
			dismissButton = {
				TextButton(onClick = { showTimerDialog = false }) {
					Text("Cancel")
				}
			}
		)
	}
}

