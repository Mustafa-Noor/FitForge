package com.example.fitforge.data

import com.example.fitforge.data.models.Exercise

object ExerciseData {
    val exercises: List<Exercise> = listOf(
        // === CHEST ===
        Exercise("Push-Ups", "Chest", "Beginner", "Classic bodyweight exercise for chest and triceps."),
        Exercise("Bench Press", "Chest", "Intermediate", "Barbell press for chest, triceps, and shoulders."),
        Exercise("Weighted Dips", "Chest", "Advanced", "Advanced compound movement targeting lower chest and triceps."),

        // === BACK ===
        Exercise("Superman", "Back", "Beginner", "Floor exercise to strengthen lower back and glutes."),
        Exercise("Pull-Up", "Back", "Intermediate", "Bodyweight vertical pull targeting lats and upper back."),
        Exercise("Muscle-Up", "Back", "Advanced", "Explosive pull-up transition into a dip for elite back strength."),

        // === LEGS ===
        Exercise("Bodyweight Squat", "Legs", "Beginner", "Foundational lower-body movement for quads and glutes."),
        Exercise("Bulgarian Split Squat", "Legs", "Intermediate", "Unilateral squat that builds balance and quad power."),
        Exercise("Pistol Squat", "Legs", "Advanced", "Elite single-leg squat requiring extreme strength and mobility."),

        // === SHOULDERS ===
        Exercise("Pike Push-Ups", "Shoulders", "Beginner", "Bodyweight press targeting the shoulders and upper back."),
        Exercise("Overhead Press", "Shoulders", "Intermediate", "Vertical pressing movement for deltoids and triceps."),
        Exercise("Handstand Push-Ups", "Shoulders", "Advanced", "Extreme vertical push requiring core stability and shoulder power."),

        // === ARMS ===
        Exercise("Bench Dips", "Arms", "Beginner", "Tricep-focused dip using a bench or stable surface."),
        Exercise("Tricep Pushdown", "Arms", "Intermediate", "Cable extension movement focused on triceps."),
        Exercise("Close-Grip Chin-Ups", "Arms", "Advanced", "Vertical pull emphasizing bicep recruitment."),

        // === CORE ===
        Exercise("Crunch", "Core", "Beginner", "Abdominal flexion movement for rectus abdominis."),
        Exercise("Leg Raise", "Core", "Intermediate", "Hanging or lying leg lift for lower abdominal strength."),
        Exercise("L-Sit", "Core", "Advanced", "Static holds requiring high levels of core and tricep strength."),

        // === CARDIO ===
        Exercise("Walking", "Cardio", "Beginner", "Steady low-impact cardio for heart health."),
        Exercise("Running", "Cardio", "Intermediate", "Steady-state or interval running for endurance."),
        Exercise("Burpees", "Cardio", "Advanced", "High-intensity full body movement for maximum burn."),

        // === FULL BODY ===
        Exercise("Jumping Jacks", "Full Body", "Beginner", "Full body coordination and light cardio movement."),
        Exercise("Clean and Press", "Full Body", "Intermediate", "Explosive compound lift combining lower and upper body."),
        Exercise("Barbell Thrusters", "Full Body", "Advanced", "Combining a deep squat with an overhead press.")
    )

    val muscleFilters: List<String> = listOf(
        "All",
        "Chest",
        "Back",
        "Legs",
        "Arms",
        "Core",
        "Shoulders",
        "Cardio",
        "Full Body"
    )
}
