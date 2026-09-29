package com.aria.app

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.net.URLEncoder

class ApiClient(private val baseUrl: String = "http://127.0.0.1:8000") {
    private val client = OkHttpClient()

    private suspend fun request(method: String, path: String, body: String? = null): String =
        withContext(Dispatchers.IO) {
            val requestBody = body?.toRequestBody("application/json".toMediaType())
            val builder = Request.Builder().url(baseUrl + path)
            when (method) {
                "GET" -> builder.get()
                "POST" -> builder.post(requestBody ?: "{}".toRequestBody("application/json".toMediaType()))
                "PUT" -> builder.put(requestBody ?: "{}".toRequestBody("application/json".toMediaType()))
                "DELETE" -> builder.delete()
                else -> error("Unsupported HTTP method: $method")
            }
            client.newCall(builder.build()).execute().use { response ->
                if (!response.isSuccessful) error("HTTP ${response.code}")
                response.body?.string() ?: "{}"
            }
        }

    suspend fun health() = request("GET", "/health")
    suspend fun chat(text: String) = request("POST", "/api/chat", JSONObject().put("text", text).toString())

    suspend fun tasks() = request("GET", "/api/tasks")
    suspend fun createTask(title: String, status: String = "OPEN", dueAt: Long? = null) =
        request("POST", "/api/tasks", JSONObject().put("title", title).put("status", status).put("due_at", dueAt).toString())
    suspend fun updateTask(id: Int, title: String, status: String, dueAt: Long? = null) =
        request("PUT", "/api/tasks/$id", JSONObject().put("title", title).put("status", status).put("due_at", dueAt).toString())
    suspend fun deleteTask(id: Int) = request("DELETE", "/api/tasks/$id")

    suspend fun calendar() = request("GET", "/api/calendar")
    suspend fun createCalendar(title: String, startAt: Long, endAt: Long, notes: String = "") =
        request("POST", "/api/calendar", JSONObject().put("title", title).put("start_at", startAt).put("end_at", endAt).put("notes", notes).toString())
    suspend fun updateCalendar(id: Int, title: String, startAt: Long, endAt: Long, notes: String = "") =
        request("PUT", "/api/calendar/$id", JSONObject().put("title", title).put("start_at", startAt).put("end_at", endAt).put("notes", notes).toString())
    suspend fun deleteCalendar(id: Int) = request("DELETE", "/api/calendar/$id")

    suspend fun alarms() = request("GET", "/api/alarms")
    suspend fun createAlarm(title: String, fireAt: Long, snoozeMinutes: Int = 5) =
        request("POST", "/api/alarms", JSONObject().put("title", title).put("fire_at", fireAt).put("snooze_minutes", snoozeMinutes).toString())
    suspend fun deleteAlarm(id: Int) = request("DELETE", "/api/alarms/$id")
    suspend fun snoozeAlarm(id: Int) = request("POST", "/api/alarms/$id/snooze", "{}")

    suspend fun trackers(category: String) = request("GET", "/api/trackers/${enc(category)}")
    suspend fun createTracker(category: String, title: String, value: Double, target: Double) =
        request("POST", "/api/trackers/${enc(category)}", JSONObject().put("title", title).put("value", value).put("target", target).toString())
    suspend fun updateTracker(category: String, id: Int, title: String, value: Double, target: Double) =
        request("PUT", "/api/trackers/${enc(category)}/$id", JSONObject().put("title", title).put("value", value).put("target", target).toString())
    suspend fun deleteTracker(category: String, id: Int) = request("DELETE", "/api/trackers/${enc(category)}/$id")

    suspend fun researchArxiv(q: String) = request("GET", "/api/research/arxiv?q=${enc(q)}")
    suspend fun researchSemantic(q: String) = request("GET", "/api/research/semantic?q=${enc(q)}")
    suspend fun researchRss(url: String) = request("GET", "/api/research/rss?url=${enc(url)}")
    suspend fun saveResearch(source: String, externalId: String, title: String, url: String) =
        request("POST", "/api/research/save", JSONObject().put("source", source).put("external_id", externalId).put("title", title).put("url", url).toString())
    suspend fun summarizeResearch(text: String) = request("POST", "/api/research/summarize?text=${enc(text)}")

    suspend fun analyzeContent(text: String, goals: List<String>): String {
        val array = org.json.JSONArray()
        goals.forEach(array::put)
        return request("POST", "/api/content/analyze", JSONObject().put("text", text).put("goals", array).toString())
    }

    suspend fun drafts() = request("GET", "/api/drafts")
    suspend fun createDraft(platform: String, title: String, body: String, status: String = "DRAFT") =
        request("POST", "/api/drafts", JSONObject().put("platform", platform).put("title", title).put("body", body).put("status", status).toString())
    suspend fun updateDraft(id: Int, platform: String, title: String, body: String, status: String) =
        request("PUT", "/api/drafts/$id", JSONObject().put("platform", platform).put("title", title).put("body", body).put("status", status).toString())
    suspend fun deleteDraft(id: Int) = request("DELETE", "/api/drafts/$id")

    suspend fun publishGithub(owner: String, repo: String, path: String, content: String, message: String) =
        request("POST", "/api/publish/github?owner=${enc(owner)}&repo=${enc(repo)}&path=${enc(path)}&content=${enc(content)}&message=${enc(message)}")
    suspend fun publishLinkedIn(text: String) = request("POST", "/api/publish/linkedin?text=${enc(text)}")
    suspend fun kaggleStatus() = request("GET", "/api/integrations/kaggle")

    suspend fun saveScreenTime(packageName: String, minutes: Double, day: String) =
        request("POST", "/api/screen-time?package_name=${enc(packageName)}&minutes=$minutes&day=${enc(day)}")
    suspend fun screenTime(day: String) = request("GET", "/api/screen-time?day=${enc(day)}")
    suspend fun briefing() = request("GET", "/api/briefing")
    suspend fun dashboard(name: String) = request("GET", "/api/dashboard/${enc(name)}")

    private fun enc(value: String) = URLEncoder.encode(value, "UTF-8")
}
