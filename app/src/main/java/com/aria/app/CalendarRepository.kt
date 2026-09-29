package com.aria.app

class CalendarRepository(private val api: ApiClient) {
    suspend fun list() = api.calendar()
    suspend fun create(title: String, startAt: Long, endAt: Long, notes: String = "") =
        api.createCalendar(title, startAt, endAt, notes)
    suspend fun update(id: Int, title: String, startAt: Long, endAt: Long, notes: String = "") =
        api.updateCalendar(id, title, startAt, endAt, notes)
    suspend fun delete(id: Int) = api.deleteCalendar(id)
}
