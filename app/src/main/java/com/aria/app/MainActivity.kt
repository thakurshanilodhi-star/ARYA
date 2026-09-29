package com.aria.app

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    private lateinit var api: ApiClient
    private lateinit var voice: VoiceEngine
    private lateinit var brain: BrainRouter
    private lateinit var chat: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        api = ApiClient()
        brain = BrainRouter(api)
        voice = VoiceEngine(this)
        chat = findViewById(R.id.chat)

        val spinner = findViewById<Spinner>(R.id.dashboardSpinner)
        spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, DashboardRenderer.names)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) = Unit
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                showDashboard(DashboardRenderer.names[position])
            }
        }

        findViewById<Button>(R.id.send).setOnClickListener {
            send(findViewById<EditText>(R.id.input).text.toString())
        }
        findViewById<Button>(R.id.mic).setOnClickListener {
            if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), 10)
            } else {
                voice.listen()
            }
        }
        voice.onResult = { send(it) }
        scheduleBriefing()
    }

    private fun showDashboard(name: String) {
        lifecycleScope.launch {
            val container = findViewById<FrameLayout>(R.id.dashboardContainer)
            try {
                val data = org.json.JSONObject(api.dashboard(name))
                container.removeAllViews()
                container.addView(DashboardRenderer.render(this@MainActivity, name, data))
            } catch (e: Exception) {
                container.removeAllViews()
                val fallback = TextView(this@MainActivity)
                fallback.text = "${DashboardRenderer.titleFor(name)}\nBackend unavailable: ${e.message}"
                fallback.textSize = 16f
                fallback.setPadding(20, 20, 20, 20)
                container.addView(fallback)
            }
        }
    }

    private fun scheduleBriefing() {
        val request = PeriodicWorkRequestBuilder<BriefingWorker>(24, TimeUnit.HOURS).build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "aria-morning-briefing", ExistingPeriodicWorkPolicy.KEEP, request
        )
    }

    private fun send(text: String) {
        if (text.isBlank()) return
        lifecycleScope.launch {
            try {
                val reply = brain.respond(text)
                chat.append("\nARIA: $reply")
                voice.speak(reply)
            } catch (e: Exception) {
                chat.append("\nARIA error: ${e.message}")
            }
        }
    }

    override fun onResume() {
        super.onResume()
        findViewById<ThreeDView>(R.id.threeDView)?.onResume()
    }

    override fun onPause() {
        findViewById<ThreeDView>(R.id.threeDView)?.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        voice.release()
        super.onDestroy()
    }
}
