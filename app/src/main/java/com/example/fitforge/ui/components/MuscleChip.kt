package com.example.fitforge.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.fitforge.ui.theme.FitOrange
import com.example.fitforge.ui.theme.FitSurface
import com.example.fitforge.ui.theme.FitTextSecondary

@Composable
fun MuscleChip(
	label: String,
	selected: Boolean,
	onClick: () -> Unit
) {
	FilterChip(
		selected = selected,
		onClick = onClick,
		label = { Text(label) },
		border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
		colors = FilterChipDefaults.filterChipColors(
			selectedContainerColor = FitOrange,
			selectedLabelColor = Color.White,
			containerColor = FitSurface,
			labelColor = FitTextSecondary
		)
	)
}

