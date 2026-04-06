package com.example.fitforge.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EmptyState(
	icon: String,
	title: String,
	subtitle: String,
	buttonLabel: String? = null,
	onButtonClick: (() -> Unit)? = null,
	modifier: Modifier = Modifier
) {
	Column(
		modifier = modifier
			.fillMaxWidth()
			.padding(24.dp),
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.spacedBy(8.dp)
	) {
		Text(text = icon, fontSize = 56.sp)
		Text(text = title, style = MaterialTheme.typography.headlineSmall)
		Text(text = subtitle, style = MaterialTheme.typography.bodyMedium)
		if (buttonLabel != null && onButtonClick != null) {
			Button(onClick = onButtonClick, modifier = Modifier.padding(top = 8.dp)) {
				Text(buttonLabel)
			}
		}
	}
}

