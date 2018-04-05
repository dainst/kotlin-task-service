package org.dainst.tasks.common

import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory

fun createRabbitMqConnection(): Connection {
    var connection: Connection? = null
    while (connection == null) {
        try {
            val factory = ConnectionFactory()
            factory.host = System.getenv("BROKER_HOST") ?: "localhost"
            factory.username = System.getenv("BROKER_USER") ?: "guest"
            factory.password = System.getenv("BROKER_PASSWORD") ?: "guest"
            factory.virtualHost = System.getenv("BROKER_VHOST") ?: "/"
            connection = factory.newConnection()
        } catch (e: Exception) {
            println(" [ ] Connection to broker refused, reason: $e. Will retry in 1s ...")
            Thread.sleep(1000)
        }
    }
    return connection
}