package com.example.fitforge.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.fitforge.data.PreferencesManager
import com.example.fitforge.ui.components.FitDrawer
import com.example.fitforge.ui.screens.ExerciseLibraryScreen
import com.example.fitforge.ui.screens.HistoryScreen
import com.example.fitforge.ui.screens.HomeScreen
import com.example.fitforge.ui.screens.LogWorkoutScreen
import com.example.fitforge.ui.screens.ProfileScreen
import com.example.fitforge.ui.screens.SettingsScreen
import kotlinx.coroutines.launch

object FitRoutes {
    const val SPLASH = "splash"
    const val WELCOME = "welcome"
    const val HOME = "home"
    const val LOG = "log"
    const val LOG_WITH_ARG = "log?exercise={exercise}"
    const val HISTORY = "history"
    const val LIBRARY = "library"
    const val PROFILE = "profile"
    const val SETTINGS = "settings"
}

@Composable
fun FitNavGraph() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val preferences = remember { PreferencesManager(context) }
    val streak by preferences.currentStreak.collectAsState(initial = 0)
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            FitDrawer(
                currentRoute = currentRoute,
                streak = streak,
                onRouteClick = { route ->
                    scope.launch {
                        drawerState.close()
                        navController.navigate(route) {
                            launchSingleTop = true
                        }
                    }
                }
            )
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = FitRoutes.HOME,
            modifier = Modifier.fillMaxSize()
        ) {
            composable(FitRoutes.HOME) {
                HomeScreen(navController = navController, onOpenDrawer = { scope.launch { drawerState.open() } })
            }
            composable(
                route = FitRoutes.LOG_WITH_ARG,
                arguments = listOf(navArgument("exercise") {
                    type = NavType.StringType
                    defaultValue = ""
                    nullable = true
                })
            ) { entry ->
                LogWorkoutScreen(
                    navController = navController,
                    prefilledExercise = entry.arguments?.getString("exercise")
                )
            }
            composable(FitRoutes.HISTORY) {
                HistoryScreen(navController = navController, onOpenDrawer = { scope.launch { drawerState.open() } })
            }
            composable(FitRoutes.LIBRARY) {
                ExerciseLibraryScreen(navController = navController, onOpenDrawer = { scope.launch { drawerState.open() } })
            }
            composable(FitRoutes.PROFILE) {
                ProfileScreen(navController = navController, onOpenDrawer = { scope.launch { drawerState.open() } })
            }
            composable(FitRoutes.SETTINGS) {
                SettingsScreen(navController = navController)
            }
        }
    }
}

