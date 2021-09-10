package de.marcoheiming.whorules;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

public class BeersAdapter extends RecyclerView.Adapter<BeersAdapter.BeerViewHolder> {

    private List<Beer> beersList;
    private Context context;

    public class BeerViewHolder extends RecyclerView.ViewHolder {
        public TextView defendant, prosecutors, description, date, count;

        public BeerViewHolder(View view) {
            super(view);
            defendant = (TextView) view.findViewById(R.id.beer_list_defendant);
            prosecutors = (TextView) view.findViewById(R.id.beer_list_prosecutors);
            description = (TextView) view.findViewById(R.id.beer_list_description);
            date = (TextView) view.findViewById(R.id.beer_list_date);
            count = (TextView) view.findViewById(R.id.beer_list_count);
        }
    }


    public BeersAdapter(List<Beer> beersList) {
        this.beersList = beersList;
    }

    public BeersAdapter(List<Beer> beersList, Context context) {
        this.beersList = beersList;
        this.context = context;
    }

    @NonNull
    @Override
    public BeerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.beer_list_row, parent, false);

        return new BeerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final BeerViewHolder holder, int pos) {
        int position = holder.getAdapterPosition();
        final Beer beer = beersList.get(position);
        holder.defendant.setText(beer.getDefendant());
        holder.prosecutors.setText(beer.getProsecutors());
        holder.description.setText(beer.getDescription());
        String date = "-";
        if (beer.getDate() != null) {
            date = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(beer.getDate());
        }
        holder.date.setText(date);
        holder.count.setText(String.format("%s: %s", this.context.getString(R.string.count), String.valueOf(beer.getCount())));

    }

    @Override
    public int getItemCount() {
        return beersList.size();
    }


}