package de.marcoheiming.whorules;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class BeerDBHelper extends SQLiteOpenHelper {

    Context context;
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Beers.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + BeerContract.BeerEntry.TABLE_NAME + " (" +
                    BeerContract.BeerEntry._ID + " INTEGER PRIMARY KEY," +
                    BeerContract.BeerEntry.COLUMN_NAME_DATE + " TEXT," +
                    BeerContract.BeerEntry.COLUMN_NAME_DEFENDANT + " TEXT," +
                    BeerContract.BeerEntry.COLUMN_NAME_DEFENDANT_ID + " INTEGER," +
                    BeerContract.BeerEntry.COLUMN_NAME_PROSECUTORS + " TEXT," +
                    BeerContract.BeerEntry.COLUMN_NAME_DESCRIPTION + " TEXT," +
                    BeerContract.BeerEntry.COLUMN_NAME_COUNT + " INTEGER)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + BeerContract.BeerEntry.TABLE_NAME;

    public BeerDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
        db.close();
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public List<Beer> getListOfBeers() {
        SQLiteDatabase db = this.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                BaseColumns._ID,
                BeerContract.BeerEntry.COLUMN_NAME_DATE,
                BeerContract.BeerEntry.COLUMN_NAME_DEFENDANT,
                BeerContract.BeerEntry.COLUMN_NAME_PROSECUTORS,
                BeerContract.BeerEntry.COLUMN_NAME_DESCRIPTION,
                BeerContract.BeerEntry.COLUMN_NAME_COUNT
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                BeerContract.BeerEntry.COLUMN_NAME_DATE + " ASC";

        Cursor cursor = db.query(
                BeerContract.BeerEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );

        BeerDBHelper dbBeer = new BeerDBHelper(context);

        List<Beer> listOfBeers = new ArrayList<Beer>();
        while (cursor.moveToNext()) {
            Beer b = new Beer();

            b.setDefendant(cursor.getString(cursor.getColumnIndexOrThrow(BeerContract.BeerEntry.COLUMN_NAME_DEFENDANT)));
            b.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(BeerContract.BeerEntry.COLUMN_NAME_DESCRIPTION)));
            b.setProsecutors(cursor.getString(cursor.getColumnIndexOrThrow(BeerContract.BeerEntry.COLUMN_NAME_PROSECUTORS)));
            b.setCount(cursor.getInt(cursor.getColumnIndexOrThrow(BeerContract.BeerEntry.COLUMN_NAME_COUNT)));

            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                b.setDate(sdf.parse(cursor.getString(cursor.getColumnIndexOrThrow(BeerContract.BeerEntry.COLUMN_NAME_DATE))));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            listOfBeers.add(b);
        }
        cursor.close();
        db.close();
        return listOfBeers;
    }

}
