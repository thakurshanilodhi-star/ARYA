package com.aria.app

class ContentRepository(private val api: ApiClient) {
    suspend fun analyze(text: String, goals: List<String>) = api.analyzeContent(text, goals)
    suspend fun drafts() = api.drafts()
    suspend fun create(platform: String, title: String, body: String) = api.createDraft(platform, title, body)
    suspend fun update(id: Int, platform: String, title: String, body: String, status: String) =
        api.updateDraft(id, platform, title, body, status)
    suspend fun delete(id: Int) = api.deleteDraft(id)
}
