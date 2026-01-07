package com.rafaelmukhametov.habittrackerandroid.service

import android.content.Context
import com.rafaelmukhametov.habittrackerandroid.domain.model.Habit
import com.rafaelmukhametov.habittrackerandroid.domain.model.HabitCompletion
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class ExportService(private val context: Context) {
    
    fun exportToCSV(habits: List<Habit>): String {
        var csv = "Name,Description,Category,Created,Total Completions,Current Streak,Success Rate\n"
        
        val dateFormatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        
        habits.forEach { habit ->
            val category = habit.category?.name ?: "No Category"
            val createdDate = dateFormatter.format(Date(habit.createdAt))
            
            csv += "\"${habit.name}\",\"${habit.description}\",\"$category\",\"$createdDate\",\"${habit.completions.size}\",\"${habit.currentStreak()}\",\"${habit.overallCompletionPercentage().toInt()}%\"\n"
        }
        
        return csv
    }
    
    fun exportToJSON(habits: List<Habit>): String {
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        dateFormatter.timeZone = TimeZone.getTimeZone("UTC")
        
        val exportData = habits.map { habit ->
            mapOf(
                "id" to habit.id,
                "name" to habit.name,
                "description" to habit.description,
                "colorHex" to habit.colorHex,
                "iconName" to habit.iconName,
                "category" to (habit.category?.name ?: null),
                "goalType" to habit.goalType.ordinal,
                "goalValue" to habit.goalValue,
                "createdAt" to dateFormatter.format(Date(habit.createdAt)),
                "totalCompletions" to habit.completions.size,
                "currentStreak" to habit.currentStreak(),
                "successRate" to habit.overallCompletionPercentage(),
                "completions" to habit.completions.map { completion ->
                    mapOf(
                        "id" to completion.id,
                        "completedAt" to dateFormatter.format(Date(completion.completedAt)),
                        "notes" to (completion.notes ?: null)
                    )
                }
            )
        }
        
        // Simple JSON serialization (можно использовать Gson для более сложных случаев)
        return buildString {
            append("[\n")
            exportData.forEachIndexed { index, habitData ->
                append("  {\n")
                habitData.forEach { (key, value) ->
                    when (value) {
                        is String -> append("    \"$key\": \"$value\"")
                        is Number -> append("    \"$key\": $value")
                        is Boolean -> append("    \"$key\": $value")
                        is List<*> -> {
                            append("    \"$key\": [\n")
                            value.forEachIndexed { i, item ->
                                if (item is Map<*, *>) {
                                    append("      {\n")
                                    item.forEach { (k, v) ->
                                        when (v) {
                                            is String -> append("        \"$k\": \"$v\"")
                                            is Number -> append("        \"$k\": $v")
                                            null -> append("        \"$k\": null")
                                            else -> append("        \"$k\": \"$v\"")
                                        }
                                        if (item.keys.last() != k) append(",")
                                        append("\n")
                                    }
                                    append("      }")
                                    if (i < value.size - 1) append(",")
                                    append("\n")
                                }
                            }
                            append("    ]")
                        }
                        null -> append("    \"$key\": null")
                        else -> append("    \"$key\": \"$value\"")
                    }
                    if (habitData.keys.last() != key) append(",")
                    append("\n")
                }
                append("  }")
                if (index < exportData.size - 1) append(",")
                append("\n")
            }
            append("]")
        }
    }
    
    fun saveFile(content: String, filename: String): File? {
        return try {
            val file = File(context.getExternalFilesDir(null), filename)
            file.writeText(content)
            file
        } catch (e: Exception) {
            null
        }
    }
    
    fun saveData(data: ByteArray, filename: String): File? {
        return try {
            val file = File(context.getExternalFilesDir(null), filename)
            file.writeBytes(data)
            file
        } catch (e: Exception) {
            null
        }
    }
}



