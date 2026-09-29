package com.aria.app

import android.app.Activity
import android.os.Bundle
import android.widget.TextView

class IncomingCallActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = TextView(this)
        view.text = "ARIA: Incoming call detected"
        view.textSize = 24f
        view.setPadding(32, 32, 32, 32)
        setContentView(view)
    }
}
