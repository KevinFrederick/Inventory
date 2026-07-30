package com.kevinfreyap.product.domain.util

import com.kevinfreyap.product.domain.model.query.FilterDateOption
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import org.junit.Test
import java.time.LocalDate
import java.time.ZoneId

class DateCalculatorTest {
    @Test
    fun `when LAST_7_DAYS selected, return start date 7 days ago and null end date`() {
        val option = FilterDateOption.LAST_7_DAYS

        // Expected
        val zoneId = ZoneId.systemDefault()
        val expectedStart = LocalDate.now(zoneId).minusDays(7)
            .atStartOfDay(zoneId)
            .toInstant()
            .toEpochMilli()

        // Act
        val result = DateCalculator.calculateDateRange(option)

        assertEquals(expectedStart, result.startMillis)
        assertNull(result.endMillis)
    }

    @Test
    fun `when THIS_MONTH selected, return boundaries from start of month to today`() {
        val option = FilterDateOption.THIS_MONTH

        val zoneId = ZoneId.systemDefault()
        val today = LocalDate.now(zoneId)

        val expectedStart = today.withDayOfMonth(1)
            .atStartOfDay(zoneId)
            .toInstant()
            .toEpochMilli()

        val expectedEnd = today.plusDays(1)
            .atStartOfDay(zoneId)
            .toInstant()
            .toEpochMilli() - 1L

        val result = DateCalculator.calculateDateRange(option)

        assertEquals(expectedStart, result.startMillis)
        assertEquals(expectedEnd, result.endMillis)
    }

    @Test
    fun `when option is null or unrecognized, return empty boundaries`() {
        val result = DateCalculator.calculateDateRange(null)

        assertNull(result.startMillis)
        assertNull(result.endMillis)
    }
}