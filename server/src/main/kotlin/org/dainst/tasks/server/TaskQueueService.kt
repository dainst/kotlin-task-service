package org.dainst.tasks.server

import com.rabbitmq.client.AMQP
import com.rabbitmq.client.Channel
import org.dainst.tasks.common.JsonUtil
import org.dainst.tasks.common.Task
import org.dainst.tasks.common.createRabbitMqConnection
import org.springframework.stereotype.Service
import java.nio.charset.Charset


const val TASK_QUEUE_NAME = "tasks";
const val REPLY_QUEUE_NAME = "finished-tasks"

@Service
class TaskQueueService {

    private val connection = createRabbitMqConnection()
    val channel: Channel = connection.createChannel()

    init {

        channel.queueDeclare(TASK_QUEUE_NAME, false, false, false, null)
    }

    fun enqueue(task: Task) {

        val props = AMQP.BasicProperties.Builder()
                .replyTo(REPLY_QUEUE_NAME)
                .build()

        channel.basicPublish(
                "",
                TASK_QUEUE_NAME,
                props,
                JsonUtil().toJson(task).toByteArray(Charset.forName("UTF-8"))
        )
    }

    fun destroy() {
        channel.close()
        connection.close()
    }

}