package de.marcoheiming.whorules;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
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

import java.util.List;

public class TimerAdapter extends RecyclerView.Adapter<TimerAdapter.TimerViewHolder> {

    private List<DrinkTimer> timerList;
    private Context context;

    public class TimerViewHolder extends RecyclerView.ViewHolder {
        public TextView remainingText, vasall;
        public ProgressBar progressBar;

        public TimerViewHolder(View view) {
            super(view);
            remainingText = (TextView) view.findViewById(R.id.timer_remaining_time);
            //vasallList = (TextView) view.findViewById(R.id.timer_vasall_list);
            //progressBar = (ProgressBar) itemView.findViewById(R.id.timer_progress);
        }
    }


    public TimerAdapter(List<DrinkTimer> timerList) {
        this.timerList = timerList;
    }

    public TimerAdapter(List<DrinkTimer> timerList, Context context) {
        this.timerList = timerList;
        this.context = context;
    }

    @NonNull
    @Override
    public TimerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.timer_row, parent, false);

        return new TimerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final TimerViewHolder holder, final int position) {
        final DrinkTimer timer = timerList.get(position);
        holder.remainingText.setText(timer.remainingText);
        //holder.vasallList.setText(""); // TODO

        //holder.progressBar.setProgress(timer); // TODO


      /*
           DrinkTimer timer = timerList.get(position);
                                timer.cancelTimer();

                                timerList.remove(position);
                                notifyItemRemoved(position);
       */
    }


    @Override
    public int getItemCount() {
        if (timerList == null)
        {
            return 0;
        }
        return timerList.size();
    }


}