package de.marcoheiming.whorules;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class VasallsListActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vasall_list);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        if (ab != null)
        {
            ab.setDisplayHomeAsUpEnabled(true);
            //ab.setDisplayShowHomeEnabled(true);
        }

        final MainActivity ma = new MainActivity();


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_vasalls);

        final VasallsAdapter mAdapter = new VasallsAdapter(MainActivity.vasallList, VasallsListActivity.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_people_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Context context = VasallsListActivity.this;
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);

                final EditText nameText = new EditText(context);
                nameText.setHint("Name");
                layout.addView(nameText);

                final Spinner rankSpinner = new Spinner(context);
                ArrayList<String> spinnerArray = new ArrayList<String>();
                List<Rank> ranks = new RankDBHelper(context).getListOfRanks();
                for (Rank r : ranks)
                {
                    spinnerArray.add(r.name);
                }
                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                        (context, android.R.layout.simple_spinner_item,
                                spinnerArray); //selected item will look like a spinner set from XML
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                        .simple_spinner_dropdown_item);
                rankSpinner.setAdapter(spinnerArrayAdapter);
                rankSpinner.setPadding(0, 20, 0, 20);
                layout.addView(rankSpinner);

                final EditText norText = new EditText(context);
                norText.setHint("Anzahl Regentschaften");
                norText.setText("0");
                norText.setInputType(InputType.TYPE_CLASS_NUMBER);
                layout.addView(norText);

                new AlertDialog.Builder(VasallsListActivity.this)
                        .setTitle("Vasalle hinzufügen")
                        .setMessage("Bitte gebe den Namen, Rang und Anzahl der Regentschaften des neuen Vasallen ein:")
                        .setIcon(android.R.drawable.ic_menu_add)
                        .setView(layout)
                        .setPositiveButton("Hinzufügen", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                ma.addVasall(getApplicationContext(), nameText.getText().toString(), rankSpinner.getSelectedItem().toString());
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
