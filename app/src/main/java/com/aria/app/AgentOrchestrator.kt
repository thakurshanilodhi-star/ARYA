package com.aria.app
class AgentOrchestrator(private val api:ApiClient){
    fun route(text:String):String{
        val t=text.lowercase()
        return when{
            "alarm" in t||"remind" in t->"alarm"
            "calendar" in t||"meeting" in t->"calendar"
            "task" in t||"todo" in t->"task"
            "research" in t||"paper" in t->"research"
            "gym" in t||"workout" in t->"gym"
            "startup" in t->"startup"
            "career" in t->"career"
            "kaggle" in t||"competition" in t->"competition"
            else->"chat"
        }
    }
}
