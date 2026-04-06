package com.example.fitforge.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.fitforge.data.models.Workout
import com.example.fitforge.utils.DateUtils

@Composable
fun WorkoutCard(
	workout: Workout,
	onDeleteClick: (() -> Unit)? = null,
	modifier: Modifier = Modifier
) {
	Card(modifier = modifier.fillMaxWidth()) {
		Column(modifier = Modifier.padding(14.dp)) {
			Row(
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.SpaceBetween,
				verticalAlignment = Alignment.CenterVertically
			) {
				Row(verticalAlignment = Alignment.CenterVertically) {
					Icon(Icons.Default.FitnessCenter, contentDescription = null)
					Text(
						text = workout.exerciseName,
						style = MaterialTheme.typography.labelLarge,
						modifier = Modifier.padding(start = 8.dp)
					)
				}
				Text(DateUtils.formatHistoryDate(workout.dateMillis), style = MaterialTheme.typography.bodySmall)
			}
			Text("${workout.muscleGroup}  •  ${workout.sets} sets × ${workout.reps} reps")
			Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
				Text(if (workout.weightKg > 0f) "${workout.weightKg} kg" else "Bodyweight", style = MaterialTheme.typography.bodySmall)
				if (onDeleteClick != null) {
					IconButton(onClick = onDeleteClick) {
						Icon(Icons.Default.Delete, contentDescription = "Delete workout")
					}
				}
			}
		}
	}
}
