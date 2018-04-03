package org.dainst.tasks.worker;

import com.google.gson.Gson
import com.rabbitmq.client.AMQP
import com.rabbitmq.client.Channel
import com.rabbitmq.client.DefaultConsumer
import com.rabbitmq.client.Envelope
import org.dainst.tasks.common.Task
import org.dainst.tasks.common.TaskService
import org.springframework.beans.factory.annotation.Autowired
import java.nio.charset.Charset

class TaskConsumer(channel: Channel, private val taskRunner: TaskRunner) : DefaultConsumer(channel) {

    override fun handleDelivery(consumerTag: String?, envelope: Envelope?, properties: AMQP.BasicProperties?, body: ByteArray?) {

        taskRunner.runTask(readTask(body))
        if (envelope != null)
            channel.basicAck(envelope.deliveryTag, false)
    }

    private fun readTask(body: ByteArray?): Task {
        if (body == null) throw Exception("Empty body")
        return Gson().fromJson(String(body, Charset.forName("UTF-8")), Task::class.java)
    }
}