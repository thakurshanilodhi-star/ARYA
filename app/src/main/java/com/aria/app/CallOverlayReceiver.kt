package com.aria.app

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class CallOverlayReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        context.startActivity(Intent(context, IncomingCallActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }
}
