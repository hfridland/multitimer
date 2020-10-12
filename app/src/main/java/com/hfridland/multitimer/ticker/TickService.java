package com.hfridland.multitimer.ticker;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.hfridland.multitimer.AppDelegate;
import com.hfridland.multitimer.data.Storage;
import com.hfridland.multitimer.data.model.TimerItem;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.hfridland.multitimer.R;
import com.hfridland.multitimer.ui.alarm.AlarmFragment;
import com.hfridland.multitimer.ui.timers.TimersActivity;

import static com.hfridland.multitimer.utils.StringUtils.duration2String;
import static java.lang.Math.round;

public class TickService extends Service {
    public static final String TAG = TickService.class.getSimpleName();
    public static final String HAS_EXP_ITEM = "HAS_EXP_ITEM";
    public static final String ACTIVE_ITEMS_COUNT = "ACTIVE_ITEMS_COUNT";


    private ScheduledExecutorService mScheduledExecutorService;
    private NotificationManager mManager;
    private NotificationCompat.Builder mBuilder;

    public TickService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        mScheduledExecutorService = Executors.newScheduledThreadPool(1);

        mManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mBuilder = getNotificationBuilder();

        mBuilder.setContentTitle("Active Timers:")
                .setSmallIcon(R.drawable.ic_launcher_foreground);
    }

    private NotificationCompat.Builder getNotificationBuilder() {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return new NotificationCompat.Builder(this);
        } else {
            String channelId = "timers_channel_id";
            if(mManager.getNotificationChannel(channelId) == null) {
                NotificationChannel channel = new
                        NotificationChannel(channelId, "Active timers",
                        NotificationManager.IMPORTANCE_LOW);

                        mManager.createNotificationChannel(channel);
            }
            return new NotificationCompat.Builder(this, channelId);
        }
    }

    private Notification getNotification(String contentText){
        Intent intent = new Intent(this, TimersActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        return mBuilder.setContentText(contentText)
                .setContentIntent(pendingIntent)
                .build();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(1, getNotification(""));

        final Storage storage = AppDelegate.getStorage();

        mScheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                List<TimerItem> timerItems = storage.getActiveTimerItems();
                if (!timerItems.isEmpty()) {
                    mManager.notify(1, getNotification("" + timerItems.size() + " active timers" ));
                    TimerItem expiredItem = null;
                    StringBuilder sb = new StringBuilder();
                    long t = System.currentTimeMillis();
                    for(TimerItem timerItem : timerItems) {
                        if (timerItem.isActive() && timerItem.getAlarmTime() <= System.currentTimeMillis()) {
                            expiredItem = timerItem;
                            break;
                        }
                    }


                    if (expiredItem != null) {
                        expiredItem.setActive(false);
                        storage.saveTimerItem(expiredItem);

                        Intent intentAlarm = new Intent(AlarmReceiver.ALARM_ACTION);
                        intentAlarm.putExtra(AlarmFragment.TIMER_ITEM, expiredItem);
                        PackageManager packageManager = getPackageManager();
                        List<ResolveInfo> infos = packageManager.queryBroadcastReceivers(intentAlarm, 0);
                        for (ResolveInfo info : infos) {
                            ComponentName cn = new ComponentName(info.activityInfo.packageName, info.activityInfo.name);
                            intentAlarm.setComponent(cn);
                            sendBroadcast(intentAlarm);
                        }
                    } else {
                        Intent intentToSend = new Intent(TickReceiver.TICK_ACTION);
                        sendBroadcast(intentToSend);
                    }
                }
            }
        }, 500, 500, TimeUnit.MILLISECONDS);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        mManager.cancelAll();
        stopForeground(true);
        mScheduledExecutorService.shutdownNow();
    }
}
