package org.dainst.tasks.workers

import com.rabbitmq.client.*
import java.nio.charset.Charset


val QUEUE_NAME = "tasks";

fun main(args: Array<String>) {

    val factory = ConnectionFactory()
    factory.host = System.getenv("BROKER_HOST") ?: "localhost"
    factory.username = System.getenv("BROKER_USER") ?: "guest"
    factory.password = System.getenv("BROKER_PASSWORD") ?: "guest"
    factory.virtualHost = System.getenv("BROKER_VHOST") ?: "/"
    var connection: Connection? = null;
    while (connection == null) {
        try {
            connection = factory.newConnection()
        } catch (e: Exception) {
            println(" [ ] Connection to broker '${factory.host}' refused, reason: ${e.toString()}. Will retry in 1s ...")
            Thread.sleep(1000)
        }
    }
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
            println(" [x] Received '$message', faking workload ...")
            Thread.sleep(1000)
            println(" [x] Finished '$message'.")
            if (envelope != null)
                channel.basicAck(envelope.deliveryTag, false)
        }
    }
    channel.basicConsume(QUEUE_NAME, false, consumer)
}