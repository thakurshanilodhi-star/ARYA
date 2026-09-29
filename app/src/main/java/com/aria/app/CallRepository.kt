package com.aria.app

import android.content.Context
import android.content.Intent
import android.net.Uri

class CallRepository(private val context: Context) {
    fun dial(number: String) {
        context.startActivity(Intent(Intent.ACTION_CALL, Uri.parse("tel:${Uri.encode(number)}")).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }
}
