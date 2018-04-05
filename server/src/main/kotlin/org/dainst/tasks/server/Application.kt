package org.dainst.tasks.server

import com.rabbitmq.client.AMQP
import com.rabbitmq.client.DefaultConsumer
import com.rabbitmq.client.Envelope
import org.dainst.tasks.common.JsonUtil
import org.dainst.tasks.common.TaskService
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import java.nio.charset.Charset

@SpringBootApplication(scanBasePackages = ["org.dainst.tasks.common", "org.dainst.tasks.server"])
@EnableMongoRepositories("org.dainst.tasks.common")
class Application {

    @Bean
    fun init(
            taskService: TaskService,
            taskQueueService: TaskQueueService
    ) = CommandLineRunner {

        taskQueueService.channel.queueDeclare(REPLY_QUEUE_NAME, false, false, false, null)

        taskQueueService.channel.basicConsume(REPLY_QUEUE_NAME, false, object : DefaultConsumer(taskQueueService.channel) {
            override fun handleDelivery(consumerTag: String?, envelope: Envelope?, properties: AMQP.BasicProperties?, body: ByteArray?) {
                if (body == null) throw Exception("Empty body")
                val task = JsonUtil().fromJson(String(body, Charset.forName("UTF-8")))
                taskService.save(task.copy(status = "finished"))
                println(" [x] Finished $task")
            }
        })

        println(" [*] Waiting for finished tasks. To exit press CTRL+C")
    }

}

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}