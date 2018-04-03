package org.dainst.tasks.worker;

import com.google.gson.Gson
import com.rabbitmq.client.AMQP
import com.rabbitmq.client.Channel
import com.rabbitmq.client.DefaultConsumer
import com.rabbitmq.client.Envelope
import org.dainst.tasks.common.Task
import org.dainst.tasks.common.TaskService
import java.nio.charset.Charset

class TaskConsumer(channel: Channel, val taskService: TaskService) : DefaultConsumer(channel) {

    override fun handleDelivery(consumerTag: String?, envelope: Envelope?, properties: AMQP.BasicProperties?, body: ByteArray?) {

        var task = readTask(body)
        task = taskService.save(task.copy(status="running"))
        println(" [x] Received '${task}', running ...")
        createRunnableTask(task).run()
        task = taskService.save(task.copy(status = "finished"))
        println(" [x] Finished '${task}'.")
        if (envelope != null)
            channel.basicAck(envelope.deliveryTag, false)
    }

    private fun readTask(body: ByteArray?): Task {
        if (body == null) throw Exception("Empty body")
        return Gson().fromJson(String(body, Charset.forName("UTF-8")), Task::class.java)
    }

    private fun createRunnableTask(task: Task): RunnableTask {
        return when (task.name) {
            "fake" -> FakeTask()
            else -> throw Exception("Unsupported task: '$task.name'")
        }
    }
}