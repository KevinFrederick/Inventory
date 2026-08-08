package com.kevinfreyap.product.presentation.util

import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.TimeZone

object DateFormatter {

    fun formatDatePickerDate(dateMillis: Long): String {
        val dateFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy")
            .withZone(ZoneId.systemDefault())

        val dateInstant = Instant.ofEpochMilli(dateMillis)

        return dateFormatter.format(dateInstant)
    }

    fun parseDateStringToLong(dateString: String): Long? {
        val inputFormatter = SimpleDateFormat("ddMMyyyy", Locale.getDefault()).apply {
            timeZone = TimeZone.getTimeZone("UTC")
            isLenient = false
        }

        return try {
            inputFormatter.parse(dateString)?.time
        } catch (_: Exception) {
            null
        }
    }

    fun formatDateLongToString(dateMillis: Long?): String {
        val inputFormatter = SimpleDateFormat("ddMMyyyy", Locale.getDefault()).apply {
            timeZone = TimeZone.getTimeZone("UTC")
            isLenient = false
        }

        return if (dateMillis != null) {
            inputFormatter.format(dateMillis)
        } else ""
    }
}