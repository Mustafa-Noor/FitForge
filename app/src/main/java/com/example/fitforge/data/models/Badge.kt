package com.example.fitforge.data.models

data class Badge(
	val id: String,
	val title: String,
	val description: String,
	val unlockCondition: String,
	val icon: String,
	val unlocked: Boolean,
	val unlockedDate: String? = null
)

