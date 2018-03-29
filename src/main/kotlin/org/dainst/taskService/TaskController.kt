package org.dainst.taskService

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class TaskController {

    @GetMapping("/task/{id}")
    fun task(@PathVariable id: String,
             @RequestParam(value = "name", defaultValue = "DefaultTask") name: String
    ) = Task(id, name)


}