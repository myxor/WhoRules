package de.marcoheiming.whorules;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

public class TimerActivity extends AppCompatActivity {

    CountDownTimer cTimer = null;
    FloatingActionButton startBtn;

    MediaPlayer mediaPlayer;

    TimerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            //ab.setDisplayShowHomeEnabled(true);
        }

        RecyclerView recyclerView = findViewById(R.id.rv_timer_list);

        if (mAdapter == null) {
            mAdapter = new TimerAdapter(MainActivity.activeTimerToDrinks, TimerActivity.this);
        }

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        startBtn = findViewById(R.id.fab_timer_add);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = TimerActivity.this;
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);

                final EditText secondsText = new EditText(context);
                secondsText.setHint(R.string.seconds);
                secondsText.setText("90");
                secondsText.setInputType(InputType.TYPE_CLASS_NUMBER);
                layout.addView(secondsText);

                new AlertDialog.Builder(TimerActivity.this)
                        .setTitle(R.string.timerToDrink)
                        .setMessage(R.string.timer_add_text)
                        .setIcon(android.R.drawable.ic_menu_add)
                        .setView(layout)
                        .setPositiveButton(R.string.start, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                startTimer(Integer.parseInt(secondsText.getText().toString()));
                            }
                        }).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        List<TimerToDrink> timerList = mAdapter.getTimerToDrinkList();
        for (int i = 0; i < timerList.size(); i++) {
            TimerToDrink timer = timerList.get(i);
        }
        if (timerList.size() > 0) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void startTimer(int seconds) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        TimerToDrink timerToDrink = new TimerToDrink(TimerActivity.this, mAdapter, seconds);
        MainActivity.activeTimerToDrinks.add(timerToDrink);
        mediaPlayer = MediaPlayer.create(TimerActivity.this, R.raw.beep_short);
        mAdapter.notifyDataSetChanged();
    }


}
