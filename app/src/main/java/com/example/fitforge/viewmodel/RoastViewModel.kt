package com.example.fitforge.viewmodel

import androidx.lifecycle.ViewModel

class RoastViewModel : ViewModel() {
    private val snarkyMessages = listOf(
        "Workout logged, keep grinding! 🔥",
        "Another rep closer to greatness!",
        "Your future self thanks you 💪",
        "That's what champions do!",
        "Keep the streak alive! 🚀",
        "Gains incoming! 💎",
        "You're on fire today! 🌟",
        "Beast mode activated! 🦾"
    )

    private val roasts = listOf(
        "You skipped a day? Amateur move 😅",
        "Muscles don't build themselves, buddy!",
        "One day off = ten days back. Math checks out.",
        "Rest days are for people who don't want gains.",
        "Your couch called. It misses you. 🛋️",
        "Consistency is key, and you forgot the key 🔑",
        "Even I'm disappointed, and I'm just code. 💔"
    )

    fun getSnarkyFeedback(): String = snarkyMessages.random()

    fun generateRoast(streak: Int): String {
        return if (streak > 0) {
            "🔥 Streak: $streak days! Keep crushing it!"
        } else {
            roasts.random()
        }
    }
}
