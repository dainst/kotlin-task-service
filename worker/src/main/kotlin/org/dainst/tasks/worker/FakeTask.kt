package org.dainst.tasks.worker

class FakeTask: RunnableTask {

    override fun run() {
        Thread.sleep(5000L)
    }
}