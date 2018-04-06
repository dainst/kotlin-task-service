package org.dainst.tasks.worker

import org.dainst.tasks.common.createRabbitMqConnection
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories


const val QUEUE_NAME = "tasks";

class Application {

    fun run() {

        val connection = createRabbitMqConnection()
        val channel = connection.createChannel()
        channel.queueDeclare(QUEUE_NAME, false, false, false, null)
        channel.basicConsume(QUEUE_NAME, false, TaskConsumer(channel, TaskRunner()))
        channel.basicQos(1)

        println(" [*] Waiting for tasks. To exit press CTRL+C")
    }

}

fun main(args: Array<String>) {

    Application().run()
}