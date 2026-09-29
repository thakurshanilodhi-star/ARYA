package com.aria.app
import android.app.*
import android.content.*
import android.os.Build
class AlarmScheduler(private val context: Context) {
    private val manager = context.getSystemService(AlarmManager::class.java)
    private fun pending(id: Int) = PendingIntent.getBroadcast(
        context, id, Intent(context, AlarmReceiver::class.java).putExtra("id", id),
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
    fun schedule(id: Int, at: Long) {
        val p = pending(id)
        try {
            if (Build.VERSION.SDK_INT >= 31 && !manager.canScheduleExactAlarms()) {
                manager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, at, p)
            } else {
                manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, at, p)
            }
        } catch (_: SecurityException) {
            manager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, at, p)
        }
    }
    fun cancel(id: Int) { manager.cancel(pending(id)) }
    fun snooze(id: Int, minutes: Int) { schedule(id, System.currentTimeMillis() + minutes * 60000L) }
}
