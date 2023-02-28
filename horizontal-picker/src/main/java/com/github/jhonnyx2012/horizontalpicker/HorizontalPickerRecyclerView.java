package com.github.jhonnyx2012.horizontalpicker;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import org.joda.time.DateTime;
import org.joda.time.Days;

/**
 * Created by Jhonny Barrios on 22/02/2017.
 *
 */

public class HorizontalPickerRecyclerView extends RecyclerView implements OnItemClickedListener, View.OnClickListener {


    private HorizontalPickerAdapter adapter;

    // picker's positions
    private int lastPosition;
    private int offset;

    private LinearLayoutManager layoutManager;
    private float itemWidth;

    private HorizontalPickerListener listener;

    private boolean selectOnScroll;

    //

    public HorizontalPickerRecyclerView(Context context) {
        super(context);
    }

    public HorizontalPickerRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HorizontalPickerRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    //

    public void init(final int daysToPlus, final int initialOffset, final int mBackgroundColor, final boolean selectOnScroll) {
        this.offset = initialOffset;
        this.lastPosition = initialOffset;
        this.selectOnScroll = selectOnScroll;

        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        setLayoutManager(layoutManager);

        post(new Runnable() {
            @Override
            public void run() {
                itemWidth = getMeasuredWidth() / 7f;
                adapter = new HorizontalPickerAdapter(
                        (int) itemWidth,
                        HorizontalPickerRecyclerView.this,
                        getContext(),
                        daysToPlus,
                        initialOffset,
                        selectOnScroll);
                setAdapter(adapter);
                LinearSnapHelper snapHelper=new LinearSnapHelper();
                snapHelper.attachToRecyclerView(HorizontalPickerRecyclerView.this);
                removeOnScrollListener(onScrollListener);
                addOnScrollListener(onScrollListener);
            }
        });
    }

    private OnScrollListener onScrollListener = new OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            switch (newState) {
                case RecyclerView.SCROLL_STATE_IDLE:
                    if (selectOnScroll) {
                        listener.onStopDraggingPicker();
                        int position = (int) ((computeHorizontalScrollOffset() / itemWidth) + 3.5);
                        if (position != -1 && position != lastPosition) {
                            selectItem(position);
                        }
                    }
                    break;
                case SCROLL_STATE_DRAGGING:
                    if (selectOnScroll) {
                        listener.onDraggingPicker();
                    }
            break;
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }
    };

    private void selectItem(int position) {
        adapter.getItem(lastPosition).setSelected(false);
        adapter.notifyItemChanged(lastPosition);

        adapter.getItem(position).setSelected(true);
        adapter.notifyItemChanged(position);

        listener.onDateSelected(adapter.getItem(position));
        lastPosition = position;
    }

    public void setListener(HorizontalPickerListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClickView(View v, int adapterPosition) {
        selectItem(adapterPosition);
    }

    @Override
    public void onClick(View v) {
        selectItem(offset);
        smoothScrollToPosition(offset);
    }

    @Override
    public void smoothScrollToPosition(int position) {
        final RecyclerView.SmoothScroller smoothScroller = new CenterSmoothScroller(getContext());
        smoothScroller.setTargetPosition(position);
        post(new Runnable() {
            @Override
            public void run() {
                layoutManager.startSmoothScroll(smoothScroller);
            }
        });
    }

    public void setDate(DateTime date) {
        int prefix = date.isBefore(DateTime.now()) ? -1 : 1;
        int difference = Days.daysBetween(date, DateTime.now()).getDays() * prefix;

        smoothScrollToPosition(offset + difference);
        selectItem(offset + difference);
    }

    private static class CenterSmoothScroller extends LinearSmoothScroller {

        CenterSmoothScroller(Context context) {
            super(context);
        }

        @Override
        public int calculateDtToFit(int viewStart, int viewEnd, int boxStart, int boxEnd, int snapPreference) {
            return (boxStart + (boxEnd - boxStart) / 2) - (viewStart + (viewEnd - viewStart) / 2);
        }
    }
}
