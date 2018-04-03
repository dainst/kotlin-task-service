package org.dainst.tasks.worker

import com.google.gson.Gson
import com.rabbitmq.client.*
import java.nio.charset.Charset
import org.dainst.tasks.common.Task
import org.dainst.tasks.common.TaskRepository
import org.dainst.tasks.common.TaskService
import org.dainst.tasks.common.createRabbitMqConnection
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories


val QUEUE_NAME = "tasks";

@SpringBootApplication(scanBasePackages = ["org.dainst.tasks.common"])
@EnableMongoRepositories("org.dainst.tasks.common")
class Worker {

    @Bean
    fun init(taskService: TaskService) = CommandLineRunner {

        val channel = establishRabbitMqChannel()
        channel.basicConsume(QUEUE_NAME, false, TaskConsumer(channel, taskService))

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

    private class TaskConsumer(channel: Channel, val taskService: TaskService) : DefaultConsumer(channel) {

        override fun handleDelivery(consumerTag: String?, envelope: Envelope?, properties: AMQP.BasicProperties?, body: ByteArray?) {

            var task: Task;
            task = if (body != null) {
                Gson().fromJson(String(body, Charset.forName("UTF-8")), Task::class.java)
            } else {
                Task("","")
            }
            task = taskService.save(task.copy(status = "running"))
            println(" [x] Received '${task}', faking workload ...")
            Thread.sleep(10000L)
            task = taskService.save(task.copy(status = "finished"))
            println(" [x] Finished '${task}'.")
            if (envelope != null)
                channel.basicAck(envelope.deliveryTag, false)
        }
    }

}

fun main(args: Array<String>) {
    SpringApplication.run(Worker::class.java, *args)
}