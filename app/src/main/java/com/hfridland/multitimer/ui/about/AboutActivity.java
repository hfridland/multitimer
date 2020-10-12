package com.hfridland.multitimer.ui.about;

import android.support.v4.app.Fragment;

import com.hfridland.multitimer.common.SingleFragmentActivity;

public class AboutActivity extends SingleFragmentActivity {
    @Override
    protected Fragment getFragment() {
        return AboutFragment.newInstance();
    }
}
