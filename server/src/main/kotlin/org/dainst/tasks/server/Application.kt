package org.dainst.tasks.server

import org.dainst.tasks.common.TaskRepository
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@SpringBootApplication(scanBasePackages = ["org.dainst.tasks.common", "org.dainst.tasks.server"])
@EnableMongoRepositories("org.dainst.tasks.common")
class Application

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}