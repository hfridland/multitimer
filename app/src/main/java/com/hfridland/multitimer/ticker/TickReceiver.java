package com.hfridland.multitimer.ticker;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;

import com.hfridland.multitimer.AppDelegate;
import com.hfridland.multitimer.ui.timers.TimersActivity;
import com.hfridland.multitimer.ui.timers.TimersFragment;

public class TickReceiver extends BroadcastReceiver {
    public static final String TICK_ACTION = "com.hfridland.multitimer.ticker.TICK_ACTION";

    private final Ticker mTicker;

    public TickReceiver(Ticker ticker) {
        mTicker = ticker;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        mTicker.onTick(intent);
    }

    public interface Ticker {
        void onTick(Intent intent);
    }
}
