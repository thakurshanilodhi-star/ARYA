package com.aria.app

class TrackerRepository(private val api: ApiClient) {
    suspend fun list(category: String) = api.trackers(category)
    suspend fun create(category: String, title: String, value: Double, target: Double) =
        api.createTracker(category, title, value, target)
    suspend fun update(category: String, id: Int, title: String, value: Double, target: Double) =
        api.updateTracker(category, id, title, value, target)
    suspend fun delete(category: String, id: Int) = api.deleteTracker(category, id)
}
