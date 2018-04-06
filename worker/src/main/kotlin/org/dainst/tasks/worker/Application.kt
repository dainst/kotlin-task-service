package org.dainst.tasks.worker

import org.dainst.tasks.common.createRabbitMqConnection


const val TOPIC_EXCHANGE_NAME = "tasks"

class Application {

    fun run() {

        val connection = createRabbitMqConnection()
        val channel = connection.createChannel()
        channel.exchangeDeclare(TOPIC_EXCHANGE_NAME, "topic", true)
        val queueName = channel.queueDeclare().queue
        channel.queueBind(queueName, TOPIC_EXCHANGE_NAME, "task.*.queued")
        channel.basicConsume(queueName, false, TaskConsumer(channel, TaskRunner()))
        channel.basicQos(1)

        println(" [*] Waiting for tasks. To exit press CTRL+C")
    }

}

fun main(args: Array<String>) {

    Application().run()
}