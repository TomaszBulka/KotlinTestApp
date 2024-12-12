package com.example.utils

class BatchSaver<T>(
    private val saveFunction: (List<T>) -> Unit
) {
    private val batch = mutableListOf<T>()
    private val lock = java.util.concurrent.locks.ReentrantLock()
    private var running = false

    fun addToBatch(item: T) {
        lock.lock()
        try {
            batch.add(item)
        } finally {
            lock.unlock()
        }
    }

    fun start() {
        running = true
        Thread {
            while (running) {
                Thread.sleep(60_000)
                saveBatch()
            }
        }.start()
    }

    private fun saveBatch() {
        lock.lock()
        try {
            if (batch.isNotEmpty()) {
                saveFunction(batch.toList())
                batch.clear()
            }
        } finally {
            lock.unlock()
        }
    }

    fun stop() {
        running = false
    }
}
