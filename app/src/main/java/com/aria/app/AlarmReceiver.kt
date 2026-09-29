package com.aria.app
import android.content.*;import android.app.*;import android.os.Bundle
class AlarmReceiver:BroadcastReceiver(){
    override fun onReceive(context:Context,intent:Intent){
        val nm=context.getSystemService(NotificationManager::class.java);val ch="aria_alarm"
        if(android.os.Build.VERSION.SDK_INT>=26)nm.createNotificationChannel(NotificationChannel(ch,"ARIA Alarms",NotificationManager.IMPORTANCE_HIGH))
        val n=Notification.Builder(context,ch).setSmallIcon(android.R.drawable.ic_lock_idle_alarm).setContentTitle("ARIA Alarm").setContentText("Scheduled ARIA alarm").setAutoCancel(true).build()
        nm.notify(intent.getIntExtra("id",0),n)
    }
}
