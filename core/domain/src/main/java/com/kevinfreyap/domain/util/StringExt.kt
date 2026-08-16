package com.kevinfreyap.domain.util

fun String.cleanInlineSpaces(): String {
    return this
        .trim()
        .replace(" {2,}".toRegex(), " ")
}

fun String.toTitleCase(): String {
    return this
        .lowercase()
        .split(" ")
        .joinToString(" ") { word ->
            word.replaceFirstChar { it.titlecase() }
        }
}

fun String.toShortId(): String {
    return this.substring(6, 12).uppercase()
}