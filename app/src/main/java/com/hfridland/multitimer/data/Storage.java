package com.hfridland.multitimer.data;

import com.hfridland.multitimer.data.database.MultitimerDao;
import com.hfridland.multitimer.data.model.TimerItem;

import java.util.List;

public class Storage {

    private MultitimerDao mMultitimerDao;

    public Storage(MultitimerDao multitimerDao) {
        mMultitimerDao = multitimerDao;
    }

    public void saveTimerItem(TimerItem timerItem) {
        mMultitimerDao.insertTimerItem(timerItem);
    }

    public List<TimerItem> getTimerItems() {
        return mMultitimerDao.getTimerItems();
    }

    public List<TimerItem> getActiveTimerItems() {
        return mMultitimerDao.getActiveTimerItems();
    }

    public void deleteTimerItem(int id) { mMultitimerDao.deleteTimerItem(id); }

    public TimerItem getTimerItemById(int id) { return  mMultitimerDao.getTimerItemById(id); }
}
