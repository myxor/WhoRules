package de.marcoheiming.whorules;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class RanksAdapter extends RecyclerView.Adapter<RanksAdapter.RankViewHolder> {

    private List<Rank> ranksList;
    private Context context;

    public class RankViewHolder extends RecyclerView.ViewHolder {
        public TextView name, sort, numberOfVasalls;

        public RankViewHolder(View view) {
            super(view);

            name = (TextView) view.findViewById(R.id.ranks_list_name);
            sort = (TextView) view.findViewById(R.id.ranks_list_sort);
            numberOfVasalls = (TextView) view.findViewById(R.id.ranks_list_number_of_people);

        }
    }


    public RanksAdapter(List<Rank> ranksList) {
        this.ranksList = ranksList;
    }

    public RanksAdapter(List<Rank> ranksList, Context context) {
        this.ranksList = ranksList;
        this.context = context;
    }

    @NonNull
    @Override
    public RankViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ranks_list_row, parent, false);

        return new RankViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final RankViewHolder holder, final int position) {
        final Rank r = ranksList.get(position);
        if (r != null)
        {
            System.out.println("Rank: " + r.name + " : " + r.numberOfVasallsHoldingThisRank);
            holder.name.setText(r.name);
            holder.sort.setText(String.valueOf(r.sort));
            holder.numberOfVasalls.setText(String.valueOf(r.numberOfVasallsHoldingThisRank));
        }
    }

    @Override
    public int getItemCount() {
        return ranksList.size();
    }


}