package com.aria.app

import android.content.Context
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UsageStatsReporter(private val context: Context, private val api: ApiClient) {
    suspend fun upload(): Int = withContext(Dispatchers.IO) {
        val day = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
        var count = 0
        UsageStatsRepository(context).daily().forEach { (packageName, millis) ->
            api.saveScreenTime(packageName, millis / 60000.0, day)
            count++
        }
        count
    }
}
