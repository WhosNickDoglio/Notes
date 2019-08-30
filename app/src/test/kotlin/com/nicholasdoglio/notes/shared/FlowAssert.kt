/*
 * MIT License
 *
 * Copyright (c) 2019 Nicholas Doglio
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.nicholasdoglio.notes.shared

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Pulled from
 * https://github.com/square/sqldelight/blob/master/extensions/coroutines-extensions/src/commonTest/kotlin/com/squareup/sqldelight/runtime/coroutines/FlowAssert.kt
 *
 * https://github.com/square/sqldelight/issues/1416
 */

suspend fun <T> Flow<T>.test(timeoutMs: Long = 1000L, validate: suspend FlowAssert<T>.() -> Unit) {
    coroutineScope {
        val events = Channel<Event<T>>(Channel.UNLIMITED)
        val collectJob = launch {
            val terminalEvent = try {
                collect { item ->
                    events.send(Event.Item(item))
                }
                Event.Complete
            } catch (_: CancellationException) {
                null
            } catch (t: Throwable) {
                Event.Error(t)
            }
            if (terminalEvent != null) {
                events.send(terminalEvent)
            }
            events.close()
        }
        val flowAssert = FlowAssert(events, collectJob, timeoutMs)
        val ensureConsumed = try {
            flowAssert.validate()
            true
        } catch (e: CancellationException) {
            if (e !== ignoreRemainingEventsException) {
                throw e
            }
            false
        }
        if (ensureConsumed) {
            flowAssert.expectNoMoreEvents()
        }
    }
}

private val ignoreRemainingEventsException = CancellationException("Ignore remaining events")

internal sealed class Event<out T> {
    object Complete : Event<Nothing>()
    data class Error(val throwable: Throwable) : Event<Nothing>()
    data class Item<T>(val item: T) : Event<T>()
}

class FlowAssert<T> internal constructor(
    private val events: Channel<Event<T>>,
    private val collectJob: Job,
    private val timeoutMs: Long
) {
    private suspend fun <T> withTimeout(body: suspend () -> T): T {
        return if (timeoutMs == 0L) {
            body()
        } else {
            kotlinx.coroutines.withTimeout(timeoutMs) {
                body()
            }
        }
    }

    fun cancel() {
        collectJob.cancel()
    }

    fun cancelAndIgnoreRemainingEvents(): Nothing {
        cancel()
        throw ignoreRemainingEventsException
    }

    fun expectNoEvents() {
        val event = events.poll()
        if (event != null) {
            throw AssertionError("Expected no events but found $event")
        }
    }

    suspend fun expectNoMoreEvents() {
        val event = withTimeout {
            events.receiveOrNull()
        }
        if (event != null) {
            throw AssertionError("Expected no more events but found $event")
        }
    }

    suspend fun expectItem(): T {
        val event = withTimeout {
            events.receive()
        }
        if (event !is Event.Item<T>) {
            throw AssertionError("Expected item but was $event")
        }
        return event.item
    }

    suspend fun expectComplete() {
        val event = withTimeout {
            events.receive()
        }
        if (event != Event.Complete) {
            throw AssertionError("Expected complete but was $event")
        }
    }

    suspend fun expectError(): Throwable {
        val event = withTimeout {
            events.receive()
        }
        if (event !is Event.Error) {
            throw AssertionError("Expected error but was $event")
        }
        return event.throwable
    }
}
