package com.kevinfreyap.ui.util

object DateInputFieldValidationHelper {
    fun isValidPartialDate(input: String): Boolean {
        if (input.isEmpty()) return true
        if (input.length > 8) return false

        if (input.length == 1) {
            if (input[0] !in '0'..'3') return false
        }
        if (input.length >= 2) {
            val day = input.substring(0, 2).toIntOrNull() ?: 0
            if (day !in 1..31) return false
        }

        // Month
        if (input.length == 3) {
            if (input[2] !in '0'..'1') return false
        }
        if (input.length >= 4) {
            val month = input.substring(2, 4).toIntOrNull() ?: 0
            if (month !in 1..12) return false

            val day = input.substring(0, 2).toInt()
            when (month) {
                4, 6, 9, 11 -> if (day > 30) return false
                2 -> if (day > 29) return false
            }
        }

        // Year
        if (input.length == 5) {
            if (input[4] !in '1'..'2') return false
        }
        if (input.length == 6) {
            val y1 = input[4]
            val y2 = input[5]

            if (y1 == '1' && y2 != '9') return false
        }
        if (input.length == 7) {
            val y123 = input.substring(4, 7)

            if (y123 < "196") return false
        }
        if (input.length == 8) {
            val year = input.substring(4, 8).toIntOrNull() ?: 0
            if (year < 1960) return false

            val day = input.substring(0, 2).toInt()
            val month = input.substring(2, 4).toInt()
            if (month == 2 && day == 29) {
                val isLeapYear = (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
                if (!isLeapYear) return false
            }
        }
        return true
    }
}