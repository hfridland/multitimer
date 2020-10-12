package com.hfridland.multitimer.ticker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.hfridland.multitimer.data.model.TimerItem;
import com.hfridland.multitimer.ui.alarm.AlarmActivity;
import com.hfridland.multitimer.ui.alarm.AlarmFragment;
import com.hfridland.multitimer.ui.timers.TimersActivity;

public class AlarmReceiver extends BroadcastReceiver {
    public static final String ALARM_ACTION = "com.hfridland.multitimer.ticker.ALARM_ACTION";


    @Override
    public void onReceive(Context context, Intent intent) {

        TimerItem timerItem = (TimerItem) intent.getSerializableExtra(AlarmFragment.TIMER_ITEM);

        //Intent launchIntent = new Intent(context, AlarmActivity.class);
        Intent launchIntent = new Intent(context, TimersActivity.class);
        launchIntent.setAction(ALARM_ACTION);
        launchIntent.putExtra(AlarmFragment.TIMER_ITEM, timerItem);
        //launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(launchIntent);

    }
}
