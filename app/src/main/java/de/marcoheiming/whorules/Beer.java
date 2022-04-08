package de.marcoheiming.whorules;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;

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
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


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

        saveToDB();
    }

    /*
    creates Beer object from JSON
     */
    public Beer(Context context, JSONObject json) {
        this.context = context;
        try {
            this.defendant = json.getString("defendant");
            this.defendant_id = json.getInt("defendant_id");
            this.prosecutors = json.getString("prosecutors");
            this.description = json.getString("description");
            this.date = sdf.parse(json.getString("date"));
            this.count = json.getInt("count");

            this.dbHelper = new BeerDBHelper(this.context);

            saveToDB();
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }
    }

    public JSONObject toJSON() {
        JSONObject j = new JSONObject();
        try {
            j.put("defendant", this.defendant);
            j.put("defendant_id", this.defendant_id);
            j.put("prosecutors", this.prosecutors);
            j.put("description", this.description);
            j.put("date", sdf.format(this.defendant));
            j.put("count", this.count);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return j;
    }

    public long saveToDB() {
        return createBeer(this.date, this.defendant, this.defendant_id, this.prosecutors, this.description, this.count);
    }

    private long createBeer(Date date, String defendant, int defendant_id, String prosecutors, String description, int count) {

        this.defendant = defendant;
        this.defendant_id = defendant_id;
        this.prosecutors = prosecutors;
        this.description = description;
        this.date = date;
        this.count = count;

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(BeerContract.BeerEntry.COLUMN_NAME_ID, this._id);
        values.put(BeerContract.BeerEntry.COLUMN_NAME_DATE, sdf.format(date));
        values.put(BeerContract.BeerEntry.COLUMN_NAME_DEFENDANT, defendant);
        values.put(BeerContract.BeerEntry.COLUMN_NAME_DEFENDANT_ID, defendant_id);
        values.put(BeerContract.BeerEntry.COLUMN_NAME_DESCRIPTION, description);
        values.put(BeerContract.BeerEntry.COLUMN_NAME_PROSECUTORS, prosecutors);
        values.put(BeerContract.BeerEntry.COLUMN_NAME_COUNT, count);

        db.insert(BeerContract.BeerEntry.TABLE_NAME, null, values);

        return this._id;
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


