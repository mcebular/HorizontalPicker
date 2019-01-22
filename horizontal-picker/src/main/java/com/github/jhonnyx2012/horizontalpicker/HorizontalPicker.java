package com.github.jhonnyx2012.horizontalpicker;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.joda.time.DateTime;

/**
 * Created by Jhonny Barrios on 22/02/2017.
 */

public class HorizontalPicker extends LinearLayout {

    private static final int
            DEFAULT_DAYS = 90,
            DEFAULT_OFFSET = 7;

    private DatePickerListener listener;

    private HorizontalPickerRecyclerView rvDays;

    private View vHover;
    private TextView tvMonth;
    private TextView tvToday;

    // number of generated days
    private int days = -1;

    // number of days before today's date
    private int offset = -1;

    private boolean showTodayButton = true;
    private boolean selectOnScroll = true;

    public HorizontalPicker(Context context) {
        super(context);
    }

    public HorizontalPicker(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

    }

    public HorizontalPicker(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public HorizontalPicker setListener(DatePickerListener listener) {
        this.listener = listener;
        return this;
    }

    public void setDate(final DateTime date) {
        rvDays.post(new Runnable() {
            @Override
            public void run() {
                rvDays.setDate(date);
            }
        });
    }

    public void init() {
        inflate(getContext(), R.layout.horizontal_picker, this);
        rvDays = findViewById(R.id.rvDays);

        int finalDays = days == -1 ? DEFAULT_DAYS : days;
        int finalOffset = offset == -1 ? DEFAULT_OFFSET : offset;

        vHover = findViewById(R.id.vHover);
        tvMonth = findViewById(R.id.tvMonth);

        rvDays.setListener(new HorizontalPickerListener() {
            @Override
            public void onStopDraggingPicker() {
                if (vHover.getVisibility() == VISIBLE) vHover.setVisibility(INVISIBLE);
            }

            @Override
            public void onDraggingPicker() {
                if (vHover.getVisibility() == INVISIBLE) vHover.setVisibility(VISIBLE);
            }

            @Override
            public void onDateSelected(Day item) {
                tvMonth.setText(item.getMonth());
                if (showTodayButton)
                    tvToday.setVisibility(item.isToday() ? INVISIBLE : VISIBLE);
                if (listener != null) {
                    listener.onDateSelected(item.getDate());
                }
            }
        });

        tvToday = findViewById(R.id.tvToday);
        tvToday.setOnClickListener(rvDays);
        tvToday.setVisibility(showTodayButton ? VISIBLE : INVISIBLE);
        tvToday.setTextColor(getColor(R.color.colorPrimary));

        int mBackgroundColor = getBackgroundColor();
        setBackgroundColor(mBackgroundColor != Color.TRANSPARENT ? mBackgroundColor : Color.WHITE);

        rvDays.init(
                finalDays,
                finalOffset,
                mBackgroundColor,
                selectOnScroll);
    }

    private int getColor(int colorId) {
        return getResources().getColor(colorId);
    }

    public int getBackgroundColor() {
        int color = Color.TRANSPARENT;
        Drawable background = getBackground();
        if (background instanceof ColorDrawable)
            color = ((ColorDrawable) background).getColor();
        return color;
    }

    @Override
    public boolean post(Runnable action) {
        return rvDays.post(action);
    }

    /**
     * Number of days to be generated in the view. Default value is 90 days.
     * @param days number of days to generate
     * @throws IllegalArgumentException if passed number of days is negative
     * @return this HorizontalPicker instance for chaining method calls
     */
    public HorizontalPicker setDays(int days) {
        if (days < 0) throw new IllegalArgumentException("number of days must be positive");
        this.days = days;
        return this;
    }

    public int getDays() {
        return days;
    }

    /**
     * Number of days to offset from today. Setting offset to 0 will make all but today's day in the
     * future. Default value is 7 days.
     * @param offset number of days before today's date, must be positive
     * @throws IllegalArgumentException if passed offset is negative
     * @return this HorizontalPicker instance for chaining method calls
     */
    public HorizontalPicker setOffset(int offset) {
        if (offset < 0) throw new IllegalArgumentException("offset must be positive");
        this.offset = offset;
        return this;
    }

    public int getOffset() {
        return offset;
    }

    public HorizontalPicker showTodayButton(boolean show) {
        showTodayButton = show;
        return this;
    }

    public HorizontalPicker setSelectOnScroll(boolean selectOnScroll) {
        this.selectOnScroll = selectOnScroll;
        return this;
    }
}