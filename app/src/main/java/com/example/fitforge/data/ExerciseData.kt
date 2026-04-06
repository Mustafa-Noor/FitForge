package com.example.fitforge.data

import com.example.fitforge.data.models.Exercise

object ExerciseData {
    val exercises: List<Exercise> = listOf(
        Exercise("Bench Press", "Chest", "Compound", "Barbell press for chest, triceps, and shoulders."),
        Exercise("Incline Dumbbell Press", "Chest", "Compound", "Upper-chest focused dumbbell press on an incline bench."),
        Exercise("Cable Fly", "Chest", "Isolation", "Chest isolation movement using constant cable tension."),
        Exercise("Pull-Up", "Back", "Compound", "Bodyweight vertical pull targeting lats and upper back."),
        Exercise("Barbell Row", "Back", "Compound", "Horizontal pull to build back thickness and strength."),
        Exercise("Lat Pulldown", "Back", "Compound", "Machine-based vertical pull for lat development."),
        Exercise("Deadlift", "Back", "Compound", "Full posterior-chain lift emphasizing back and hips."),
        Exercise("Squat", "Legs", "Compound", "Foundational lower-body movement for quads and glutes."),
        Exercise("Romanian Deadlift", "Legs", "Compound", "Hip-hinge focused on hamstrings and glutes."),
        Exercise("Leg Press", "Legs", "Compound", "Machine compound leg movement with stable torso support."),
        Exercise("Leg Extension", "Legs", "Isolation", "Isolates quadriceps through knee extension."),
        Exercise("Overhead Press", "Shoulders", "Compound", "Vertical pressing movement for deltoids and triceps."),
        Exercise("Lateral Raise", "Shoulders", "Isolation", "Targets side delts to build shoulder width."),
        Exercise("Bicep Curl", "Arms", "Isolation", "Elbow flexion movement focused on biceps."),
        Exercise("Tricep Pushdown", "Arms", "Isolation", "Cable extension movement focused on triceps."),
        Exercise("Hammer Curl", "Arms", "Isolation", "Neutral-grip curl emphasizing brachialis and forearms."),
        Exercise("Plank", "Core", "Isometric", "Static core brace to train trunk stability."),
        Exercise("Crunch", "Core", "Isolation", "Abdominal flexion movement for rectus abdominis."),
        Exercise("Running", "Cardio", "Cardio", "Steady-state or interval running for cardiovascular endurance."),
        Exercise("Jump Rope", "Cardio", "Cardio", "High-tempo cardio drill that boosts coordination and conditioning.")
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

