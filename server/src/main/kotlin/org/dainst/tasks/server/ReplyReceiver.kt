package org.dainst.tasks.server

import org.dainst.tasks.common.JsonUtil
import org.dainst.tasks.common.TaskService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.nio.charset.Charset

@Component
class ReplyReceiver {

    @Autowired
    lateinit var taskService: TaskService

    fun receiveMessage(body: ByteArray) {
        val task = JsonUtil().fromJson(String(body, Charset.forName("UTF-8")))
        taskService.save(task.copy(status = "finished"))
        println(" [x] Finished $task")
    }

}