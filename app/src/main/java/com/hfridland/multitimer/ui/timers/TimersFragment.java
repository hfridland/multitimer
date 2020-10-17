package com.hfridland.multitimer.ui.timers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.hfridland.multitimer.AppDelegate;
import com.hfridland.multitimer.R;
import com.hfridland.multitimer.data.database.MultitimerDao;
import com.hfridland.multitimer.data.model.TimerItem;
import com.hfridland.multitimer.ticker.AlarmReceiver;
import com.hfridland.multitimer.ticker.TickService;
import com.hfridland.multitimer.ui.about.AboutActivity;
import com.hfridland.multitimer.ui.alarm.AlarmActivity;
import com.hfridland.multitimer.ui.alarm.AlarmFragment;
import com.hfridland.multitimer.ui.dialogs.TimeEditorDialogFragment;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class TimersFragment extends Fragment implements TimeEditorDialogFragment.NoticeDialogListener {
    private RecyclerView mRecyclerView;
    private TimersAdapter mTimersAdapter;
    private final MultitimerDao mMultitimerDao = AppDelegate.getMultitimerDao();
    private TimersAdapter.OnTimerItemClickListener mOnTimerItemClickListener = new TimersAdapter.OnTimerItemClickListener() {

        @Override
        public void onStartStopClick(int id) {
            Single.fromCallable(() -> mMultitimerDao.changeActive(id))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(timerItem -> {
                    mTimersAdapter.updateData();
                    if (timerItem.isActive()) {
                        Intent intent = new Intent(getContext(), TickService.class);
                        getActivity().startService(intent);
                    } else {
                        mMultitimerDao.getActiveTimerItemsRx()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(timerItems -> {
                                if (timerItems.isEmpty()) {
                                    Intent intentClose = new Intent(getContext(), TickService.class);
                                    getActivity().stopService(intentClose);
                                }
                            });
                    }
                });
        }

        @Override
        public void onEditClick(int id) {
            Bundle data = new Bundle();
            data.putInt("id", id);
            DialogFragment newFragment = new TimeEditorDialogFragment();
            newFragment.setArguments(data);
            newFragment.show(getActivity().getSupportFragmentManager(), "timerEditor");
        }

        @Override
        public void onDeleteClick(int id) {
            Single.fromCallable(() -> mMultitimerDao.deleteTimerItem(id))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(n -> {
                    mTimersAdapter.updateData();
                });
        }
    };

    private Disposable mTimerDisposable;
    private IntentFilter mIntentFilter;

    private PowerManager mPowerManager;
    PowerManager.WakeLock  mWakeLock;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMultitimerDao.getActiveTimerItemsRx()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(timerItems -> {
                if (!timerItems.isEmpty()) {
                    Intent intent = new Intent(getContext(), TickService.class);
                    getActivity().startService(intent);
                }
            });

        mPowerManager = (PowerManager) getActivity().getSystemService(Context.POWER_SERVICE);
        mWakeLock = mPowerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK |
                PowerManager.ACQUIRE_CAUSES_WAKEUP |
                PowerManager.ON_AFTER_RELEASE, "appname::WakeLock");

        mTimerDisposable = Observable.interval(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(seconds -> {
                    mMultitimerDao.getActiveTimerItemsRx()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(timerItems -> {
                            if (!timerItems.isEmpty()) {
                                mTimersAdapter.updateData();
                            }
                        });
                });


    }

    public static TimersFragment newInstance() {
        return new TimersFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fr_timers, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.mn_timers, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mn_newtimer:
                Bundle data = new Bundle();
                data.putInt("id", -1);
                DialogFragment newFragment = new TimeEditorDialogFragment();
                newFragment.setArguments(data);
                newFragment.show(getActivity().getSupportFragmentManager(), "timerEditor");
                return true;
            /*case R.id.mn_debug:
                TimerItem timerItem = mStorage.getTimerItemById(1);
                Intent launchIntent = new Intent(getActivity(), AlarmActivity.class);
                launchIntent.putExtra(AlarmFragment.TIMER_ITEM, timerItem);
                getActivity().startActivity(launchIntent);

                return true;*/
            case R.id.mn_about:
                Intent launchIntent = new Intent(getActivity(), AboutActivity.class);
                getActivity().startActivity(launchIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mRecyclerView = view.findViewById(R.id.recycler);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mTimersAdapter = new TimersAdapter(mOnTimerItemClickListener);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mTimersAdapter);

        if (getActivity() != null) {
            Intent intent = getActivity().getIntent();
            if (intent != null) {
                String action = intent.getAction();
                if (action == null) action = "";
                if (!action.equals(AlarmReceiver.ALARM_ACTION)) return;
                TimerItem timerItem = (TimerItem) intent.getSerializableExtra(AlarmFragment.TIMER_ITEM);
                if (timerItem != null) {
                    intent.setAction("");


                    //acquire will turn on the display
                    mWakeLock.acquire();

                    //release will release the lock from CPU, in case of that, screen will go back to sleep mode in defined time bt device settings
                    mWakeLock.release();

                    Context context = getActivity().getApplicationContext();
                    AlarmManager am =(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

                    Intent wakeIntent = new Intent(getActivity(), AlarmActivity.class);
                    wakeIntent.putExtra("NAME", timerItem.getName());
                    PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(),
                            12345, wakeIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                    am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pendingIntent);

                    mMultitimerDao.getActiveTimerItemsRx()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(timerItems -> {
                            if (timerItems.isEmpty()) {
                                Intent intentClose = new Intent(getContext(), TickService.class);
                                getActivity().stopService(intentClose);
                            }
                        });
                }
            }
        }

    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        mTimersAdapter.updateData();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) { }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTimerDisposable.dispose();
    }
}
