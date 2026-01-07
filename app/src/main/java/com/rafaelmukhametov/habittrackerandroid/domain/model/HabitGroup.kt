package com.rafaelmukhametov.habittrackerandroid.domain.model

import java.util.UUID

data class HabitGroup(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String = "",
    val colorHex: String = "#007AFF",
    val iconName: String = "folder.fill",
    val habitIds: List<String> = emptyList(),
    val createdAt: Long = System.currentTimeMillis()
)





