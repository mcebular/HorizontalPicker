package com.github.mcebular.horizontalpicker

interface HorizontalPickerListener {
    fun onStopDraggingPicker()
    fun onDraggingPicker()
    fun onDateSelected(item: Day)
}