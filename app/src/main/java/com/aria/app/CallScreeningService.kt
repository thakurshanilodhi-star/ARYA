package com.aria.app

import android.content.Intent
import android.os.Build
import android.telecom.Call

class CallScreeningService : android.telecom.CallScreeningService() {
    override fun onScreenCall(details: Call.Details) {
        try {
            val incoming = Build.VERSION.SDK_INT < 29 ||
                details.callDirection == Call.Details.DIRECTION_INCOMING
            if (incoming) {
                sendBroadcast(Intent(this, CallOverlayReceiver::class.java))
            }
        } catch (_: Exception) {
        }
        respondToCall(
            details,
            android.telecom.CallScreeningService.CallResponse.Builder()
                .setDisallowCall(false)
                .setRejectCall(false)
                .setSilenceCall(false)
                .build()
        )
    }
}
