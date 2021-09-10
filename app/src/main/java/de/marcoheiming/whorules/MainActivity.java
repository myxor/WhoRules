package de.marcoheiming.whorules;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.view.View;

import com.google.android.material.navigation.NavigationView;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String NOTIFICATION_CHANNEL_NEW_RULER_ID = "NotiNewRuler";
    public static final String NOTIFICATION_CHANNEL_PROMOTION_ID = "NotiRulerPromoted";

    Ruler currentRuler = null;
    public static ArrayList<Ruler> previousRulers = new ArrayList<Ruler>();
    List<String> previousRulerNames = new ArrayList<>();
    ArrayAdapter<String> previousRulersAdapter;

    public static List<Vasall> vasallList = new ArrayList<>();

    public static List<Rank> rankList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fab.setEnabled(false);

                if (vasallList == null || vasallList.isEmpty() || vasallList.size() == 1) {
                    Toast toast = Toast.makeText(MainActivity.this, "Sie müssen zu erst mindestens zwei Vasallen anlegen!", Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Neuer Regend")
                            .setMessage("Willst du wirklich einen neuen Regent bestimmen?")
                            .setIcon(android.R.drawable.ic_media_next)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    Ruler newRuler = chooseNextRandomRuler();
                                    showCurrentRuler(newRuler);
                                    fab.setEnabled(true);
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    fab.setEnabled(true);
                                }
                            }).show();
                }

                fab.setEnabled(true);
            }
        });


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.notification_channel_new_ruler);
            String description = getString(R.string.notification_channel_new_ruler_desc);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_PROMOTION_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

            // TODO: second channel for new ruler

        }

        // Init DB tables and arrays:
        initRankList();

        initVasallList();

        initRulerList();


        // Update main menu:
        Menu menu = navigationView.getMenu();
        MenuItem navVasalls = menu.findItem(R.id.nav_people);
        if (navVasalls != null) {
            navVasalls.setTitle(R.string.pref_header_people + " (" + MainActivity.vasallList.size() + ")");
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        FloatingActionButton fab = findViewById(R.id.fab);
        if (vasallList.isEmpty() || vasallList.size() == 1) {
            fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
        } else {
            fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
        }

        // Update main menu:
        NavigationView navigationView = findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();
        MenuItem navVasalls = menu.findItem(R.id.nav_people);
        if (navVasalls != null) {
            navVasalls.setTitle(getString(R.string.pref_header_people) + " (" + MainActivity.vasallList.size() + ")");
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent = null;

        if (id == R.id.nav_random) {
            intent = new Intent(this, MainActivity.class);
        } else if (id == R.id.nav_people) {
            intent = new Intent(this, VasallsListActivity.class);
        } else if (id == R.id.nav_timer) {
            intent = new Intent(this, TimerActivity.class);
        } else if (id == R.id.nav_beers) {
            intent = new Intent(this, BeersListActivity.class);
        } else if (id == R.id.nav_ranks) {
            intent = new Intent(this, RanksListActivity.class);
        }

        if (intent != null) {
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public Ruler chooseNextRandomRuler() {

        if (vasallList.isEmpty()) {
            return null;
        }

        // copy list of vasalls except the current ruler
        List<Vasall> localVasallList = new ArrayList<>();
        for (Vasall v : vasallList) {
            if (currentRuler == null || !v.getName().equals(currentRuler.name)) {
                localVasallList.add(v);
            }
        }
        // Here the random happens
        int rnd = new Random().nextInt(localVasallList.size());

        Vasall vasall = localVasallList.get(rnd);
        vasall.increaseNumberOfReigns();
        addNewRulerToList(vasall);
        return rulerChosen(vasall);
    }

    private Ruler rulerChosen(Vasall v) {
        currentRuler = new Ruler();

        currentRuler.name = v.getName();
        currentRuler.rank = v.getRankId();
        currentRuler.startDate = Calendar.getInstance().getTime();
        currentRuler.reignNumber = v.getNumberOfReigns();

        return currentRuler;
    }

    private void showCurrentRuler(Ruler ruler) {
        TextView tv = findViewById(R.id.current_ruler_text_field);
        tv.setText(String.format("%s %s", ruler.rank, ruler.name));

        tv = findViewById(R.id.current_ruler_date_text_field);
        String sinceDate = String.format("%s %s", "seit",
                DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(ruler.startDate));
        tv.setText(sinceDate);


        tv = findViewById(R.id.current_ruler_date_text_field2);
        String remaining;
        // 2 hours, TODO: make configurable
        long diff = ((ruler.startDate.getTime() + 2 * 60 * 60 * 1000) - new Date().getTime() / 1000);
        if (diff > 0) // remaining seconds
        {
            if (diff > 60 * (60 + 55)) // almost 2 hours
            {
                remaining = "zwei Stunden";
            } else {
                if (diff > 60 * (60 + 45)) // almost 2 hours
                {
                    remaining = "knapp zwei Stunden";
                } else {
                    if (diff > 60 * 60) // more than one hour
                    {
                        remaining = "mehr als eine Stunde";
                    } else {
                        if (diff > 60) {
                            diff = diff / 60;
                            remaining = String.format("~%s Minuten", diff);
                        } else {
                            remaining = String.format("~%s Sekunden", diff);
                        }
                    }
                }
            }
            tv.setText(String.format("Noch %s", remaining));
        } else {
            tv.setText("");
        }

        ProgressBar progressBar = findViewById(R.id.current_ruler_promotion_progressBar);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setProgress(ruler.reignNumber);
    }

    private void addNewRulerToList(Vasall v) {
        Ruler newRuler = new Ruler();
        newRuler.name = v.getName();
        newRuler.rank = v.getRankId();
        newRuler.startDate = Calendar.getInstance().getTime();

        previousRulers.add(newRuler);

        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
        previousRulerNames.add(0, (newRuler.rank != null ? newRuler.rank : "") + " " + newRuler.name + " (" + df.format(newRuler.startDate) + ")");

        if (previousRulersAdapter == null) {
            // Create an ArrayAdapter from List
            previousRulersAdapter = new ArrayAdapter<>
                    (this, android.R.layout.simple_list_item_activated_1, previousRulerNames);

            // DataBind ListView with items from ArrayAdapter
            final ListView lv = findViewById(R.id.ruler_list);
            lv.setAdapter(previousRulersAdapter);
            lv.setVisibility(View.VISIBLE);
        }

        findViewById(R.id.textView5).setVisibility(View.VISIBLE);
        previousRulersAdapter.notifyDataSetChanged();

        // Save to DB:
        RulerDBHelper dbHelper = new RulerDBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(RulerContract.RulerEntry.COLUMN_NAME_VASALL_ID, v.getId());
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        values.put(RulerContract.RulerEntry.COLUMN_NAME_SINCE, sdf.format(newRuler.startDate));
        values.put(RulerContract.RulerEntry.COLUMN_NAME_RANK, newRuler.rank);

        db.insert(RulerContract.RulerEntry.TABLE_NAME, null, values);
    }

    protected void initRankList() {
        MainActivity.rankList.clear();
        MainActivity.rankList.add(new Rank(MainActivity.this, 1, "Freiherr"));
        MainActivity.rankList.add(new Rank(MainActivity.this, 2, "Graf"));
        MainActivity.rankList.add(new Rank(MainActivity.this, 3, "Fürst"));
        MainActivity.rankList.add(new Rank(MainActivity.this, 4, "Herzog"));
        MainActivity.rankList.add(new Rank(MainActivity.this, 5, "König"));
        MainActivity.rankList.add(new Rank(MainActivity.this, 6, "Kaiser"));
        MainActivity.rankList.add(new Rank(MainActivity.this, 7, "Imperator"));
        MainActivity.rankList.add(new Rank(MainActivity.this, 8, "Pharao"));
        MainActivity.rankList.add(new Rank(MainActivity.this, 9, "Halbgott"));
        MainActivity.rankList.add(new Rank(MainActivity.this, 10, "Gott"));
    }

    protected void initVasallList() {
        MainActivity.vasallList.clear();

        VasallsDBHelper db = new VasallsDBHelper(MainActivity.this);
        List<Vasall> list = db.getListOfVasalls();

        for (Vasall v : list) {
            addVasall(MainActivity.this, v.getName(), v.getRankId());
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        if (list.isEmpty() || list.size() == 1) {
            fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
        } else {
            fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
        }
    }

    protected void addVasall(Context context, String name, int rank) {
        Vasall newVasall = new Vasall(context, name, rank);
        MainActivity.vasallList.add(newVasall);
    }

    protected void addVasall(Context context, String name, String rank) {
        Vasall newVasall = new Vasall(context, name, rank);
        MainActivity.vasallList.add(newVasall);
    }

    protected void initRulerList() {
        previousRulers.clear();
        previousRulerNames.clear();

        RulerDBHelper dbHelper = new RulerDBHelper(this);
        List<Ruler> list = dbHelper.getListOfRulers();
        if (!list.isEmpty()) {

            findViewById(R.id.textView5).setVisibility(View.VISIBLE);

            final ListView lv = findViewById(R.id.ruler_list);
            lv.setVisibility(View.VISIBLE);

            DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);

            // Create an ArrayAdapter from List
            previousRulersAdapter = new ArrayAdapter<String>
                    (this, android.R.layout.simple_list_item_activated_1, previousRulerNames);

            // DataBind ListView with items from ArrayAdapter
            lv.setAdapter(previousRulersAdapter);

            for (Ruler r : list) {
                previousRulerNames.add(0, (r.rank != null ? r.rank : "") + " " + r.name + " (" + df.format(r.startDate) + ")");

                previousRulers.add(r);

                // Find current ruler: 2 hours, TODO: make configurable
                if (r.startDate.getTime() >= new Date().getTime() - 2 * 60 * 60 * 1000) {
                    showCurrentRuler(r);
                }
            }

            previousRulersAdapter.notifyDataSetChanged();
        }
    }
}
