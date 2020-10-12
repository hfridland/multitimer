package com.hfridland.multitimer;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.hfridland.multitimer.data.Storage;
import com.hfridland.multitimer.data.database.MultitimerDatabase;
import com.hfridland.multitimer.ui.timers.TimersFragment;

public class AppDelegate extends Application {

    private static Storage mStorage;

    public static Storage getStorage() {
        return mStorage;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        final MultitimerDatabase mMultitimerDatabase = Room.databaseBuilder(getApplicationContext(), MultitimerDatabase.class, "multitimer_database")
                .allowMainThreadQueries()
                .build();
        mStorage = new Storage(mMultitimerDatabase.getMultitimerDao());
    }

}
