package com.aria.app

import java.text.SimpleDateFormat
import java.util.Locale

data class ParsedCalendarEvent(val title: String, val startAt: Long, val endAt: Long, val notes: String)

object CalendarVoiceParser {
    private val clock = Regex("""\b(\d{1,2})(?::(\d{2}))?\s*(am|pm)?\b""", RegexOption.IGNORE_CASE)

    fun parse(text: String, now: Long = System.currentTimeMillis()): ParsedCalendarEvent? {
        val match = clock.find(text) ?: return null
        val hourRaw = match.groupValues[1].toIntOrNull() ?: return null
        val minute = match.groupValues[2].ifBlank { "0" }.toIntOrNull() ?: 0
        val ampm = match.groupValues[3].lowercase(Locale.US)
        val hour = when {
            ampm == "pm" && hourRaw < 12 -> hourRaw + 12
            ampm == "am" && hourRaw == 12 -> 0
            else -> hourRaw
        }
        if (hour !in 0..23 || minute !in 0..59) return null
        val day = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(java.util.Date(now))
        val date = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US).parse("$day %02d:%02d".format(hour, minute)) ?: return null
        val title = text.replace(Regex("""(?i)\b(add|create|schedule|calendar|meeting|event)\b"""), " ").trim().replace(Regex("""\s+"""), " ")
        return ParsedCalendarEvent(if (title.isBlank()) "ARIA event" else title, date.time, date.time + 3600000L, text)
    }
}
