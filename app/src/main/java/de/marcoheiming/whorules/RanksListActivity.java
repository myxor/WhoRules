package de.marcoheiming.whorules;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class RanksListActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            //ab.setDisplayShowHomeEnabled(true);
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_ranks);

        final RanksAdapter mAdapter = new RanksAdapter(MainActivity.rankList, RanksListActivity.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


/*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_people_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Context context = RanksListActivity.this;
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);

                final EditText nameText = new EditText(context);
                nameText.setHint("Name");
                layout.addView(nameText);

                final EditText rankText = new EditText(context);
                rankText.setHint("Rank");
                layout.addView(rankText);

                final EditText norText = new EditText(context);
                norText.setHint("Anzahl Regentschaften");
                norText.setText("0");
                norText.setInputType(InputType.TYPE_CLASS_NUMBER);
                layout.addView(norText);

                new AlertDialog.Builder(RanksListActivity.this)
                        .setTitle("Vasalle hinzufügen")
                        .setMessage("Bitte gebe den Namen und Rank des neuen Vasallen ein:")
                        .setIcon(android.R.drawable.ic_menu_add)
                        .setView(layout)
                        .setPositiveButton("Hinzufügen", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                ma.addVasall(getApplicationContext(), nameText.getText().toString(), rankText.getText().toString());
                                mAdapter.notifyDataSetChanged();

                            }
                        }).show();
            }
        });
        */

        mAdapter.notifyDataSetChanged();
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


}
