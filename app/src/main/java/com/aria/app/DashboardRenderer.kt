package com.aria.app

import android.content.Context
import android.graphics.Typeface
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import org.json.JSONObject

object DashboardRenderer {
    val names = listOf(
        "Home", "Brain/Chat", "Tasks", "Calendar", "Gym",
        "Career", "Startup", "Competitions", "Research", "Content", "Settings"
    )

    fun render(context: Context, name: String, data: JSONObject): View {
        val box = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(20, 20, 20, 20)
        }
        addCard(box, context, name, titleFor(name))
        addCard(box, context, "Tasks", "Open tasks: ${data.optJSONArray("tasks")?.length() ?: 0}")
        addCard(box, context, "Trackers", "Tracked records: ${data.optJSONArray("trackers")?.length() ?: 0}")
        addCard(box, context, "Drafts", "Drafts: ${data.optJSONArray("drafts")?.length() ?: 0}")
        return box
    }

    private fun addCard(parent: LinearLayout, context: Context, heading: String, body: String) {
        val title = TextView(context).apply {
            text = heading
            textSize = 18f
            typeface = Typeface.DEFAULT_BOLD
        }
        val value = TextView(context).apply {
            text = body
            textSize = 15f
            setPadding(0, 4, 0, 18)
        }
        parent.addView(title)
        parent.addView(value)
    }

    fun titleFor(name: String) = when (name) {
        "Home" -> "Daily command center"
        "Brain/Chat" -> "Gemini online brain with llama.cpp offline failover"
        "Tasks" -> "Tasks, status and deadlines"
        "Calendar" -> "Calendar events and scheduling"
        "Gym" -> "Training progress"
        "Career" -> "Career targets and skills"
        "Startup" -> "Startup metrics"
        "Competitions" -> "Competition targets and deadlines"
        "Research" -> "Research discovery and saved papers"
        "Content" -> "Content drafts and goal gaps"
        else -> "ARIA configuration"
    }
}
