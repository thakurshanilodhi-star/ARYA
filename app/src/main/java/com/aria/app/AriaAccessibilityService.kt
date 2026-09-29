package com.aria.app

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

class AriaAccessibilityService : AccessibilityService() {
    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        Log.d("ARIA_ACCESS", "package=${event.packageName} type=${event.eventType}")
    }

    override fun onInterrupt() {
        Log.d("ARIA_ACCESS", "service interrupted")
    }

    fun clickText(text: String): Boolean =
        findNode(text)?.performAction(AccessibilityNodeInfo.ACTION_CLICK) == true

    fun scrollForward(): Boolean =
        rootInActiveWindow?.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD) == true

    private fun findNode(text: String): AccessibilityNodeInfo? =
        rootInActiveWindow?.findAccessibilityNodeInfosByText(text)?.firstOrNull()

    fun youtubePlay() = clickText("Play")
    fun youtubePause() = clickText("Pause")
    fun youtubeScroll() = scrollForward()
    fun whatsappSend() = clickText("Send")
}
