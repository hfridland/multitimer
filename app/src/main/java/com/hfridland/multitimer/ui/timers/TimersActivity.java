package com.hfridland.multitimer.ui.timers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.hfridland.multitimer.common.SingleFragmentActivity;

public class TimersActivity extends SingleFragmentActivity {

    @Override
    protected Fragment getFragment() {
        return TimersFragment.newInstance();
    }
}
