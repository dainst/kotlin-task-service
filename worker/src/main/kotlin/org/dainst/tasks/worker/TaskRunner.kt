package org.dainst.tasks.worker

import org.dainst.tasks.common.Task
import org.dainst.tasks.common.TaskService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class TaskRunner {

    @Autowired
    private lateinit var taskService: TaskService

    fun runTask(task: Task) {

        prepare(task)
        try {
            createRunnableTask(task).run()
            handleSuccess(task)
        } catch (e: Exception) {
            handleError(task)
        }
    }

    private fun prepare(task: Task) {

        taskService.save(task.copy(status="running"))
        println(" [x] Received '${task}', running ...")
    }

    private fun handleSuccess(task: Task) {

        taskService.save(task.copy(status = "finished"))
        println(" [x] Finished '${task}'.")
    }

    private fun handleError(task: Task) {

        taskService.save(task.copy(status = "errored"))
        println(" [x] Errored '${task}'.")
    }

    private fun createRunnableTask(task: Task): RunnableTask {
        return when (task.name) {
            "fake" -> FakeTask()
            else -> throw Exception("Unsupported task: '$task.name'")
        }
    }
}