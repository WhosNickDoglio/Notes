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

package com.nicholasdoglio.notes.data.note

import com.google.common.truth.Truth.assertThat
import com.nicholasdoglio.notes.data.TimestampColumnAdapter
import org.junit.Test
import org.threeten.bp.LocalDateTime

class TimestampColumnAdapterTest {

    private val adapter = TimestampColumnAdapter()

    private val date = LocalDateTime.of(2020, 1, 1, 12, 0)

    private val dateAsString = "2020-01-01T12:00:00"

    @Test
    fun `given date as LocalDateTime when encoded then return date as string`() {
        assertThat(adapter.encode(date)).isEqualTo(dateAsString)
    }

    @Test
    fun `given date as String when decoded then return data as LocalDateTime`() {
        assertThat(adapter.decode(dateAsString)).isEqualTo(date)
    }
}
