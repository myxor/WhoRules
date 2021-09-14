package de.marcoheiming.whorules;

import android.app.AlertDialog;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.ArrayList;
import java.util.List;

public class TimerAdapter extends RecyclerView.Adapter<TimerAdapter.TimerViewHolder> {

    private List<TimerToDrink> timerToDrinkList;
    private Context context;
    private TimerViewHolder holder;

    public class TimerViewHolder extends RecyclerView.ViewHolder {
        public TextView remainingText, vasall, buttonViewOption;
        public ProgressBar progressBar;

        public TimerViewHolder(View view) {
            super(view);
            remainingText = (TextView) view.findViewById(R.id.timer_remaining_time);
            //vasallList = (TextView) view.findViewById(R.id.timer_vasall_list);
            //progressBar = (ProgressBar) itemView.findViewById(R.id.timer_progress);

            buttonViewOption = (TextView) itemView.findViewById(R.id.textViewOptions);
            holder = this;
        }
    }


    public TimerAdapter(List<TimerToDrink> timerToDrinkList) {
        this.timerToDrinkList = timerToDrinkList;
    }

    public TimerAdapter(List<TimerToDrink> timerToDrinkList, Context context) {
        this.timerToDrinkList = timerToDrinkList;
        this.context = context;
    }

    public List<TimerToDrink> getTimerToDrinkList() {
        return timerToDrinkList;
    }

    @NonNull
    @Override
    public TimerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.timer_row, parent, false);

        return new TimerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final TimerViewHolder holder, final int pos) {
        int position = holder.getAdapterPosition();
        final TimerToDrink timerToDrink = timerToDrinkList.get(position);
        holder.remainingText.setText(timerToDrink.remainingText);
        //holder.vasallList.setText(""); // TODO
        //holder.progressBar.setProgress(timer); // TODO

        holder.buttonViewOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(context, holder.buttonViewOption);
                popup.inflate(R.menu.timer_list_row_menu);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.menu_delete) {
                            timerToDrink.cancelTimer();
                            getTimerToDrinkList().remove(position);
                            notifyItemRemoved(position);
                        }
                        return false;
                    }
                });
                //displaying the popup
                popup.show();
                notifyItemChanged(position);
            }
        });
    }


    @Override
    public int getItemCount() {
        if (timerToDrinkList == null)
        {
            return 0;
        }
        return timerToDrinkList.size();
    }


    public TimerViewHolder getHolder() {
        return holder;
    }


}