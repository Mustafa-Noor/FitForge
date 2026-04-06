package com.example.fitforge.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.fitforge.R
import com.example.fitforge.navigation.FitRoutes
import com.example.fitforge.ui.theme.FitOrange

private data class DrawerItem(
	val title: String,
	val subtitle: String,
	val route: String,
	val icon: @Composable () -> Unit
)

@Composable
fun FitDrawer(
	currentRoute: String?,
	streak: Int,
	onRouteClick: (String) -> Unit
) {
	val items = listOf(
		DrawerItem("Home", "Today's summary", FitRoutes.HOME) { Icon(Icons.Default.Home, contentDescription = null) },
		DrawerItem("Log Workout", "Track your session", FitRoutes.LOG) { Icon(Icons.Default.Add, contentDescription = null) },
		DrawerItem("History", "Past workouts", FitRoutes.HISTORY) { Icon(Icons.Default.DateRange, contentDescription = null) },
		DrawerItem("Exercise Library", "Browse exercises", FitRoutes.LIBRARY) { Icon(Icons.Default.FitnessCenter, contentDescription = null) },
		DrawerItem("My Profile", "Stats & badges", FitRoutes.PROFILE) { Icon(Icons.Default.Person, contentDescription = null) },
		DrawerItem("Settings", "Notifications & prefs", FitRoutes.SETTINGS) { Icon(Icons.Default.Settings, contentDescription = null) }
	)

	ModalDrawerSheet {
		Column(
			modifier = Modifier
				.fillMaxWidth()
				.height(160.dp)
				.padding(16.dp),
			verticalArrangement = Arrangement.SpaceBetween
		) {
			Column {
				Row(verticalAlignment = Alignment.CenterVertically) {
					Image(
						painter = painterResource(id = R.drawable.ff_gym_logo),
						contentDescription = "FitForge logo",
						modifier = Modifier.size(64.dp)
					)
					Column(modifier = Modifier.padding(start = 12.dp)) {
						Text("FitForge", style = MaterialTheme.typography.headlineMedium, color = FitOrange)
						Text("No cap. Built diff", style = MaterialTheme.typography.bodySmall)
					}
				}
				HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))
				Text("🔥 $streak day streak", color = FitOrange, style = MaterialTheme.typography.labelLarge)
			}
		}

		items.forEach { item ->
			NavigationDrawerItem(
				label = {
					Column {
						Text(item.title)
						Text(item.subtitle, style = MaterialTheme.typography.bodySmall)
					}
				},
				selected = currentRoute == item.route,
				onClick = { onRouteClick(item.route) },
				icon = item.icon,
				modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
			)
		}
	}
}

