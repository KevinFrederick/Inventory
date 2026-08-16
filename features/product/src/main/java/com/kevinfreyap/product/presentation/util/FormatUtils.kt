package com.kevinfreyap.product.presentation.util

import java.text.NumberFormat
import java.util.Locale

val locale = Locale.Builder()
    .setLanguage("id")
    .setRegion("ID")
    .build()

fun Int.toFormattedNumber(): String {
    val formatter = NumberFormat.getNumberInstance(locale)

    return formatter.format(this)
}

fun Double.toFormattedCurrency(): String {
    val formatter = NumberFormat.getCurrencyInstance(locale)

    formatter.maximumFractionDigits = 0
    return formatter.format(this)
}