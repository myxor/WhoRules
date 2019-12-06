package de.marcoheiming.whorules;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class RulerDBHelper extends SQLiteOpenHelper {

    Context context;
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "Rulers.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + RulerContract.RulerEntry.TABLE_NAME + " (" +
                    RulerContract.RulerEntry._ID + " INTEGER PRIMARY KEY," +
                    RulerContract.RulerEntry.COLUMN_NAME_VASALL_ID + " INTEGER," +
                    RulerContract.RulerEntry.COLUMN_NAME_SINCE + " TEXT, " +
                    RulerContract.RulerEntry.COLUMN_NAME_RANK + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + RulerContract.RulerEntry.TABLE_NAME;

    public RulerDBHelper(Context context) {
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
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public List<Ruler> getListOfRulers() {
        SQLiteDatabase db = this.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                BaseColumns._ID,
                RulerContract.RulerEntry.COLUMN_NAME_VASALL_ID,
                RulerContract.RulerEntry.COLUMN_NAME_SINCE,
                RulerContract.RulerEntry.COLUMN_NAME_RANK
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                RulerContract.RulerEntry.COLUMN_NAME_SINCE + " ASC";

        Cursor cursor = db.query(
                RulerContract.RulerEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );

        VasallsDBHelper dbVasall = new VasallsDBHelper(context);

        List<Ruler> listOfRulers = new ArrayList<Ruler>();
        while (cursor.moveToNext()) {
            Ruler r = new Ruler();
            Vasall v = dbVasall.getVasallById((cursor.getLong(cursor.getColumnIndexOrThrow(RulerContract.RulerEntry.COLUMN_NAME_VASALL_ID))));
            if (v != null)
            {
                r.name = v.getName();
                r.vasallId = v.getId();
            }
            else
            {
                r.name = "?";
            }

            r.rank = cursor.getString(cursor.getColumnIndexOrThrow(RulerContract.RulerEntry.COLUMN_NAME_RANK));

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                r.startDate = sdf.parse(cursor.getString(cursor.getColumnIndexOrThrow(RulerContract.RulerEntry.COLUMN_NAME_SINCE)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            listOfRulers.add(r);
        }
        cursor.close();
        return listOfRulers;
    }

}
