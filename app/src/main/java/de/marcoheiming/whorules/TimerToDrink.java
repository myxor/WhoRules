package de.marcoheiming.whorules;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.view.WindowManager;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

class TimerToDrink {

    private Context context;

    long _id;
    int secondsRemaining;
    String remainingText;
    Vasall vasall;

    CountDownTimer cTimer = null;

    MediaPlayer mediaPlayer;
    TimerAdapter mAdapter;

    public TimerToDrink() {
    }

    public TimerToDrink(Context context) {

        this.context = context;

    }

    public TimerToDrink(Context context, TimerAdapter mAdapter, int seconds) {
        this.context = context;
        this.secondsRemaining = seconds;
        this.mAdapter = mAdapter;

        createCountdown();
    }

    public TimerToDrink(Context context, TimerAdapter mAdapter, Vasall vasall, int seconds) {

        this.context = context;
        this.vasall = vasall;
        this.secondsRemaining = seconds;
        this.mAdapter = mAdapter;

        createCountdown();

    }

    public void createCountdown()
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

                    // Last seconds:
                    if (secondsUntilFinished < 10) {
                        mAdapter.getHolder().remainingText.setTextColor(context.getResources().getColor(R.color.red));
                    }
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
        }
    }

    public long get_id() {
        return _id;
    }

    public int getSecondsRemaining() {
        return secondsRemaining;
    }

    public void setSecondsRemaining(int secondsRemaining) {
        this.secondsRemaining = secondsRemaining;
    }

    public String getRemainingText() {
        return remainingText;
    }

    public void setRemainingText(String remainingText) {
        this.remainingText = remainingText;
    }

    public Vasall getVasall() {
        return vasall;
    }

    public void setVasall(Vasall vasall) {
        this.vasall = vasall;
    }

    public CountDownTimer getcTimer() {
        return cTimer;
    }

    public void setcTimer(CountDownTimer cTimer) {
        this.cTimer = cTimer;
    }
}
