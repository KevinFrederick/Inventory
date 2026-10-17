package com.kevinfreyap.ui.util

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

fun Long.toFullDateString(
    locale: Locale = Locale.getDefault(),
    zoneId: ZoneId = ZoneId.systemDefault()
): String {
    val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", locale)
    val instant = Instant.ofEpochMilli(this)

    return instant.atZone(zoneId).format(formatter)
}