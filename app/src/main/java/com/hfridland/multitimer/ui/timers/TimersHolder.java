package com.hfridland.multitimer.ui.timers;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.hfridland.multitimer.R;
import com.hfridland.multitimer.data.model.TimerItem;
import com.hfridland.multitimer.databinding.TimerItemBinding;

public class TimersHolder extends RecyclerView.ViewHolder {
    private TimerItemBinding mTimerItemBinding;

    public TimersHolder(TimerItemBinding binding) {
        super(binding.getRoot());

        mTimerItemBinding = binding;
    }

    public void bind(TimerItem item, TimersAdapter.OnTimerItemClickListener onTimerItemClickListener) {
        mTimerItemBinding.setTimerItem(new TimerItemViewModel(item));
        mTimerItemBinding.setOnTimerItemClickListener(onTimerItemClickListener);
        mTimerItemBinding.executePendingBindings();
    }
}
