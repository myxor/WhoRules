package de.marcoheiming.whorules;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class VasallsAdapter extends RecyclerView.Adapter<VasallsAdapter.VasallViewHolder> {

    private List<Vasall> vasallsList;
    private Context context;

    public class VasallViewHolder extends RecyclerView.ViewHolder {
        public TextView name, numberOfReigns, rank, buttonViewOption;
        public ProgressBar progressBar;

        public VasallViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.vasall_list_name);
            numberOfReigns = (TextView) view.findViewById(R.id.vasall_list_number_of_reigns);
            rank = (TextView) view.findViewById(R.id.vasall_list_rank);
            buttonViewOption = (TextView) itemView.findViewById(R.id.textViewOptions);
            progressBar = (ProgressBar) itemView.findViewById(R.id.vasall_list_progressBar);
        }
    }


    public VasallsAdapter(List<Vasall> vasallsList) {
        this.vasallsList = vasallsList;
    }

    public VasallsAdapter(List<Vasall> vasallsList, Context context) {
        this.vasallsList = vasallsList;
        this.context = context;
    }

    @NonNull
    @Override
    public VasallViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vasall_list_row, parent, false);

        return new VasallViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final VasallViewHolder holder, final int position) {
        final Vasall vasall = vasallsList.get(position);
        holder.name.setText(vasall.getName());
        holder.rank.setText(vasall.getRank());

        holder.numberOfReigns.setText(String.format("%s / 5", String.valueOf(vasall.getNumberOfReigns())));
        holder.progressBar.setProgress(vasall.getNumberOfReigns());


        holder.buttonViewOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(context, holder.buttonViewOption);
                popup.inflate(R.menu.vasall_list_row_menu);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_edit:

                                LinearLayout layout = new LinearLayout(context);
                                layout.setOrientation(LinearLayout.VERTICAL);

                                final EditText nameText = new EditText(context);
                                nameText.setHint("Name");
                                nameText.setText(vasall.getName());
                                layout.addView(nameText);

                                final Spinner rankSpinner = new Spinner(context);
                                int selectionPosition = 0; int i = 0;
                                ArrayList<String> spinnerArray = new ArrayList<String>();
                                List<Rank> ranks = new RankDBHelper(context).getListOfRanks();
                                for (Rank r : ranks)
                                {
                                    spinnerArray.add(r.name);
                                    if (r.name.equals(vasall.getRank()))
                                    {
                                        selectionPosition = i;
                                    }
                                    i++;
                                }
                                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                                        (context, android.R.layout.simple_spinner_item,
                                                spinnerArray); //selected item will look like a spinner set from XML
                                spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                                        .simple_spinner_dropdown_item);
                                rankSpinner.setAdapter(spinnerArrayAdapter);
                                rankSpinner.setSelection(selectionPosition);
                                layout.addView(rankSpinner);

                                final EditText norText = new EditText(context);
                                norText.setHint("Anzahl Regentschaften");
                                norText.setText(String.valueOf(vasall.getNumberOfReigns()));
                                norText.setInputType(InputType.TYPE_CLASS_NUMBER);
                                layout.addView(norText);

                                new AlertDialog.Builder(context)
                                        .setTitle("Vasalle bearbeiten")
                                        .setMessage("Bitte gebe den Namen, Rang und Anzahl Regentschaften des Vasallen " + vasall.getName() + " ein:")
                                        .setIcon(android.R.drawable.ic_menu_add)
                                        .setView(layout)
                                        .setPositiveButton("Speichern", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                vasall.setName(nameText.getText().toString());
                                                vasall.setRank(rankSpinner.getSelectedItem().toString());
                                                vasall.setNumberOfReigns(Integer.parseInt(norText.getText().toString()));

                                                notifyDataSetChanged();
                                            }
                                        }).show();
                                break;
                            case R.id.menu_delete:

                                if (vasall.delete()) {

                                    vasallsList.remove(position);
                                    notifyItemRemoved(position);
                                }

                                break;
                        }
                        return false;
                    }
                });
                //displaying the popup
                popup.show();

                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return vasallsList.size();
    }


}