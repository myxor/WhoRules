package de.marcoheiming.whorules;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_beers);

        BeerDBHelper beerDBHelper = new BeerDBHelper(getApplicationContext());

        List<Beer> beersList = beerDBHelper.getListOfBeers();

        final BeersAdapter mAdapter = new BeersAdapter(beersList, BeersListActivity.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


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
                        .setTitle("Strafbier hinzufügen")
                        .setMessage("Bitte gebe den Ankläger, Angeklagten, Grund und Anzahl der Strafbiere ein:")
                        .setIcon(android.R.drawable.ic_menu_add)
                        .setView(layout)
                        .setPositiveButton("Hinzufügen", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                new Beer(getApplicationContext(), defendantText.getText().toString(), prosecutorsText.getText().toString(),
                                        descriptionText.getText().toString(), Integer.parseInt(countText.getText().toString()));
                                mAdapter.notifyDataSetChanged();
                            }
                        }).show();
            }
        });
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

}
