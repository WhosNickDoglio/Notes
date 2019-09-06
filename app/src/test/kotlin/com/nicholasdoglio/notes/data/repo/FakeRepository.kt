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

package com.nicholasdoglio.notes.data.repo

import com.nicholasdoglio.notes.data.model.Note
import kotlinx.coroutines.flow.Flow

// TODO implement this
class FakeRepository : Repository<Note> {

    override val observeCountOfItems: Flow<Int>
        get() = TODO("not implemented") // To change initializer of created properties use File | Settings | File Templates.
    override val observeItems: Flow<List<Note>>
        get() = TODO("not implemented") // To change initializer of created properties use File | Settings | File Templates.

    override fun findItemById(id: Long): Flow<Note?> {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun upsert(item: Note) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun delete(item: Note) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }
}
