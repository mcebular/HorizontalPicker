package com.github.mcebular.horizontalpicker

import java.time.LocalDate

interface DatePickerListener {
    fun onDateSelected(dateSelected: LocalDate)
}