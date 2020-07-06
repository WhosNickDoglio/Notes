package com.doglio.shared.data

import com.google.common.truth.Truth
import org.junit.Test
import java.time.LocalDateTime

class TimestampColumnAdapterTest {

    private val adapter = TimestampColumnAdapter()

    private val date = LocalDateTime.of(2020, 1, 1, 12, 0)

    private val dateAsString = "2020-01-01T12:00:00"

    @Test
    fun `given date as LocalDateTime when encoded then return date as string`() {
        Truth.assertThat(adapter.encode(date)).isEqualTo(dateAsString)
    }

    @Test
    fun `given date as String when decoded then return data as LocalDateTime`() {
        Truth.assertThat(adapter.decode(dateAsString)).isEqualTo(date)
    }
}
