package com.aria.app

import android.content.Context
import android.speech.tts.TextToSpeech
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.util.Locale
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class BriefingWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val response = ApiClient().briefing()
            val text = JSONObject(response).optString("briefing", "ARIA morning briefing is ready.")
            speak(text)
            Result.success()
        } catch (_: Exception) {
            Result.retry()
        }
    }

    private fun speak(text: String) {
        val latch = CountDownLatch(1)
        var tts: TextToSpeech? = null
        android.os.Handler(android.os.Looper.getMainLooper()).post {
            tts = TextToSpeech(applicationContext) { status ->
                if (status == TextToSpeech.SUCCESS) {
                    tts?.language = Locale.US
                    tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "aria-briefing")
                }
                latch.countDown()
            }
        }
        latch.await(10, TimeUnit.SECONDS)
        android.os.Handler(android.os.Looper.getMainLooper()).post { tts?.shutdown() }
    }
}
