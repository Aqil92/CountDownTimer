package com.aqil.countdowntimer;

import android.app.Activity;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.TextView;

import com.aqil.timerlibrary.ProgressWheel;

import java.util.logging.Logger;

public class MainActivity extends Activity {

    private ProgressWheel mDaysWheel;
    private ProgressWheel mHoursWheel;
    private ProgressWheel mMinutesWheel;
    private ProgressWheel mSecondsWheel;

    // Timer setup
    Time conferenceTime = new Time(Time.getCurrentTimezone());
    int hour = 22;
    int minute = 33;
    int second = 0;
    int monthDay = 28;
    // month is zero based...7 == August
    int month = 9;
    int year;

    // Values displayed by the timer
    private int mDisplayDays;
    private int mDisplayHours;
    private int mDisplayMinutes;
    private int mDisplaySeconds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        configureConferenceDate();
    }

    void init(){
        this.conferenceTime.setToNow();
        this.year = conferenceTime.year;
        mDaysWheel =  findViewById(R.id.activity_countdown_timer_days);
        mHoursWheel =  findViewById(R.id.activity_countdown_timer_hours);
        mMinutesWheel =  findViewById(R.id.activity_countdown_timer_minutes);
        mSecondsWheel =  findViewById(R.id.activity_countdown_timer_seconds);
    }

    private void configureConferenceDate() {
        conferenceTime.set(second, minute, hour, monthDay, month, year);
        conferenceTime.normalize(true);
        long confMillis = conferenceTime.toMillis(true);

        Time nowTime = new Time(Time.getCurrentTimezone());
        nowTime.setToNow();
        nowTime.normalize(true);
        long nowMillis = nowTime.toMillis(true);

        long milliDiff = confMillis - nowMillis;

        new CountDownTimer(milliDiff, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                // decompose difference into days, hours, minutes and seconds
                mDisplayDays = (int) ((millisUntilFinished / 1000) / 86400);
                mDisplayHours = (int) (((millisUntilFinished / 1000) - (mDisplayDays * 86400)) / 3600);
                mDisplayMinutes = (int) (((millisUntilFinished / 1000) - ((mDisplayDays * 86400) + (mDisplayHours * 3600))) / 60);
                mDisplaySeconds = (int) ((millisUntilFinished / 1000) % 60);

                mDaysWheel.setText(String.valueOf(mDisplayDays));
                mDaysWheel.setProgress(mDisplayDays);

                mHoursWheel.setText(String.valueOf(mDisplayHours));
                mHoursWheel.setProgress(mDisplayHours * 15);

                mMinutesWheel.setText(String.valueOf(mDisplayMinutes));
                mMinutesWheel.setProgress(mDisplayMinutes * 6);

                Animation an = new RotateAnimation(0.0f, 90.0f, 250f, 273f);
                an.setFillAfter(true);

                mSecondsWheel.setText(String.valueOf(mDisplaySeconds));
                mSecondsWheel.setProgress(mDisplaySeconds * 6);
            }

            @Override
            public void onFinish() {
                //TODO: this is where you would launch a subsequent activity if you'd like.  I'm currently just setting the seconds to zero

                mSecondsWheel.setText("0");
                mSecondsWheel.setProgress(0);
            }
        }.start();
    }
}
