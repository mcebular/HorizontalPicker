package com.github.mcebular.horizontalpicker

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.github.mcebular.horizontalpicker.databinding.HorizontalPickerBinding
import java.time.LocalDate

class HorizontalPicker : LinearLayout {

    companion object {
        private val DEFAULT_DAYS = 90
        private val DEFAULT_OFFSET = 7
    }

    var showTodayButton: Boolean = true
    var selectOnScroll: Boolean = true

    var days: Int = -1
    var offset: Int = -1

    private lateinit var binding: HorizontalPickerBinding
    private var listener: DatePickerListener? = null

    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun post(action: Runnable?): Boolean {
        return binding.recyclerView.post(action)
    }

    fun init() {
        val view = inflate(context, R.layout.horizontal_picker, this)
        binding = HorizontalPickerBinding.bind(view)

        binding.recyclerView.listener = object : HorizontalPickerListener {
            override fun onStopDraggingPicker() {
                if (binding.layoutHover.visibility == VISIBLE) binding.layoutHover.visibility = INVISIBLE
            }

            override fun onDraggingPicker() {
                if (binding.layoutHover.visibility == INVISIBLE) binding.layoutHover.visibility = VISIBLE
            }

            override fun onDateSelected(item: Day) {
                binding.textViewMonth.text = item.month
                if (showTodayButton) binding.textViewToday.visibility = if (item.isToday) INVISIBLE else VISIBLE
                listener?.onDateSelected(item.date)
            }
        }

        binding.textViewToday.let {
            it.setOnClickListener(binding.recyclerView)
            it.visibility = if (showTodayButton) VISIBLE else INVISIBLE
            it.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
        }

        val bgColor = getBackgroundColor()
        setBackgroundColor(if (bgColor != Color.TRANSPARENT) bgColor else Color.WHITE)

        binding.recyclerView.init(
            if (days < 0) DEFAULT_DAYS else days,
            if (offset < 0) DEFAULT_OFFSET else offset,
            selectOnScroll
        )
    }

    //

    fun setListener(listener: DatePickerListener): HorizontalPicker {
        this.listener = listener
        return this
    }

    /**
     * Number of days to be generated in the view. Default value is 90 days.
     * @param days number of days to generate
     * @throws IllegalArgumentException if [days] parameter is negative
     * @return this [HorizontalPicker] instance for chaining method calls
     */
    fun setDays(days: Int): HorizontalPicker {
        if (days < 0) throw IllegalArgumentException("Number of days must be positive")
        this.days = days
        return this
    }

    /**
     * Number of days to offset from today. Setting offset to 0 will make all but today's day in the
     * future. Default value is 7 days.
     * @param offset number of days before today's date
     * @throws IllegalArgumentException if [offset] parameter is negative
     * @return this [HorizontalPicker] instance for chaining method calls
     */
    fun setOffset(offset: Int): HorizontalPicker {
        if (offset < 0) throw IllegalArgumentException("Offset must be positive")
        this.offset = offset
        return this
    }

    fun setShowTodayButton(showTodayButton: Boolean): HorizontalPicker {
        this.showTodayButton = showTodayButton
        return this
    }

    fun setSelectOnScroll(selectOnScroll: Boolean): HorizontalPicker {
        this.selectOnScroll = selectOnScroll
        return this
    }

    fun setDate(date: LocalDate) {
        binding.recyclerView.let {
            it.post { it.setDate(date) }
        }
    }

    //

    private fun getBackgroundColor(): Int {
        var color = Color.TRANSPARENT
        val background = background
        if (background is ColorDrawable) color = background.color
        return color
    }

}