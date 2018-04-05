package org.dainst.tasks.server

import org.dainst.tasks.common.*
import org.springframework.web.bind.annotation.*
import org.springframework.beans.factory.annotation.Autowired
import java.util.*


@RestController
@RequestMapping("/task")
class TaskController {

    @Autowired
    lateinit var taskService: TaskService

    @Autowired
    lateinit var taskQueueService: TaskQueueService

    @PostMapping("/create/{name}")
    fun create(@PathVariable name: String): String {

        val id = UUID.randomUUID()
        val task = Task(id.toString(), name, "queued")
        taskQueueService.enqueue(task)
        taskService.save(task)
        println(" [x] Enqueued '$task'")

        return JsonUtil().toJson(task)
    }

    @GetMapping("/status/{id}")
    fun status(@PathVariable id: String): String {
        val task = taskService.get(id)
        return JsonUtil().toJson(task)
    }


}