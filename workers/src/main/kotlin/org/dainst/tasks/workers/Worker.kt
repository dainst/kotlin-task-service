package org.dainst.tasks.workers

import com.rabbitmq.client.AMQP
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.DefaultConsumer
import com.rabbitmq.client.Envelope
import java.nio.charset.Charset


val QUEUE_NAME = "tasks";

fun main(args: Array<String>) {

    val factory = ConnectionFactory()
    factory.host = "localhost"
    factory.username = "user"
    factory.password = "password"
    factory.virtualHost = "task_queue"
    val connection = factory.newConnection()
    val channel = connection.createChannel()

    channel.queueDeclare(QUEUE_NAME, false, false, false, null)
    println(" [*] Waiting for messages. To exit press CTRL+C")

    val consumer = object : DefaultConsumer(channel) {
        override fun handleDelivery(consumerTag: String?, envelope: Envelope?, properties: AMQP.BasicProperties?, body: ByteArray?) {
            val message: String;
            if (body != null) {
                message = String(body, Charset.forName("UTF-8"))
            } else {
                message = "Empty body";
            }
            println(" [x] Received '$message'")
        }
    }
    channel.basicConsume(QUEUE_NAME, true, consumer)
}