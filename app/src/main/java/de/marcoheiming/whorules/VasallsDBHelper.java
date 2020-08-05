package de.marcoheiming.whorules;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

public class VasallsDBHelper extends SQLiteOpenHelper {

    Context context;
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "Vasalls.db";

    List<Vasall> listOfVasalls = new ArrayList<Vasall>();

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + VasallsContract.VasallEntry.TABLE_NAME + " (" +
                    VasallsContract.VasallEntry._ID + " INTEGER PRIMARY KEY," +
                    VasallsContract.VasallEntry.COLUMN_NAME_NAME + " TEXT," +
                    VasallsContract.VasallEntry.COLUMN_NAME_RANK + " INTEGER," +
                    VasallsContract.VasallEntry.COLUMN_NAME_NOR + " INTEGER)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + VasallsContract.VasallEntry.TABLE_NAME;

    public VasallsDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }


    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
        db.close();
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    Vasall getVasallById(long id) {
        if (listOfVasalls.isEmpty()) {
            listOfVasalls = getListOfVasalls();
        }

        for (Vasall v : listOfVasalls) {
            if (v.getId() == id) {
                return v;
            }
        }
        return null;
    }


    public Vasall getVasallFromDB(String name) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                BaseColumns._ID,
                VasallsContract.VasallEntry.COLUMN_NAME_NAME,
                VasallsContract.VasallEntry.COLUMN_NAME_RANK,
                VasallsContract.VasallEntry.COLUMN_NAME_NOR
        };

        String selection = VasallsContract.VasallEntry.COLUMN_NAME_NAME + " = ?";
        String[] selectionArgs = {name};

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                VasallsContract.VasallEntry.COLUMN_NAME_NAME + " ASC";

        Cursor cursor = db.query(
                VasallsContract.VasallEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );

        if (cursor.moveToNext()) {
            Vasall v = new Vasall();
            v.setId(cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID)));
            v.setName(cursor.getString(cursor.getColumnIndexOrThrow(VasallsContract.VasallEntry.COLUMN_NAME_NAME)));
            v.setRank(cursor.getInt(cursor.getColumnIndexOrThrow(VasallsContract.VasallEntry.COLUMN_NAME_RANK)));
            v.setNumberOfReigns((int) cursor.getLong(cursor.getColumnIndexOrThrow(VasallsContract.VasallEntry.COLUMN_NAME_NOR)));
            cursor.close();
            return v;
        }
        cursor.close();
        db.close();
        return null;
    }

    public List<Vasall> getListOfVasalls() {
        SQLiteDatabase db = this.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                BaseColumns._ID,
                VasallsContract.VasallEntry.COLUMN_NAME_NAME,
                VasallsContract.VasallEntry.COLUMN_NAME_RANK,
                VasallsContract.VasallEntry.COLUMN_NAME_NOR
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                VasallsContract.VasallEntry.COLUMN_NAME_NAME + " ASC";

        Cursor cursor = db.query(
                VasallsContract.VasallEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );

        while (cursor.moveToNext()) {
            Vasall v = new Vasall(this.context);
            v.setId(cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID)));
            v.setName(cursor.getString(cursor.getColumnIndexOrThrow(VasallsContract.VasallEntry.COLUMN_NAME_NAME)));
            v.setRank(cursor.getInt(cursor.getColumnIndexOrThrow(VasallsContract.VasallEntry.COLUMN_NAME_RANK)));
            v.setNumberOfReigns((int) cursor.getLong(cursor.getColumnIndexOrThrow(VasallsContract.VasallEntry.COLUMN_NAME_NOR)));

            listOfVasalls.add(v);
        }
        cursor.close();
        db.close();
        return listOfVasalls;
    }
}
