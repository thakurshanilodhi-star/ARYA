package com.aria.app
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
class BrainRouter(private val api:ApiClient) {
    suspend fun respond(text:String):String=withContext(Dispatchers.IO){
        try{org.json.JSONObject(api.chat(text)).optString("answer")}
        catch(_:Exception){OfflineLlmClient.respond(text)}
    }
}
object OfflineLlmClient{
    private val client=okhttp3.OkHttpClient()
    suspend fun respond(text:String):String=withContext(Dispatchers.IO){
        val body=org.json.JSONObject().put("model","local").put("messages",org.json.JSONArray().put(org.json.JSONObject().put("role","user").put("content",text))).toString().toRequestBody("application/json".toMediaType())
        val r=client.newCall(okhttp3.Request.Builder().url("http://127.0.0.1:8080/v1/chat/completions").post(body).build()).execute()
        r.use{if(!it.isSuccessful)error("offline ${it.code}");org.json.JSONObject(it.body?.string()?:"{}").getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content")}
    }
}
