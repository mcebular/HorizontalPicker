package com.github.mcebular.horizontalpicker.example

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.mcebular.horizontalpicker.DatePickerListener
import com.github.mcebular.horizontalpicker.example.databinding.ActivityMainBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity(), DatePickerListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.datePicker
            .setListener(this)
            .setDays(120)
            .setOffset(7)
            .setShowTodayButton(true)
            .init()

        binding.datePicker.setDate(LocalDate.now())
    }

    override fun onDateSelected(dateSelected: LocalDate) {
        Toast.makeText(baseContext, "Selected date: " + dateSelected.format(DateTimeFormatter.ISO_LOCAL_DATE), Toast.LENGTH_SHORT).show()
    }

}