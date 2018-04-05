package org.dainst.tasks.worker

import org.dainst.tasks.common.Task
import org.dainst.tasks.common.TaskService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class TaskRunner {

    fun runTask(task: Task): Task {

        return try {
            createRunnableTask(task).run()
            task.copy(status = "finished")
        } catch (e: Exception) {
            task.copy(status = "error")
        }
    }

    private fun createRunnableTask(task: Task): RunnableTask {

        return when (task.name) {
            "fake" -> FakeTask()
            else -> throw Exception("Unsupported task: '$task.name'")
        }
    }
}