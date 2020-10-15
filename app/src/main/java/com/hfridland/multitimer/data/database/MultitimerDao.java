package com.hfridland.multitimer.data.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import com.hfridland.multitimer.data.model.TimerItem;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public abstract class MultitimerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract long insertTimerItem(TimerItem timerItem);

    @Query("delete from timeritem where id = :id")
    public abstract int deleteTimerItem(int id);

    @Query("select * from TimerItem order by active desc, name")
    public abstract List<TimerItem> getTimerItems();

    @Query("select * from TimerItem where active = 1")
    public abstract List<TimerItem> getActiveTimerItems();

    @Query("select * from TimerItem where active = 1")
    public abstract Single<List<TimerItem>> getActiveTimerItemsRx();


    @Query("select * from timeritem where id = :id")
    public abstract TimerItem getTimerItemById(int id);

    @Transaction
    public TimerItem changeActive(int id) {
        TimerItem timerItem = getTimerItemById(id);
        timerItem.setActive(!timerItem.isActive());
        insertTimerItem(timerItem);
        return timerItem;
    }

    @Query("select * from TimerItem order by active desc, name")
    public abstract Single<List<TimerItem>> getTimerItemsRx();

    @Query("select * from timeritem where id = :id")
    public abstract Single<TimerItem> getTimerItemByIdRx(int id);


}
