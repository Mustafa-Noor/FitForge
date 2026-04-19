package com.example.fitforge.data

import com.example.fitforge.R
import com.example.fitforge.data.models.*

object ChallengeData {
    val challenges = listOf(
        Challenge(
            id = "7_day_abs",
            title = "7-Day Core Crusher",
            description = "Quick and effective daily core routine to build abdominal strength.",
            totalDays = 7,
            imageResId = R.drawable.core,
            days = generateDays(7, listOf(
                ChallengeExercise("Crunches", "Abs", 3, 15),
                ChallengeExercise("Plank", "Abs", 3, 0, 45),
                ChallengeExercise("Leg Raises", "Abs", 3, 12),
                ChallengeExercise("Mountain Climbers", "Abs", 3, 20)
            ))
        ),
        Challenge(
            id = "30_day_pushup",
            title = "30-Day Push-Up Pro",
            description = "Master the push-up and build chest and tricep power over a month.",
            totalDays = 30,
            imageResId = R.drawable.chest,
            days = generateDays(30, listOf(
                ChallengeExercise("Push-Ups", "Chest", 4, 10),
                ChallengeExercise("Diamond Push-Ups", "Triceps", 3, 8),
                ChallengeExercise("Plank", "Abs", 2, 0, 60)
            ))
        ),
        Challenge(
            id = "14_day_full_body",
            title = "14-Day Full Body Blast",
            description = "Burn fat and build muscle with intense full-body circuits.",
            totalDays = 14,
            imageResId = R.drawable.full_body,
            days = generateDays(14, listOf(
                ChallengeExercise("Burpees", "Full Body", 3, 10),
                ChallengeExercise("Squats", "Legs", 3, 20),
                ChallengeExercise("Push-Ups", "Chest", 3, 15),
                ChallengeExercise("Jumping Jacks", "Full Body", 3, 30)
            ))
        )
    )

    private fun generateDays(count: Int, baseExercises: List<ChallengeExercise>): List<ChallengeDay> {
        return (1..count).map { ChallengeDay(it, baseExercises) }
    }
}
