package com.hfridland.multitimer.ui.timers;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hfridland.multitimer.AppDelegate;
import com.hfridland.multitimer.R;

import com.hfridland.multitimer.data.Storage;
import com.hfridland.multitimer.data.model.TimerItem;
import com.hfridland.multitimer.databinding.TimerItemBinding;

import java.util.ArrayList;
import java.util.List;

public class TimersAdapter extends RecyclerView.Adapter<TimersHolder> {
    @NonNull
    private final List<TimerItem> mTimerItems = new ArrayList<>();
    private final OnTimerItemClickListener mOnTimerItemClickListener;

    public TimersAdapter(OnTimerItemClickListener onTimerItemClickListener) {
        mOnTimerItemClickListener = onTimerItemClickListener;
        updateData();
    }

    @NonNull
    @Override
    public TimersHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        TimerItemBinding binding = TimerItemBinding.inflate(inflater, parent, false);
        return new TimersHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TimersHolder holder, int position) {
        TimerItem timerItem = mTimerItems.get(position);
        holder.bind(timerItem, mOnTimerItemClickListener);
    }

    @Override
    public int getItemCount() {
        return mTimerItems.size();
    }

    public void updateData() {
        mTimerItems.clear();
        final Storage storage = AppDelegate.getStorage();
        mTimerItems.addAll(storage.getTimerItems());
        notifyDataSetChanged();
    }

    public interface OnTimerItemClickListener {
        void onStartStopClick(int id);
        void onEditClick(int id);
        void onDeleteClick(int id);
    }

}
