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

import com.google.common.truth.Truth.assertThat
import com.nicholasdoglio.notes.TestData
import com.nicholasdoglio.notes.data.model.Note
import com.nicholasdoglio.notes.test
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

class NoteRepositoryTest {

    private val repository = NoteRepository(FakeDao())

    // TODO better naming

    @Test
    fun `observe number of notes - success`() = runBlockingTest {
        repository.countOfNotes.test {
            assertThat(expectItem()).isEqualTo(0)
            cancel()
        }

        repository.upsert(TestData.firstNote)

        repository.countOfNotes.test {
            assertThat(expectItem()).isEqualTo(1)
            cancel()
        }
    }

    @Test
    fun `observe notes -  success`() = runBlockingTest {
        repository.observeNotes.test {
            assertThat(expectItem()).isEqualTo(emptyList<Note>())
            cancel()
        }

        repository.upsert(TestData.firstNote)

        repository.observeNotes.test {
            assertThat(expectItem()).isEqualTo(listOf(TestData.firstNote))
            cancel()
        }
    }

    @Test
    fun `find note - success`() = runBlockingTest {
        TestData.someNotes.forEach {
            repository.upsert(it)
        }

        repository.findNoteById(TestData.firstNote.id).test {
            assertThat(expectItem()).isEqualTo(TestData.firstNote)
            expectComplete()
            cancel()
        }

        repository.findNoteById(TestData.secondNote.id).test {
            assertThat(expectItem()).isEqualTo(TestData.secondNote)
            expectComplete()
            cancel()
        }

        repository.findNoteById(TestData.thirdNote.id).test {
            assertThat(expectItem()).isEqualTo(TestData.thirdNote)
            expectComplete()
            cancel()
        }
    }

    @Test
    fun `find note -- failure`() = runBlockingTest {
        repository.findNoteById(TestData.thirdNote.id).test {
            assertThat(expectItem()).isEqualTo(null)
            expectComplete()
            cancel()
        }
    }

    @Test // TODO think about failure case?
    fun `upsert note - insert - success`() = runBlockingTest {
        val note = Note(5, "Hello World", "Testing upsert success")
        repository.upsert(note)

        repository.observeNotes.test {
            assertThat(expectItem()).isEqualTo(listOf(note))
            cancel()
        }

        repository.countOfNotes.test {
            assertThat(expectItem()).isEqualTo(1)
            cancel()
        }

        repository.findNoteById(note.id).test {
            assertThat(expectItem()).isEqualTo(note)
            expectComplete()
            cancel()
        }
    }

    @Test // TODO think about failure case?
    fun `upsert note - update - success`() = runBlockingTest { }

    @Test // TODO think about failure case?
    fun `delete note -  success`() = runBlockingTest {
        repository.upsert(TestData.firstNote)

        repository.countOfNotes.test {
            assertThat(expectItem()).isEqualTo(1)
            cancel()
        }

        repository.delete(TestData.firstNote)

        repository.countOfNotes.test {
            assertThat(expectItem()).isEqualTo(0)
            cancel()
        }
    }
}
