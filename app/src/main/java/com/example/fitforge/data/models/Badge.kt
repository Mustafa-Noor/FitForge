package com.example.fitforge.data.models

import kotlinx.serialization.Serializable

@Serializable
data class Badge(
	val id: String,
	val name: String,
	val description: String,
	val unlockCondition: String,
	val emoji: String,
	val isUnlocked: Boolean = false,
	val unlockedDateMillis: Long? = null
)

