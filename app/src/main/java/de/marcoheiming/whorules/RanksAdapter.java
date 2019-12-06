package de.marcoheiming.whorules;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class RanksAdapter extends RecyclerView.Adapter<RanksAdapter.RankViewHolder> {

    private List<Rank> ranksList;
    private Context context;

    public class RankViewHolder extends RecyclerView.ViewHolder {
        public TextView name, sort;

        public RankViewHolder(View view) {
            super(view);

            name = (TextView) view.findViewById(R.id.ranks_list_name);
            sort = (TextView) view.findViewById(R.id.ranks_list_sort);

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
            holder.name.setText(r.name);
            holder.sort.setText(String.valueOf(r.sort));
        }
    }

    @Override
    public int getItemCount() {
        return ranksList.size();
    }


}