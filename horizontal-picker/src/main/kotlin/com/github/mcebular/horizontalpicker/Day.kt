package com.github.mcebular.horizontalpicker

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class Day(val date: LocalDate) {
    var selected = false
    var monthPattern = "MMMM YYYY"

    val day: String
        get() {
            return date.dayOfMonth.toString()
        }

    val weekDay: String
        get() {
            return date.format(DateTimeFormatter.ofPattern("EEE", Locale.getDefault()))
        }

    val month: String
        get() {
            return date.format(DateTimeFormatter.ofPattern(monthPattern, Locale.getDefault()))
        }

    val isToday: Boolean
        get() {
            return LocalDate.now().isEqual(date)
        }
}