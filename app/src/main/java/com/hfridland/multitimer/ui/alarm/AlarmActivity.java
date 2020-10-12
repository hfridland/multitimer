package com.hfridland.multitimer.ui.alarm;

import android.support.v4.app.Fragment;

import com.hfridland.multitimer.common.SingleFragmentActivity;

public class AlarmActivity extends SingleFragmentActivity {
    @Override
    protected Fragment getFragment() {
        return AlarmFragment.newInstance();
    }
}
