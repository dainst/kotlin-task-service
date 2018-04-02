package org.dainst.tasks.server

import org.springframework.web.bind.annotation.*
import com.rabbitmq.client.ConnectionFactory
import org.dainst.tasks.common.Task
import java.nio.charset.Charset
import java.util.*


@RestController
@RequestMapping("/task")
class TaskController {

    val QUEUE_NAME = "tasks";

    @PostMapping("/create/{name}")
    fun create(@PathVariable name: String): String {
        val factory = ConnectionFactory()
        factory.host = System.getenv("BROKER_HOST") ?: "localhost"
        factory.username = System.getenv("BROKER_USER") ?: "guest"
        factory.password = System.getenv("BROKER_PASSWORD") ?: "guest"
        factory.virtualHost = System.getenv("BROKER_VHOST") ?: "/"
        val connection = factory.newConnection()
        val channel = connection.createChannel()

        val id = UUID.randomUUID()
        val task = Task(id.toString(), name)

        channel.queueDeclare(QUEUE_NAME, false, false, false, null)
        channel.basicPublish(
                "",
                QUEUE_NAME,
                null,
                task.toString().toByteArray(Charset.forName("UTF-8"))
        )
        println(" [x] Sent '$name'")

        channel.close()
        connection.close()

        return task.toString()
    }

    @GetMapping("/status/{id}")
    fun status(@PathVariable id: String,
             @RequestParam(value = "name", defaultValue = "DefaultTask") name: String
    ) = Task(id, name)


}