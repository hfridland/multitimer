package com.hfridland.multitimer.ui.alarm;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.WindowManager;

import com.hfridland.multitimer.common.SingleFragmentActivity;

public class AlarmActivity extends SingleFragmentActivity {
    @Override
    protected Fragment getFragment() {
        return AlarmFragment.newInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_FULLSCREEN |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

    }
}
