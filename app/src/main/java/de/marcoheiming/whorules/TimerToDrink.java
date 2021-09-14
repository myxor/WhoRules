package de.marcoheiming.whorules;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.view.WindowManager;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

class Timer {

    private Context context;

    long _id;
    int secondsRemaining;
    String remainingText;
    Vasall vasall;

    CountDownTimer cTimer = null;

    MediaPlayer mediaPlayer;
    TimerAdapter mAdapter;

    public Timer() {
    }

    public Timer(Context context) {

        this.context = context;

    }

    public Timer(Context context, TimerAdapter mAdapter, int seconds) {
        this.context = context;
        this.secondsRemaining = seconds;
        this.mAdapter = mAdapter;

        createCountdown();
    }

    public Timer(Context context, TimerAdapter mAdapter, Vasall vasall, int seconds) {

        this.context = context;
        this.vasall = vasall;
        this.secondsRemaining = seconds;
        this.mAdapter = mAdapter;

        createCountdown();

    }

    private void createCountdown()
    {
        cTimer = new CountDownTimer(this.secondsRemaining * 1000L, 100) {

            public void onTick(long millisUntilFinished) {
                long minutesUntilFinished = 0;
                long secondsUntilFinished = Math.round(millisUntilFinished / 1000);
                if (secondsUntilFinished > 60) {
                    minutesUntilFinished = secondsUntilFinished / 60;
                    secondsUntilFinished = secondsUntilFinished % 60;
                }

                /*
                TODO countdown
                if (millisUntilFinished % 1000 == 0 &&
                        secondsUntilFinished <= 10 && secondsUntilFinished > 0) {

                    // FIXME: not working?
                    //mediaPlayer.start();
                }
                */

                long deciSeconds = Math.round((millisUntilFinished % 1000) / 100);
                if (minutesUntilFinished > 0) {
                    remainingText = ((minutesUntilFinished > 9 ? minutesUntilFinished : "0" + minutesUntilFinished) + ":" +
                            (secondsUntilFinished > 9 ? secondsUntilFinished : "0" + secondsUntilFinished));
                } else {
                    remainingText = (secondsUntilFinished + "." + deciSeconds);
                }

                mAdapter.notifyDataSetChanged();
            }

            public void onFinish() {
                remainingText = context.getString(R.string.timer_drink);

                mediaPlayer = MediaPlayer.create(context, R.raw.alarm_clock);
                mediaPlayer.start();


                new java.util.Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (mediaPlayer != null) {
                            mediaPlayer.release();
                            mediaPlayer = null;
                        }
                    }
                }, 10000);

                //getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

                mAdapter.notifyDataSetChanged();

            }
        };
        cTimer.start();
    }

    void cancelTimer() {
        if (cTimer != null) {
            cTimer.cancel();
            remainingText = "";

            if (mediaPlayer != null) {
                mediaPlayer.release();
                mediaPlayer = null;
            }
            //getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }
}
