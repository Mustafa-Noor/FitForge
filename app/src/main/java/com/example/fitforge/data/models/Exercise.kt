package com.example.fitforge.data.models

import kotlinx.serialization.Serializable

@Serializable
data class Exercise(
	val name: String,
	val muscleGroup: String,
	val type: String,
	val description: String = ""
)

