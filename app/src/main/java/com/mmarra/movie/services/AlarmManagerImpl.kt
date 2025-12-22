package com.mmarra.movie.services

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.mmarra.domain.repository.AlarmManager
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import java.util.Calendar
import android.app.AlarmManager as AndroidAlarmManager

class AlarmManagerImpl @Inject constructor(
    @param:ApplicationContext private val context: Context,
) : AlarmManager {

    override fun schedule(notificationTime: String) {
        val parts = notificationTime.split(":")

        val hours = parts[0].toInt()
        val minutes = parts[1].toInt()

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hours)
            set(Calendar.MINUTE, minutes)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        if (calendar.timeInMillis <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        val intent = Intent(context, ReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            100,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager: AndroidAlarmManager =
            context.getSystemService(android.app.AlarmManager::class.java)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
            !alarmManager.canScheduleExactAlarms()
        ) {
            return
        }

        val triggerTime = calendar.timeInMillis

        alarmManager.setExactAndAllowWhileIdle(
            android.app.AlarmManager.RTC_WAKEUP,
            triggerTime,
            pendingIntent
        )
    }
}