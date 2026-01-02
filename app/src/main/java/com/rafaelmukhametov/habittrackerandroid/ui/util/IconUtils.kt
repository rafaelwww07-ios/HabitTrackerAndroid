package com.rafaelmukhametov.habittrackerandroid.ui.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Получить ImageVector для иконки по имени (SF Symbols -> Material Icons mapping)
 */
fun getIconImageVector(iconName: String): ImageVector {
    return when (iconName.lowercase()) {
        "star", "star.fill" -> Icons.Filled.Star
        "heart.fill", "favorite" -> Icons.Filled.Favorite
        "flame.fill", "whatshot" -> Icons.Filled.Favorite // Using Favorite as fallback
        "leaf.fill", "eco" -> Icons.Filled.Star // Using Star as fallback
        "figure.run", "directions_run" -> Icons.Filled.Star // Using Star as fallback
        "dumbbell.fill", "fitness_center" -> Icons.Filled.Star // Using Star as fallback
        "book.fill", "menu_book" -> Icons.Filled.Star // Using Star as fallback
        "pencil", "create", "edit" -> Icons.Filled.Edit
        "moon.fill", "nights_stay" -> Icons.Filled.Star // Using Star as fallback
        "sun.max.fill", "wb_sunny" -> Icons.Filled.Star // Using Star as fallback
        "drop.fill", "opacity" -> Icons.Filled.Star // Using Star as fallback
        "airplane", "flight" -> Icons.Filled.Flight
        "gamecontroller.fill", "videogame_asset" -> Icons.Filled.Star // Using Star as fallback
        "music.note", "music_note" -> Icons.Filled.MusicNote
        "camera.fill", "camera_alt" -> Icons.Filled.CameraAlt
        "brain.head.profile", "psychology" -> Icons.Filled.Star // Using Star as fallback
        "folder.fill" -> Icons.Filled.Folder
        "bolt.fill" -> Icons.Filled.Star // Using Star as fallback
        "person.fill" -> Icons.Filled.Person
        "person.2.fill", "person_2.fill" -> Icons.Filled.Person // Using Person as fallback
        "paintbrush.fill", "paintbrush" -> Icons.Filled.Star // Using Star as fallback
        "dollarsign.circle.fill", "attach_money" -> Icons.Filled.AttachMoney
        "ellipsis.circle.fill" -> Icons.Filled.MoreHoriz
        "briefcase.fill", "work" -> Icons.Filled.Star // Using Star as fallback
        "list.bullet", "list_bullet" -> Icons.Filled.List
        else -> Icons.Filled.Star // Default icon
    }
}

/**
 * Получить эмодзи для иконки (fallback для старых мест, где еще используется текст)
 */
fun getIconEmoji(iconName: String): String {
    return when (iconName.lowercase()) {
        "star", "star.fill" -> "⭐"
        "favorite", "heart.fill" -> "❤️"
        "whatshot", "flame.fill" -> "🔥"
        "eco", "leaf.fill" -> "🌱"
        "directions_run", "figure.run" -> "🏃"
        "fitness_center", "dumbbell.fill" -> "💪"
        "menu_book", "book.fill" -> "📚"
        "create", "pencil" -> "✏️"
        "nights_stay", "moon.fill" -> "🌙"
        "wb_sunny", "sun.max.fill" -> "☀️"
        "opacity", "drop.fill" -> "💧"
        "flight", "airplane" -> "✈️"
        "videogame_asset", "gamecontroller.fill" -> "🎮"
        "music_note", "music.note" -> "🎵"
        "camera_alt", "camera.fill" -> "📷"
        "psychology", "brain.head.profile" -> "🧠"
        "folder.fill" -> "📁"
        "bolt.fill" -> "⚡"
        "person.fill" -> "👤"
        "person.2.fill", "person_2.fill" -> "👥"
        "paintbrush.fill" -> "🎨"
        "dollarsign.circle.fill", "attach_money" -> "💰"
        "ellipsis.circle.fill" -> "⋯"
        "briefcase.fill", "work" -> "💼"
        "list.bullet", "list_bullet" -> "📋"
        else -> "⭐"
    }
}
