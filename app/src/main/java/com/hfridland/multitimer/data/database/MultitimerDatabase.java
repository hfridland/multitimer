package com.hfridland.multitimer.data.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.hfridland.multitimer.data.model.TimerItem;

@Database(entities = {TimerItem.class}, version = 1)
public abstract class MultitimerDatabase extends RoomDatabase {
    public abstract MultitimerDao getMultitimerDao();
}
