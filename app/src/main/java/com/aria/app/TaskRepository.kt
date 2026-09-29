package com.aria.app

class TaskRepository(private val api: ApiClient) {
    suspend fun list() = api.tasks()
    suspend fun create(title: String, status: String = "OPEN", dueAt: Long? = null) =
        api.createTask(title, status, dueAt)
    suspend fun update(id: Int, title: String, status: String, dueAt: Long? = null) =
        api.updateTask(id, title, status, dueAt)
    suspend fun delete(id: Int) = api.deleteTask(id)
}
