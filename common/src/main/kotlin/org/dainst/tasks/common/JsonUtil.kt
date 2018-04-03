package org.dainst.tasks.common

import com.google.gson.Gson
import com.google.gson.GsonBuilder

class JsonUtil {

    private val gson: Gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssX").create()

    fun toJson(task: Task): String = gson.toJson(task)

    fun fromJson(value: String): Task = gson.fromJson(value, Task::class.java)

}