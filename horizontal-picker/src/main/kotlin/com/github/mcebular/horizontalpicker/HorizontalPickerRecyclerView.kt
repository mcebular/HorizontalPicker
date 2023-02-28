package com.github.mcebular.horizontalpicker

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class HorizontalPickerRecyclerView : RecyclerView, OnItemClickListener, View.OnClickListener {

    private var itemWidth: Float? = null
    private var offset: Int? = null
    private var lastPosition: Int? = null
    private var selectOnScroll: Boolean = false

    private var adapter: HorizontalPickerAdapter? = null
    var listener: HorizontalPickerListener? = null
    private val onScrollListener: OnScrollListener = object : OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            when (newState) {
                SCROLL_STATE_IDLE -> if (selectOnScroll) {
                    listener?.onStopDraggingPicker()
                    val position = (computeHorizontalScrollOffset() / itemWidth!! + 3.5).toInt()
                    if (position != -1 && position != lastPosition) {
                        selectItem(position)
                    }
                }
                SCROLL_STATE_DRAGGING -> if (selectOnScroll) {
                    listener?.onDraggingPicker()
                }
            }
        }
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    fun init(daysToPlus: Int, initialOffset: Int, selectOnScroll: Boolean) {
        this.offset = initialOffset
        this.lastPosition = initialOffset
        this.selectOnScroll = selectOnScroll
        this.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        post {
            this.itemWidth = (measuredWidth / 7f)
            this.adapter = HorizontalPickerAdapter(this, itemWidth!!.toInt(), daysToPlus, initialOffset)
            setAdapter(adapter)
            val snapHelper = LinearSnapHelper()
            snapHelper.attachToRecyclerView(this)
            removeOnScrollListener(onScrollListener)
            addOnScrollListener(onScrollListener)
        }
    }

    fun setDate(date: LocalDate) {
        offset?.let { offset ->
            val difference = date.until(LocalDate.now(), ChronoUnit.DAYS).toInt()
            smoothScrollToPosition(offset + difference)
            selectItem(offset + difference)
        }
    }

    private fun selectItem(position: Int) {
        adapter?.let { adapter ->
            lastPosition?.let { pos ->
                adapter.getItem(pos).selected = false
                adapter.notifyItemChanged(pos)
            }

            adapter.getItem(position).selected = true
            adapter.notifyItemChanged(position)

            listener?.onDateSelected(adapter.getItem(position))
        }

        lastPosition = position
    }

    override fun smoothScrollToPosition(position: Int) {
        val smoothScroller = CenterSmoothScroller(context)
        smoothScroller.targetPosition = position
        post {
            layoutManager?.startSmoothScroll(smoothScroller)
        }
    }

    override fun onClickView(v: View?, adapterPosition: Int) {
        selectItem(adapterPosition)
    }

    override fun onClick(v: View?) {
        offset?.let {
            selectItem(it)
            smoothScrollToPosition(it)
        }
    }

    private class CenterSmoothScroller(context: Context?) : LinearSmoothScroller(context) {
        override fun calculateDtToFit(
            viewStart: Int,
            viewEnd: Int,
            boxStart: Int,
            boxEnd: Int,
            snapPreference: Int
        ): Int {
            return boxStart + (boxEnd - boxStart) / 2 - (viewStart + (viewEnd - viewStart) / 2)
        }
    }

}