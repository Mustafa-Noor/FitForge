package com.example.fitforge.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fitforge.R
import com.example.fitforge.ui.theme.FitBg
import com.example.fitforge.ui.theme.FitOrange
import kotlinx.coroutines.delay

@Composable
fun WelcomeScreen(onGetStarted: () -> Unit) {
	var stage by remember { mutableStateOf(0) }

	LaunchedEffect(Unit) {
		repeat(5) {
			delay(100)
			stage = it + 1
		}
	}

	Column(
		modifier = Modifier
			.fillMaxSize()
			.background(FitBg)
			.padding(24.dp),
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.Center
	) {
		AnimatedVisibility(
			visible = stage >= 1,
			enter = fadeIn(tween(350)) + slideInVertically(tween(350), initialOffsetY = { it / 3 })
		) {
			Image(
				painter = painterResource(id = R.drawable.ff_gym_logo),
				contentDescription = "FitForge logo",
				modifier = Modifier.height(120.dp)
			)
		}

		Spacer(modifier = Modifier.height(18.dp))

		AnimatedVisibility(visible = stage >= 2, enter = fadeIn(tween(350))) {
			Text("FitForge", fontSize = 40.sp, fontWeight = FontWeight.ExtraBold, color = FitOrange)
		}

		AnimatedVisibility(visible = stage >= 3, enter = fadeIn(tween(350))) {
			Text(
				"No cap. You're built different.",
				style = MaterialTheme.typography.bodyLarge.copy(fontStyle = FontStyle.Italic)
			)
		}

		Spacer(modifier = Modifier.height(18.dp))
		AnimatedVisibility(visible = stage >= 4, enter = fadeIn(tween(350))) {
			Column(horizontalAlignment = Alignment.Start, modifier = Modifier.fillMaxWidth()) {
				Text("What this does", style = MaterialTheme.typography.labelLarge)
				Spacer(modifier = Modifier.height(8.dp))
				Text("🏋 Log your workouts")
				Text("🔥 Track your streak")
				Text("😤 Get roasted when you skip")
			}
		}

		Spacer(modifier = Modifier.height(24.dp))
		AnimatedVisibility(visible = stage >= 5, enter = fadeIn(tween(350))) {
			Button(
				onClick = onGetStarted,
				modifier = Modifier.fillMaxWidth(),
				colors = ButtonDefaults.buttonColors(containerColor = FitOrange)
			) {
				Text("LET'S GET IT")
			}
		}
	}
}

