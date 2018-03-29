package org.dainst.tasks.server

import org.springframework.web.bind.annotation.*
import com.rabbitmq.client.ConnectionFactory
import java.nio.charset.Charset


@RestController
@RequestMapping("/task")
class TaskController {

    val QUEUE_NAME = "tasks";

    @PostMapping("/hello")
    fun hello(): String {
        val factory = ConnectionFactory()
        factory.host = System.getenv("BROKER_HOST") ?: "localhost"
        factory.username = System.getenv("BROKER_USER") ?: "guest"
        factory.password = System.getenv("BROKER_PASSWORD") ?: "guest"
        factory.virtualHost = System.getenv("BROKER_VHOST") ?: "/"
        val connection = factory.newConnection()
        val channel = connection.createChannel()

        channel.queueDeclare(QUEUE_NAME, false, false, false, null)
        val message = "Hello World"
        channel.basicPublish(
                "",
                QUEUE_NAME,
                null,
                message.toByteArray(Charset.forName("UTF-8"))
        )
        println(" [x] Sent '$message'")

        channel.close()
        connection.close()

        return message
    }

    @GetMapping("/status/{id}")
    fun status(@PathVariable id: String,
             @RequestParam(value = "name", defaultValue = "DefaultTask") name: String
    ) = Task(id, name)


}