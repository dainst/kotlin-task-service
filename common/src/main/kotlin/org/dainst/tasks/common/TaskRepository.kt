package org.dainst.tasks.common

import org.springframework.data.repository.CrudRepository

interface TaskRepository : CrudRepository<Task, String>