package org.dainst.tasks.server

import org.dainst.tasks.common.*
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.web.bind.annotation.*
import org.springframework.beans.factory.annotation.Autowired
import java.util.*


@RestController
@RequestMapping("/task")
class TaskController {

    @Autowired
    lateinit var taskService: TaskService

    @Autowired
    lateinit var rabbitTemplate: RabbitTemplate

    @PostMapping("/create/{name}")
    fun create(@PathVariable name: String): String {

        val id = UUID.randomUUID()
        val task = Task(id.toString(), name, "queued")
        val json = JsonUtil().toJson(task)
        rabbitTemplate.convertAndSend(TOPIC_EXCHANGE_NAME, "task.$name.queued", json)
        taskService.save(task)
        println(" [x] Enqueued '$task'")

        return json
    }

    @GetMapping("/status/{id}")
    fun status(@PathVariable id: String): String {
        val task = taskService.get(id)
        return JsonUtil().toJson(task)
    }


}