package com.example.evaluation_system.widget;

import android.os.CountDownTimer;


public abstract class MyCountDownTimer extends CountDownTimer {

    public MyCountDownTimer(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    public abstract long getlMapper();
}
