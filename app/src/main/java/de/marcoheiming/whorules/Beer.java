package de.marcoheiming.whorules;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Beer {

    private Context context;

    private long _id;
    private Date date;
    private String defendant;
    private int defendant_id;
    private String prosecutors;
    private String description;
    private int count;

    private BeerDBHelper dbHelper;


    public Beer() {
    }

    public Beer(Context context) {

        this.context = context;
        this.dbHelper = new BeerDBHelper(this.context);
    }

    public Beer(Context context, String defendant, String prosecutors, String description, int count) {

        this.context = context;
        this.defendant = defendant;
        this.prosecutors = prosecutors;
        this.description = description;
        this.count = count;

        this.dbHelper = new BeerDBHelper(this.context);

        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(Calendar.getInstance().getTime());

        createBeer(date, defendant, 0, prosecutors, description, count); // TODO defendant_id
    }


    private void createBeer(String date, String defendant, int defendant_id, String prosecutors, String description, int count) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(BeerContract.BeerEntry.COLUMN_NAME_DATE, date);
        values.put(BeerContract.BeerEntry.COLUMN_NAME_DEFENDANT, defendant);
        values.put(BeerContract.BeerEntry.COLUMN_NAME_DEFENDANT_ID, defendant_id);
        values.put(BeerContract.BeerEntry.COLUMN_NAME_DESCRIPTION, description);
        values.put(BeerContract.BeerEntry.COLUMN_NAME_PROSECUTORS, prosecutors);
        values.put(BeerContract.BeerEntry.COLUMN_NAME_COUNT, count);

        this._id = db.insert(BeerContract.BeerEntry.TABLE_NAME, null, values);
    }

    public boolean delete(Context context, long id) {
        dbHelper = new BeerDBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete(BeerContract.BeerEntry.TABLE_NAME, "_id = ?", new String[]{String.valueOf(id)}) > 0;
    }

    public void updateBeerInDB(Context context, Beer b) {
        dbHelper = new BeerDBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(BeerContract.BeerEntry.COLUMN_NAME_DATE, date);  // TODO
        values.put(BeerContract.BeerEntry.COLUMN_NAME_DEFENDANT, b.getDefendant());
        values.put(BeerContract.BeerEntry.COLUMN_NAME_DEFENDANT_ID, b.getDefendant_id());
        values.put(BeerContract.BeerEntry.COLUMN_NAME_DESCRIPTION, b.getDescription());
        values.put(BeerContract.BeerEntry.COLUMN_NAME_PROSECUTORS, b.getProsecutors());
        values.put(BeerContract.BeerEntry.COLUMN_NAME_COUNT, b.getCount());

        db.update(BeerContract.BeerEntry.TABLE_NAME, values, "_id = ?", new String[]{String.valueOf(b.getId())});
    }

    public long getId() {
        return _id;
    }

    public void setId(long id) {
        this._id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDefendant() {
        return this.defendant;
    }

    public void setDefendant(String defendant) {
        this.defendant = defendant;
    }

    public int getDefendant_id() {
        return defendant_id;
    }

    public void setDefendant_id(int defendant_id) {
        this.defendant_id = defendant_id;
    }

    public String getProsecutors() {
        return prosecutors;
    }

    public void setProsecutors(String prosecutors) {
        this.prosecutors = prosecutors;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}


