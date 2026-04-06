package com.example.fitforge.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fitforge.ui.theme.FitOrange

@Composable
fun StreakCard(streak: Int, lastLoggedLabel: String, modifier: Modifier = Modifier) {
	Card(modifier = modifier) {
		Column(modifier = Modifier.padding(16.dp)) {
			Text("STREAK", style = MaterialTheme.typography.labelMedium)
			Row {
				Text("🔥", fontSize = 30.sp, modifier = Modifier.padding(end = 8.dp))
				AnimatedContent(
					targetState = streak,
					transitionSpec = { fadeIn() togetherWith fadeOut() },
					label = "streakCounter"
				) { value ->
					Text(value.toString(), style = MaterialTheme.typography.headlineLarge, color = FitOrange)
				}
			}
			Text("day streak", style = MaterialTheme.typography.bodyLarge)
			Text("Last logged: $lastLoggedLabel", style = MaterialTheme.typography.bodySmall)
		}
	}
}

