package com.example.fitforge.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

enum class TopBarNavType { Menu, Back, None }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FitForgeTopBar(
	title: String,
	navType: TopBarNavType,
	onNavClick: () -> Unit,
	actionIcon: @Composable (() -> Unit)? = null
) {
	CenterAlignedTopAppBar(
		title = { Text(title, style = MaterialTheme.typography.headlineSmall) },
		navigationIcon = {
			when (navType) {
				TopBarNavType.Menu -> {
					IconButton(onClick = onNavClick) {
						Icon(Icons.Default.Menu, contentDescription = "Menu")
					}
				}
				TopBarNavType.Back -> {
					IconButton(onClick = onNavClick) {
						Icon(Icons.Default.ArrowBack, contentDescription = "Back")
					}
				}
				TopBarNavType.None -> Unit
			}
		},
		actions = { actionIcon?.invoke() }
	)
}

