package com.aria.app

class DashboardDataRepository(private val api: ApiClient) {
    suspend fun load(name: String) = api.dashboard(name)
}
