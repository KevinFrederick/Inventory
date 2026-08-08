package com.kevinfreyap.product.domain.util

import com.kevinfreyap.product.domain.model.query.DateBoundaries
import com.kevinfreyap.product.domain.model.query.FilterDateOption
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset

object DateCalculator {
    fun calculateDateRange(option: FilterDateOption?): DateBoundaries {
        val zoneId = ZoneId.systemDefault()
        val today = LocalDate.now(zoneId)

        return when(option) {
            FilterDateOption.LAST_7_DAYS -> {
                val sevenDaysAgoDate = today.minusDays(7)

                val startOfDay = sevenDaysAgoDate.atStartOfDay(zoneId)
                val start = startOfDay.toInstant().toEpochMilli()

                DateBoundaries(startMillis = start, endMillis = null)
            }

            FilterDateOption.THIS_MONTH -> {
                val startMillis = today.withDayOfMonth(1)
                    .atStartOfDay(zoneId)
                    .toInstant()
                    .toEpochMilli()

                val endMillis = today.plusDays(1)
                    .atStartOfDay(zoneId)
                    .toInstant()
                    .toEpochMilli() - 1L

                DateBoundaries(startMillis = startMillis, endMillis = endMillis)
            }

            else -> DateBoundaries()
        }
    }
}

fun Long.toStartOfDayMillis(): Long {
    val localDate = Instant.ofEpochMilli(this)
        .atZone(ZoneOffset.UTC)
        .toLocalDate()

    return localDate.atStartOfDay(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()
}

fun Long.toEndOfDayMillis(): Long {
    val localDate = Instant.ofEpochMilli(this)
        .atZone(ZoneOffset.UTC)
        .toLocalDate()

    return localDate.atTime(LocalTime.MAX)
        .atZone(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()
}

fun Long.toUtcForDatePicker(): Long {
    val localDate = Instant.ofEpochMilli(this)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()

    return localDate.atStartOfDay(ZoneOffset.UTC)
        .toInstant()
        .toEpochMilli()
}