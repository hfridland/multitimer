package com.hfridland.multitimer.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.hfridland.multitimer.AppDelegate;
import com.hfridland.multitimer.R;
import com.hfridland.multitimer.data.Storage;
import com.hfridland.multitimer.data.model.TimerItem;
import com.hfridland.multitimer.ui.timers.TimersActivity;

public class TimeEditorDialogFragment extends DialogFragment {
    private TimerItem mTimerItem;
    private EditText mEtName;
    private EditText mEtHour;
    private EditText mEtMin;
    private EditText mEtSec;
    private NoticeDialogListener mListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        int id = getArguments().getInt("id");
        final Storage storage = AppDelegate.getStorage();
        if (id >= 0) {
            mTimerItem = storage.getTimerItemById(id);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dlg_timereditor, null);
        setupView(v);
        builder.setView(v)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mTimerItem == null) {
                            mTimerItem = new TimerItem();
                        }

                        mTimerItem.setName(mEtName.getText().toString());
                        mTimerItem.setDurationSec(getDurationSec());

                        storage.saveTimerItem(mTimerItem);
                        mListener.onDialogPositiveClick(TimeEditorDialogFragment.this);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { }
                });

        return builder.create();
    }

    private int getDurationSec() {
        int h = Integer.parseInt(mEtHour.getText().toString());
        int m = Integer.parseInt(mEtMin.getText().toString());
        int s = Integer.parseInt(mEtSec.getText().toString());
        return h * 3600 + m * 60 + s;
    }

    private View.OnKeyListener mDigitOnKeyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (event.getAction() != KeyEvent.ACTION_UP)
                return false;

            EditText editText = (EditText) v;

            char ch = event.getKeyCharacterMap().getDisplayLabel(keyCode);
            if (!Character.isDigit(ch)) return true;

            String sBuf = editText.getText().toString();
            sBuf += ch;
            sBuf = sBuf.substring(1);
            editText.setText(sBuf);
            editText.setSelection(editText.getText().length());
            return true;
        }
    };
    private View.OnFocusChangeListener mDigitOnFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                EditText editText = (EditText) v;
                if (Integer.parseInt(editText.getText().toString()) > 59) {
                    editText.setText("59");
                }
            }
        }
    };

    private void setupView(View v) {
        mEtName = v.findViewById(R.id.et_name);
        mEtHour = v.findViewById(R.id.et_hour);
        mEtHour.setOnKeyListener(mDigitOnKeyListener);
        mEtHour.setOnFocusChangeListener(mDigitOnFocusChangeListener);
        mEtMin  = v.findViewById(R.id.et_min);
        mEtMin.setOnKeyListener(mDigitOnKeyListener);
        mEtMin.setOnFocusChangeListener(mDigitOnFocusChangeListener);
        mEtSec  = v.findViewById(R.id.et_sec);
        mEtSec.setOnKeyListener(mDigitOnKeyListener);
        mEtSec.setOnFocusChangeListener(mDigitOnFocusChangeListener);

        if (mTimerItem == null) return;

        mEtName.setText(mTimerItem.getName());

        int duration = mTimerItem.getDurationSec();
        int h = duration / 3600;
        duration %= 3600;
        int m = duration / 60;
        int s = duration % 60;

        mEtHour.setText(String.format("%02d", h));
        mEtMin.setText(String.format("%02d", m));
        mEtSec.setText(String.format("%02d", s));

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        for(Fragment fragment : ((TimersActivity) context).getSupportFragmentManager().getFragments()) {
            if (fragment instanceof NoticeDialogListener) {
                mListener = (NoticeDialogListener)fragment;
                break;
            }
        }
    }

    public interface NoticeDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

}
