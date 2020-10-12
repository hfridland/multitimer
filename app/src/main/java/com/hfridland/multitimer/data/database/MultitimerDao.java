package com.hfridland.multitimer.data.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.hfridland.multitimer.data.model.TimerItem;

import java.util.List;

@Dao
public interface MultitimerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTimerItem(TimerItem timerItem);

    @Query("delete from timeritem where id = :id")
    void deleteTimerItem(int id);

    @Query("select * from TimerItem")
    List<TimerItem> getTimerItems();

    @Query("select * from TimerItem where active = 1")
    List<TimerItem> getActiveTimerItems();

    @Query("select * from timeritem where id = :id")
    TimerItem getTimerItemById(int id);
}
