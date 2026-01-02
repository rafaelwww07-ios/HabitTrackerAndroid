package com.rafaelmukhametov.habittrackerandroid.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.rafaelmukhametov.habittrackerandroid.HabitTrackerApplication
import com.rafaelmukhametov.habittrackerandroid.domain.model.Habit
import com.rafaelmukhametov.habittrackerandroid.domain.model.HabitReminder
import java.util.Calendar

class NotificationService(private val context: Context) {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    private val notificationManager = NotificationManagerCompat.from(context)
    
    fun scheduleNotifications(habit: Habit) {
        // Remove old notifications first
        removeNotifications(habit.id)
        
        habit.reminders.filter { it.isEnabled }.forEach { reminder ->
            scheduleReminder(habit, reminder)
        }
    }
    
    private fun scheduleReminder(habit: Habit, reminder: HabitReminder) {
        val calendar = Calendar.getInstance()
        val hours = (reminder.time / (60 * 60 * 1000)).toInt()
        val minutes = ((reminder.time % (60 * 60 * 1000)) / (60 * 1000)).toInt()
        
        reminder.daysOfWeek.forEach { dayOfWeek ->
            // Convert day of week (1=Sunday, 2=Monday, etc.) to Calendar format
            val calendarDay = when (dayOfWeek) {
                1 -> Calendar.SUNDAY
                2 -> Calendar.MONDAY
                3 -> Calendar.TUESDAY
                4 -> Calendar.WEDNESDAY
                5 -> Calendar.THURSDAY
                6 -> Calendar.FRIDAY
                7 -> Calendar.SATURDAY
                else -> return@forEach
            }
            
            calendar.set(Calendar.DAY_OF_WEEK, calendarDay)
            calendar.set(Calendar.HOUR_OF_DAY, hours)
            calendar.set(Calendar.MINUTE, minutes)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            
            // If time has passed today, schedule for next week
            if (calendar.timeInMillis <= System.currentTimeMillis()) {
                calendar.add(Calendar.WEEK_OF_YEAR, 1)
            }
            
            val intent = Intent(context, HabitReminderReceiver::class.java).apply {
                putExtra("habit_id", habit.id)
                putExtra("habit_name", habit.name)
                putExtra("day_of_week", dayOfWeek)
            }
            
            val requestCode = (habit.id.hashCode() + dayOfWeek).toString().hashCode()
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            } else {
                alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    AlarmManager.INTERVAL_DAY * 7,
                    pendingIntent
                )
            }
        }
    }
    
    fun removeNotifications(habitId: String) {
        // Cancel all pending intents for this habit
        for (dayOfWeek in 1..7) {
            val requestCode = (habitId.hashCode() + dayOfWeek).toString().hashCode()
            val intent = Intent(context, HabitReminderReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            alarmManager.cancel(pendingIntent)
        }
    }
    
    fun showNotification(habitId: String, habitName: String) {
        val notification = NotificationCompat.Builder(context, "habit_reminder_channel")
            .setSmallIcon(android.R.drawable.ic_menu_info_details)
            .setContentTitle("Время для привычки! 🔔")
            .setContentText("Не забудь выполнить: $habitName")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()
        
        notificationManager.notify(habitId.hashCode(), notification)
    }
}

class HabitReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val habitId = intent.getStringExtra("habit_id") ?: return
        val habitName = intent.getStringExtra("habit_name") ?: return
        
        val notificationService = NotificationService(context)
        notificationService.showNotification(habitId, habitName)
        
        // Reschedule for next week
        val application = context.applicationContext as HabitTrackerApplication
        val database = application.database
        val repository = com.rafaelmukhametov.habittrackerandroid.data.repository.HabitRepository(
            habitDao = database.habitDao(),
            completionDao = database.habitCompletionDao(),
            reminderDao = database.habitReminderDao()
        )
        
            // Re-schedule notification for next occurrence (simplified for now)
    }
}

