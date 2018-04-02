package org.dainst.tasks.common

import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory

fun createRabbitMqConnection(): Connection {
    val factory = ConnectionFactory()
    factory.host = System.getenv("BROKER_HOST") ?: "localhost"
    factory.username = System.getenv("BROKER_USER") ?: "guest"
    factory.password = System.getenv("BROKER_PASSWORD") ?: "guest"
    factory.virtualHost = System.getenv("BROKER_VHOST") ?: "/"
    return factory.newConnection()
}