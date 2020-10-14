package com.hfridland.multitimer.ui.timers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.WindowManager;

import com.hfridland.multitimer.common.SingleFragmentActivity;

public class TimersActivity extends SingleFragmentActivity {

    @Override
    protected Fragment getFragment() {
        return TimersFragment.newInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN |
//                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
//                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN |
//                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
//                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

//        int flags = WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON;
//        getWindow().addFlags(flags);
    }
}
