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
import androidx.recyclerview.widget.ItemTouchHelper;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class TimerActivity extends AppCompatActivity {

    CountDownTimer cTimer = null;
    Button startBtn;

    ArrayList<DrinkTimer> activeTimers = new ArrayList<>();

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

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_timer_list);

        mAdapter = new TimerAdapter(activeTimers, TimerActivity.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        SwipeController swipeController = new SwipeController();
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(recyclerView);


        startBtn = findViewById(R.id.timer_start_btn);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = TimerActivity.this;
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);

                final EditText minutesText = new EditText(context);
                minutesText.setHint("Minuten");
                minutesText.setText("10");
                minutesText.setInputType(InputType.TYPE_CLASS_NUMBER);
                layout.addView(minutesText);

                new AlertDialog.Builder(TimerActivity.this)
                        .setTitle("Timer eingeben")
                        .setMessage("Bitte gebe die Dauer in Minuten ein:")
                        .setIcon(android.R.drawable.ic_menu_add)
                        .setView(layout)
                        .setPositiveButton("Go", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                startTimer(Integer.parseInt(minutesText.getText().toString()) * 60);

                            }
                        }).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void startTimer(int seconds) {

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        DrinkTimer timer = new DrinkTimer(TimerActivity.this, mAdapter, seconds);
        activeTimers.add(timer);

        mediaPlayer = MediaPlayer.create(TimerActivity.this, R.raw.beep_short);

        mAdapter.notifyDataSetChanged();

    }


}
