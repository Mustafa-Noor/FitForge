package com.example.fitforge.data

import android.content.Context
import com.example.fitforge.data.models.Badge
import com.example.fitforge.data.models.Workout
import com.example.fitforge.data.models.WorkoutStats
import com.example.fitforge.utils.BadgeChecker
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

object WorkoutRepository {
	private lateinit var appContext: Context
	private val json = Json { ignoreUnknownKeys = true; encodeDefaults = true }

	fun initialize(context: Context) {
		appContext = context.applicationContext
	}

	private fun requireContext(): Context {
		check(::appContext.isInitialized) { "WorkoutRepository.initialize(context) must be called first." }
		return appContext
	}

	private fun prefs() = PreferencesManager(requireContext())

	private fun encodeWorkouts(workouts: List<Workout>): String = json.encodeToString(ListSerializer(Workout.serializer()), workouts)
	private fun decodeWorkouts(encoded: String): List<Workout> = runCatching {
		json.decodeFromString(ListSerializer(Workout.serializer()), encoded)
	}.getOrDefault(emptyList())

	fun getWorkoutsFlow(): Flow<List<Workout>> = requireContext().fitForgeDataStore.data.map { prefs ->
		decodeWorkouts(prefs[PrefKeys.WORKOUTS_JSON] ?: "[]")
	}

	fun getWorkoutsFilteredFlow(muscleGroup: String?): Flow<List<Workout>> = getWorkoutsFlow().map { workouts ->
		if (muscleGroup.isNullOrBlank() || muscleGroup == "All") workouts else workouts.filter { it.muscleGroup == muscleGroup }
	}

	fun getLatestFlow(): Flow<Workout?> = getWorkoutsFlow().map { it.firstOrNull() }

	fun getStatsFlow(): Flow<WorkoutStats> = combine(
		getWorkoutsFlow(),
		prefs().currentStreak,
		prefs().bestStreak
	) { workouts, currentStreak, bestStreak ->
		val thisWeekSessions = workouts.count { workout ->
			val workoutDate = Instant.ofEpochMilli(workout.dateMillis).atZone(ZoneId.systemDefault()).toLocalDate()
			!workoutDate.isBefore(LocalDate.now().minusDays(6))
		}
		WorkoutStats(
			totalWorkouts = workouts.size,
			thisWeekSessions = thisWeekSessions,
			bestStreak = maxOf(bestStreak, currentStreak)
		)
	}

	suspend fun getWorkouts(): List<Workout> {
		val encoded = requireContext().fitForgeDataStore.data.first()[PrefKeys.WORKOUTS_JSON] ?: "[]"
		return decodeWorkouts(encoded)
	}

	suspend fun saveWorkout(workout: Workout): List<Badge> {
		val preferencesManager = prefs()
		val current = getWorkouts().toMutableList()
		val previous = current.firstOrNull()
		current.add(0, workout)
		requireContext().fitForgeDataStore.edit { prefs ->
			prefs[PrefKeys.WORKOUTS_JSON] = encodeWorkouts(current)
		}
		preferencesManager.setTotalWorkouts(current.size)
		preferencesManager.setLastMuscleGroup(workout.muscleGroup)
		preferencesManager.updateStreakAfterWorkout(Instant.ofEpochMilli(workout.dateMillis).atZone(ZoneId.systemDefault()).toLocalDate())

		return BadgeChecker.checkBadges(
			totalWorkouts = current.size,
			currentStreak = preferencesManager.getCurrentStreak(),
			lastMuscleGroup = workout.muscleGroup,
			previousMuscleGroup = previous?.muscleGroup,
			prefsManager = preferencesManager
		)
	}

	suspend fun deleteWorkout(workoutId: String) {
		val current = getWorkouts().filterNot { it.id == workoutId }
		requireContext().fitForgeDataStore.edit { prefs ->
			prefs[PrefKeys.WORKOUTS_JSON] = encodeWorkouts(current)
			prefs[PrefKeys.TOTAL_WORKOUTS] = current.size
		}
	}

	suspend fun clearAll() {
		requireContext().fitForgeDataStore.edit { prefs ->
			prefs.remove(PrefKeys.WORKOUTS_JSON)
			prefs.remove(PrefKeys.TOTAL_WORKOUTS)
		}
	}

	fun createWorkout(
		exerciseName: String,
		muscleGroup: String,
		sets: Int,
		reps: Int,
		weightKg: Float,
		notes: String = "",
		dateMillis: Long = System.currentTimeMillis()
	): Workout {
		return Workout(
			exerciseName = exerciseName,
			muscleGroup = muscleGroup,
			sets = sets,
			reps = reps,
			weightKg = weightKg,
			notes = notes,
			dateMillis = dateMillis
		)
	}
}

