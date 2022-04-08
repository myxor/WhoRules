package de.marcoheiming.whorules;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class BeersListActivity extends AppCompatActivity {

    public static String BEER_API_URL = ""; // has to be configured

    Context context = BeersListActivity.this;

    public void loadSharedPrefs() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        BEER_API_URL = sharedPreferences.getString("BEER_API_URL", "");
    }

    public void storeSharedPrefs(String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beers_list);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            //ab.setDisplayShowHomeEnabled(true);
        }



        if (BEER_API_URL.equals("")) {
            loadSharedPrefs();
        }

        if (BEER_API_URL == null || BEER_API_URL.equals("")) {
            LinearLayout layout = new LinearLayout(context);
            layout.setOrientation(LinearLayout.VERTICAL);

            final EditText urlText = new EditText(context);
            urlText.setHint(getString(R.string.prosecutors));
            layout.addView(urlText);

            new AlertDialog.Builder(BeersListActivity.this)
                    .setTitle("Beer API URL")
                    .setMessage("Beer API URL eingeben")
                    .setIcon(android.R.drawable.ic_menu_add)
                    .setView(layout)
                    .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            BEER_API_URL = urlText.getText().toString();

                            storeSharedPrefs("BEER_API_URL", BEER_API_URL);

                            loadBeerList();
                        }
                    }).show();
        }

        loadBeerList();

        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_beers_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Context context = BeersListActivity.this;
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);

                final EditText prosecutorsText = new EditText(context);
                prosecutorsText.setHint(getString(R.string.prosecutors));
                layout.addView(prosecutorsText);

                final EditText defendantText = new EditText(context);
                defendantText.setHint(getString(R.string.defendant));
                layout.addView(defendantText);

                final EditText descriptionText = new EditText(context);
                descriptionText.setHint(getString(R.string.description));
                layout.addView(descriptionText);

                final EditText countText = new EditText(context);
                countText.setHint(getString(R.string.numberOfBeers));
                countText.setText("1");
                countText.setInputType(InputType.TYPE_CLASS_NUMBER);
                layout.addView(countText);

                new AlertDialog.Builder(BeersListActivity.this)
                        .setTitle(R.string.add_beer)
                        .setMessage(R.string.add_beer_text)
                        .setIcon(android.R.drawable.ic_menu_add)
                        .setView(layout)
                        .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Beer b = new Beer(getApplicationContext(), defendantText.getText().toString(), prosecutorsText.getText().toString(),
                                        descriptionText.getText().toString(), Integer.parseInt(countText.getText().toString()));
                                b.saveToDB();
                                beersList.add(b);
                                mAdapter.notifyItemInserted(beersList.size());
                            }
                        }).show();
            }
        });
        mAdapter.notifyDataSetChanged();
        */
    }

    private void loadBeerList() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_beers);

        BeerDBHelper beerDBHelper = new BeerDBHelper(getApplicationContext());

        List<Beer> beersList = beerDBHelper.getListOfBeers();

        final BeersAdapter mAdapter = new BeersAdapter(beersList, BeersListActivity.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
