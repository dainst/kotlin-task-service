package org.dainst.tasks.common

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime

data class Task(
        @Id val id: String,
        val name: String,
        val status: String = "new",
        @CreatedDate val created: LocalDateTime = LocalDateTime.now(),
        @LastModifiedDate val modified: LocalDateTime = LocalDateTime.now()
)