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
        public TextView defendant, prosecutors, description, date, count, buttonViewOption;

        public BeerViewHolder(View view) {
            super(view);
            defendant = (TextView) view.findViewById(R.id.beer_list_defendant);
            prosecutors = (TextView) view.findViewById(R.id.beer_list_prosecutors);
            description = (TextView) view.findViewById(R.id.beer_list_description);
            date = (TextView) view.findViewById(R.id.beer_list_date);
            count = (TextView) view.findViewById(R.id.beer_list_count);
            //buttonViewOption = (TextView) view.findViewById(R.id.textViewOptions);
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

        /*
        holder.buttonViewOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(context, holder.buttonViewOption);
                popup.inflate(R.menu.beer_list_row_menu);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_edit:

                                LinearLayout layout = new LinearLayout(context);
                                layout.setOrientation(LinearLayout.VERTICAL);

                                final EditText prosecutorsText = new EditText(context);
                                prosecutorsText.setHint(context.getString(R.string.prosecutors));
                                prosecutorsText.setText(beer.getProsecutors());
                                layout.addView(prosecutorsText);

                                final EditText defendantText = new EditText(context);
                                defendantText.setHint(context.getString(R.string.defendant));
                                defendantText.setText(beer.getDefendant());
                                layout.addView(defendantText);

                                final EditText descriptionText = new EditText(context);
                                descriptionText.setHint(context.getString(R.string.description));
                                descriptionText.setText(beer.getDescription());
                                layout.addView(descriptionText);

                                final EditText countText = new EditText(context);
                                countText.setHint(context.getString(R.string.numberOfBeers));
                                countText.setText("1");
                                countText.setInputType(InputType.TYPE_CLASS_NUMBER);
                                countText.setText(String.valueOf(beer.getCount()));
                                layout.addView(countText);

                                new AlertDialog.Builder(context)
                                        .setTitle(R.string.beer_modify)
                                        .setMessage(R.string.beer_modify_text)
                                        .setIcon(android.R.drawable.ic_menu_add)
                                        .setView(layout)
                                        .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                beer.setProsecutors(prosecutorsText.getText().toString());
                                                beer.setDescription(descriptionText.getText().toString());
                                                beer.setCount(Integer.parseInt(countText.getText().toString()));
                                                beer.setDefendant(defendantText.getText().toString());
                                                // beer.setDefendant_id(); // TODO
                                                // beer.setDate(); // TODO

                                                beer.updateBeerInDB(context, beer);

                                                notifyItemChanged(position);
                                            }
                                        }).show();
                                break;
                            case R.id.menu_delete:

                                if (beer.delete(context, beer.getId())) {
                                    beersList.remove(position);
                                    notifyItemRemoved(position);
                                }

                                break;
                        }
                        return false;
                    }
                });
                //displaying the popup
                popup.show();

                notifyItemChanged(position);
            }
        });
        */

    }

    @Override
    public int getItemCount() {
        return beersList.size();
    }


}