package com.github.mcebular.horizontalpicker

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.github.mcebular.horizontalpicker.databinding.ItemDayBinding
import java.time.LocalDate

class HorizontalPickerAdapter(
    private val listener: OnItemClickListener,
    private var itemWidth: Int,
    daysToCreate: Int,
    offset: Int
) : RecyclerView.Adapter<HorizontalPickerAdapter.ViewHolder>() {

    private val items: ArrayList<Day> = ArrayList()

    init {
        generateDays(daysToCreate, LocalDate.now().minusDays(offset.toLong()))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        val binding = holder.binding

        binding.textViewDay.text = item.day
        binding.textViewWeekDay.text = item.weekDay

        if (item.selected) {
            binding.textViewDay.background = getDaySelectedBackground(binding.root)
            binding.textViewDay.setTextColor(Color.WHITE)
        } else if (item.isToday) {
            binding.textViewDay.background = getDayTodayBackground(binding.root)
            binding.textViewDay.setTextColor(ContextCompat.getColor(binding.root.context, R.color.textColorPrimary))
        } else {
            binding.textViewDay.setBackgroundColor(0)
            binding.textViewDay.setTextColor(ContextCompat.getColor(binding.root.context, R.color.textColorPrimary))
        }

    }

    fun getItem(position: Int): Day {
        return items[position]
    }

    private fun generateDays(n: Int, initialDate: LocalDate) {
        items.clear()
        for (i in 0 until n) {
            val date: LocalDate = initialDate.plusDays(i.toLong())
            items.add(Day(date))
        }
    }

    private fun getDaySelectedBackground(view: View): Drawable? {
        val drawable = ContextCompat.getDrawable(view.context, R.drawable.background_day_selected)
        val color = ContextCompat.getColor(view.context, R.color.colorPrimary)
        drawable?.let { DrawableCompat.setTint(it, color) }
        return drawable
    }

    private fun getDayTodayBackground(view: View): Drawable? {
        return ContextCompat.getDrawable(view.context, R.drawable.background_day_today)
    }

    inner class ViewHolder(internal var binding: ItemDayBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            binding.textViewDay.width = itemWidth
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            listener.onClickView(v, bindingAdapterPosition)
        }
    }
}