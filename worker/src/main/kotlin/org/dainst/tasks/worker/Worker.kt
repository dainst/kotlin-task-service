package org.dainst.tasks.worker

import com.google.gson.Gson
import com.rabbitmq.client.*
import java.nio.charset.Charset
import org.dainst.tasks.common.Task
import org.dainst.tasks.common.createRabbitMqConnection


val QUEUE_NAME = "tasks";

fun main(args: Array<String>) {

    var connection: Connection? = null;
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
    println(" [*] Waiting for messages. To exit press CTRL+C")

    val consumer = object : DefaultConsumer(channel) {
        override fun handleDelivery(consumerTag: String?, envelope: Envelope?, properties: AMQP.BasicProperties?, body: ByteArray?) {
            val task: Task;
            if (body != null) {
                val message = String(body, Charset.forName("UTF-8"))
                task = Gson().fromJson(message, Task::class.java)
            } else {
                task = Task("","")
            }
            println(" [x] Received '${task.name}', faking workload ...")
            Thread.sleep(1000)
            println(" [x] Finished '${task.name}'.")
            if (envelope != null)
                channel.basicAck(envelope.deliveryTag, false)
        }
    }
    channel.basicConsume(QUEUE_NAME, false, consumer)
}