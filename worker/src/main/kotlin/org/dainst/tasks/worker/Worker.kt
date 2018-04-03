package org.dainst.tasks.worker

import com.rabbitmq.client.*
import org.dainst.tasks.common.TaskService
import org.dainst.tasks.common.createRabbitMqConnection
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories


const val QUEUE_NAME = "tasks";

@SpringBootApplication(scanBasePackages = ["org.dainst.tasks.common", "org.dainst.tasks.worker"])
@EnableMongoRepositories("org.dainst.tasks.common")
class Worker {

    @Bean
    fun init(taskRunner: TaskRunner) = CommandLineRunner {

        val channel = establishRabbitMqChannel()
        channel.basicConsume(QUEUE_NAME, false, TaskConsumer(channel, taskRunner))

        println(" [*] Waiting for messages. To exit press CTRL+C")
    }

    private fun establishRabbitMqChannel(): Channel {

        var connection: Connection? = null
        while (connection == null) {
            try {
                connection = createRabbitMqConnection()
            } catch (e: Exception) {
                println(" [ ] Connection to broker refused, reason: ${e.toString()}. Will retry in 1s ...")
                Thread.sleep(1000)
            }
        }
        val channel = connection.createChannel()
        channel.queueDeclare(QUEUE_NAME, false, false, false, null)
        return channel
    }

}

fun main(args: Array<String>) {
    SpringApplication.run(Worker::class.java, *args)
}