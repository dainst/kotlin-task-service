package org.dainst.tasks.server

import org.dainst.tasks.common.TaskService
import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.TopicExchange
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

const val TOPIC_EXCHANGE_NAME = "tasks"
const val TASK_QUEUE_NAME = "queued-tasks"
const val REPLY_QUEUE_NAME = "finished-tasks"

@SpringBootApplication(scanBasePackages = ["org.dainst.tasks.common", "org.dainst.tasks.server"])
@EnableMongoRepositories("org.dainst.tasks.common")
class Application {

    @Bean
    fun replyQueue(): Queue = Queue(REPLY_QUEUE_NAME, false)

    @Bean
    fun exchange(): TopicExchange = TopicExchange(TOPIC_EXCHANGE_NAME)

    @Bean
    fun binding(queue: Queue, exchange: TopicExchange): Binding
            = BindingBuilder.bind(queue).to(exchange).with("task.*.finished")

    @Bean
    fun listenerAdapter(receiver: ReplyReceiver): MessageListenerAdapter
            = MessageListenerAdapter(receiver, "receiveMessage")

    @Bean
    fun container(connectionFactory: ConnectionFactory, listenerAdapter: MessageListenerAdapter): SimpleMessageListenerContainer {
        val container = SimpleMessageListenerContainer()
        container.connectionFactory = connectionFactory
        container.setQueueNames(REPLY_QUEUE_NAME)
        container.messageListener = listenerAdapter
        return container
    }

    @Bean
    fun init() = CommandLineRunner {

        println(" [*] Waiting for finished tasks. To exit press CTRL+C")
    }

}

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}