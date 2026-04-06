package com.example.fitforge.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import com.example.fitforge.ui.theme.FitOrangeLight

@Composable
fun RoastBanner(message: String, modifier: Modifier = Modifier) {
	Card(
		modifier = modifier.fillMaxWidth(),
		colors = CardDefaults.cardColors(containerColor = FitOrangeLight)
	) {
		Text(
			text = message,
			style = MaterialTheme.typography.bodyMedium.copy(fontStyle = FontStyle.Italic),
			modifier = Modifier.padding(14.dp)
		)
	}
}

