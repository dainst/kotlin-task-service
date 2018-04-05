package org.dainst.tasks.worker

import java.util.*

class FakeTask: RunnableTask {

    override fun run() {

        val random = Random().nextInt(9000) + 1000
        println(" [x] Faking workload, waiting for ${random}ms ...")
        Thread.sleep(random.toLong())
    }
}