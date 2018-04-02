package org.dainst.tasks.common

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class TaskService {

    @Autowired
    lateinit var taskRepository: TaskRepository

    fun save(task: Task) = taskRepository.save(task)

    fun get(id: String) = taskRepository.findById(id).get()

}