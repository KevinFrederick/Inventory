package com.kevinfreyap.product.presentation.util

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object DateFormatter {
    private val zoneId = ZoneId.systemDefault()

    fun formatDayMonthYearDate(dateMillis: Long): String {
        val dateFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy")
            .withZone(zoneId)

        val dateInstant = Instant.ofEpochMilli(dateMillis)

        return dateFormatter.format(dateInstant)
    }
}