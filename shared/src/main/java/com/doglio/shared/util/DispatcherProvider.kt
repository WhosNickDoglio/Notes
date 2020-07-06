/*
 * MIT License
 *
 * Copyright (c) 2020 Nicholas Doglio
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

package com.doglio.shared.util

import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * An interface that provides a [CoroutineContext] for different use cases.
 */
interface DispatcherProvider {

    /**
     *  The main (UI) context that should be used for anything UI related.
     */
    val main: CoroutineContext

    /**
     *  The background context for running any computation or background work.
     */
    val background: CoroutineContext

    /**
     * The database context used only for running read/writes from the database.
     */
    val database: CoroutineContext
}

/**
 * The default implementation of [DispatcherProvider].
 */
class DefaultDispatchers @Inject constructor() : DispatcherProvider {
    override val main: CoroutineContext = Dispatchers.Main
    override val background: CoroutineContext = Dispatchers.Default
    override val database: CoroutineContext = Dispatchers.IO
}
