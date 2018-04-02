package org.dainst.tasks.server

import com.google.gson.Gson
import org.springframework.web.bind.annotation.*
import org.dainst.tasks.common.Task
import org.dainst.tasks.common.TaskRepository
import org.dainst.tasks.common.TaskService
import org.dainst.tasks.common.createRabbitMqConnection
import org.springframework.beans.factory.annotation.Autowired
import java.nio.charset.Charset
import java.util.*


@RestController
@RequestMapping("/task")
class TaskController {

    val QUEUE_NAME = "tasks";

    @Autowired
    lateinit var taskService: TaskService

    @PostMapping("/create/{name}")
    fun create(@PathVariable name: String): String {

        val connection = createRabbitMqConnection()
        val channel = connection.createChannel()

        val id = UUID.randomUUID()
        val task = Task(id.toString(), name)
        taskService.save(task)

        channel.queueDeclare(QUEUE_NAME, false, false, false, null)
        channel.basicPublish(
                "",
                QUEUE_NAME,
                null,
                Gson().toJson(task).toByteArray(Charset.forName("UTF-8"))
        )
        println(" [x] Sent '$name'")

        channel.close()
        connection.close()

        return Gson().toJson(task)
    }

    @GetMapping("/status/{id}")
    fun status(@PathVariable id: String): String {
        val task = taskService.get(id)
        return Gson().toJson(task)
    }


}