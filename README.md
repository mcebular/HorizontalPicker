Horizontal Picker
=================

[![](https://jitpack.io/v/mcebular/HorizontalPicker.svg)](https://jitpack.io/#mcebular/HorizontalPicker)

What is this?
-------------
HorizontalPicker is a custom-build Android View used for choosing dates (similar to the native date 
picker) but draws horizontally into a vertically narrow container. It allows easy day picking using 
the horizontal pan gesture.

This is what it looks like.

![Screenshot_1](https://raw.githubusercontent.com/mcebular/HorizontalPicker/master/Screenshot_custom.png)
![Screenshot_2](https://raw.githubusercontent.com/mcebular/HorizontalPicker/master/Screenshot_palette.png)

Features
--------

* Date selection using a smooth swipe gesture
* Date selection by clicking on a day slot
* Date selection from code using the HorizontalPicker java object
* Month and year view
* _Today_ button to jump to the current day
* Localized day and month names
* Configurable number of generated days (default: 120)
* Configurable number of offset generated days before the current date (default: 7)
* Customizable set of colors, or themed through the app theming engine

**Note**: This library uses the `java.time.LocalDate` via `coreLibraryDesugaring`.

Requirements
------------
- Android 5 or later (Minimum SDK level 21)
- Android Studio (to compile and use)

Getting Started
---------------

In your app module's Gradle config file, add the following dependency:
```groovy
dependencies {
    implementation 'com.github.mcebular:HorizontalPicker:2.0.0'
}
```

Then to include it into your layout, add the following:
```xml
<com.github.mcebular.horizontalpicker.HorizontalPicker
    android:id="@+id/datePicker"
    android:layout_width="match_parent"
    android:layout_height="wrap_content" />
```

In your activity, you need to initialize it and set a listener, like this:
```kotlin
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
```

```text
Copyright 2023 Jhonny Barrios, Martin Čebular

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
```
