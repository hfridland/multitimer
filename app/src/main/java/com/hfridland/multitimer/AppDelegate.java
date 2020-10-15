package com.hfridland.multitimer;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.hfridland.multitimer.data.database.MultitimerDao;
import com.hfridland.multitimer.data.database.MultitimerDatabase;

public class AppDelegate extends Application {

    private static MultitimerDao sMultitimerDao;

    public static MultitimerDao getMultitimerDao() {
        return sMultitimerDao;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        sMultitimerDao =
                Room.databaseBuilder(getApplicationContext(), MultitimerDatabase.class, "multitimer_database")
                .allowMainThreadQueries()
                .build().getMultitimerDao();
    }

}
