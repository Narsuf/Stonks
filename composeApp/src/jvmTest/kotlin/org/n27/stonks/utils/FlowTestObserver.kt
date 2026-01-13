package org.n27.stonks.utils

import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.io.Closeable

fun <T> Flow<T>.test(scope: CoroutineScope): FlowTestObserver<T> {
    return FlowTestObserver(scope, this)
}

class FlowTestObserver<T>(scope: CoroutineScope, flow: Flow<T>) : Closeable {

    private val values = mutableListOf<T>()

    private val job: Job = scope.launch {
        flow.collect { values.add(it) }
    }

    fun assertValues(vararg values: T): FlowTestObserver<T> {
        assertEquals(values.toList(), this.values)
        return this
    }

    fun reset() { values.clear() }

    override fun close() = job.cancel()
}

