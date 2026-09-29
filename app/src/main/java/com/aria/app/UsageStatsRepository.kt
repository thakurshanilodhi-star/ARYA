package com.aria.app
import android.app.usage.UsageStatsManager
import android.content.Context
class UsageStatsRepository(private val context:Context){
 fun daily():List<Pair<String,Long>>{val m=context.getSystemService(UsageStatsManager::class.java);val end=System.currentTimeMillis();val start=end-86400000L;return m.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,start,end).map{it.packageName to it.totalTimeInForeground}}
}
