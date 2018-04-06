package org.dainst.tasks.worker

import org.dainst.tasks.common.createRabbitMqConnection


const val TOPIC_EXCHANGE_NAME = "tasks"
const val WORKER_QUEUE_NAME = "generic-tasks"

class Application {

    fun run() {

        val connection = createRabbitMqConnection()
        val channel = connection.createChannel()
        channel.exchangeDeclare(TOPIC_EXCHANGE_NAME, "topic", true)
        channel.queueDeclare(WORKER_QUEUE_NAME, true, false, false, null).queue
        channel.queueBind(WORKER_QUEUE_NAME, TOPIC_EXCHANGE_NAME, "task.*.queued")
        channel.basicConsume(WORKER_QUEUE_NAME, false, TaskConsumer(channel, TaskRunner()))
        channel.basicQos(1)

        println(" [*] Waiting for tasks. To exit press CTRL+C")
    }

}

fun main(args: Array<String>) {

    Application().run()
}