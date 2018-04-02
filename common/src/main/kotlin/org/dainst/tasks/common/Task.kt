package org.dainst.tasks.common

import org.springframework.data.annotation.Id

data class Task(@Id val id: String, val name: String, val status: String = "new")