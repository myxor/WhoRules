package de.marcoheiming.whorules;

import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;

import java.util.List;

public class RanksListActivity extends AppCompatActivity {

    List<Rank> ranks;
    RanksAdapter mAdapter = null;

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

        RankDBHelper rankDBHelper = new RankDBHelper(this);

        ranks = rankDBHelper.getListOfRanks();
        for (int i = 0; i < ranks.size(); i++) {
            Rank r = ranks.get(i);
            if (r._id > 0) {
                r = rankDBHelper.updateNumberOfVasallsHoldingTheRankForRank((int)r._id);
                ranks.set(i, r);
            }
        }

        mAdapter = new RanksAdapter(ranks, RanksListActivity.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume(){
        super.onResume();

        RankDBHelper rankDBHelper = new RankDBHelper(this);

        ranks = rankDBHelper.getListOfRanks();

        for (int i = 0; i < ranks.size(); i++) {
            Rank r = ranks.get(i);
            if (r._id > 0) {
                r = rankDBHelper.updateNumberOfVasallsHoldingTheRankForRank((int)r._id);
                ranks.set(i, r);
            }
        }
        mAdapter = new RanksAdapter(ranks, RanksListActivity.this);
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
