package com.github.jhonnyx2012.horizontalpicker;


import android.app.AlarmManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by jhonn on 22/02/2017.
 */

public class HorizontalPickerAdapter extends RecyclerView.Adapter<HorizontalPickerAdapter.ViewHolder> {

    private static final long DAY_MILLIS = AlarmManager.INTERVAL_DAY;
    private int itemWidth;
    private final OnItemClickedListener listener;
    private ArrayList<Day> items;
    private boolean selectOnScroll;

    public HorizontalPickerAdapter(int itemWidth,
                                   OnItemClickedListener listener,
                                   Context context,
                                   int daysToCreate,
                                   int offset,
                                   boolean selectOnScroll) {
        items=new ArrayList<>();
        this.itemWidth=itemWidth;
        this.listener=listener;
        generateDays(daysToCreate, DateTime.now().minusDays(offset));
        this.selectOnScroll = selectOnScroll;
    }

    public void generateDays(int n, DateTime initialDate) {
        items.clear();

        for (int i = 0; i<n; i++) {
            DateTime date = new DateTime(initialDate).plusDays(i);
            items.add(new Day(date));
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_day,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Day item = getItem(position);

        holder.tvDay.setText(item.getDay());
        holder.tvWeekDay.setText(item.getWeekDay());
        if(item.isSelected())
        {
            holder.tvDay.setBackgroundDrawable(getDaySelectedBackground(holder.itemView));
            holder.tvDay.setTextColor(Color.WHITE);
        }
        else if(item.isToday())
        {
            holder.tvDay.setBackgroundDrawable(getDayTodayBackground(holder.itemView));
            holder.tvDay.setTextColor(holder.tvDay.getResources().getColor(R.color.textColorPrimary));
        }
        else
        {
            holder.tvDay.setBackgroundColor(0);
            holder.tvDay.setTextColor(holder.tvDay.getResources().getColor(R.color.textColorPrimary));
        }
    }

    private Drawable getDaySelectedBackground(View view) {
        Drawable drawable=view.getResources().getDrawable(R.drawable.background_day_selected);
        DrawableCompat.setTint(drawable, view.getResources().getColor(R.color.colorPrimary));
        return drawable;
    }

    private Drawable getDayTodayBackground(View view) {
        Drawable drawable = view.getResources().getDrawable(R.drawable.background_day_today);
        return drawable;
    }

    public Day getItem(int position) {
        return items.get(position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvDay,tvWeekDay;

        public ViewHolder(View itemView) {
            super(itemView);
            tvDay= (TextView) itemView.findViewById(R.id.tvDay);
            tvDay.setWidth(itemWidth);
            tvWeekDay= (TextView) itemView.findViewById(R.id.tvWeekDay);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClickView(v,getAdapterPosition());
        }
    }
}