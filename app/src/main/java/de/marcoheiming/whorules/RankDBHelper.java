package de.marcoheiming.whorules;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class RankDBHelper extends SQLiteOpenHelper {

    Context context;
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "Ranks.db";
    private final VasallsDBHelper vasallsDBHelper;

    List<Rank> listOfRanks = new ArrayList<Rank>();


    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + RankContract.RankEntry.TABLE_NAME + " (" +
                    RankContract.RankEntry._ID + " INTEGER PRIMARY KEY," +
                    RankContract.RankEntry.COLUMN_NAME_SORT + " INTEGER," +
                    RankContract.RankEntry.COLUMN_NAME_NAME + " TEXT," +
                    RankContract.RankEntry.COLUMN_NAME_NUMBER_OF_VASALLS + " INTEGER)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + RankContract.RankEntry.TABLE_NAME;

    public RankDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        this.vasallsDBHelper = new VasallsDBHelper(this.context);
    }

    public void onCreate(SQLiteDatabase db) {

        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion == 1 && newVersion == 2) {
            db.execSQL("ALTER TABLE " + RankContract.RankEntry.TABLE_NAME +
                    " ADD COLUMN " + RankContract.RankEntry.COLUMN_NAME_NUMBER_OF_VASALLS + " INTEGER;");
        } else {
            db.execSQL(SQL_DELETE_ENTRIES);
            onCreate(db);
            db.close();
        }
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public List<Rank> getListOfRanks() {
        SQLiteDatabase db = this.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                BaseColumns._ID,
                RankContract.RankEntry.COLUMN_NAME_SORT,
                RankContract.RankEntry.COLUMN_NAME_NAME,
                RankContract.RankEntry.COLUMN_NAME_NUMBER_OF_VASALLS
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                RankContract.RankEntry.COLUMN_NAME_SORT + " ASC";

        Cursor cursor = db.query(
                RankContract.RankEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );

        List<Rank> listOfRanks = new ArrayList<Rank>();
        while (cursor.moveToNext()) {
            Rank r = new Rank();
            r._id = cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID));
            r.sort = cursor.getInt(cursor.getColumnIndexOrThrow(RankContract.RankEntry.COLUMN_NAME_SORT));
            r.name = cursor.getString(cursor.getColumnIndexOrThrow(RankContract.RankEntry.COLUMN_NAME_NAME));
            r.numberOfVasallsHoldingThisRank = cursor.getInt(cursor.getColumnIndexOrThrow(RankContract.RankEntry.COLUMN_NAME_NUMBER_OF_VASALLS));

            listOfRanks.add(r);
        }
        cursor.close();
        db.close();
        return listOfRanks;
    }


    public Rank getRankFromName(String name) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                BaseColumns._ID,
                RankContract.RankEntry.COLUMN_NAME_SORT,
                RankContract.RankEntry.COLUMN_NAME_NAME,
                RankContract.RankEntry.COLUMN_NAME_NUMBER_OF_VASALLS
        };

        String selection = RankContract.RankEntry.COLUMN_NAME_NAME + " = ?";
        String[] selectionArgs = {name};

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                RankContract.RankEntry.COLUMN_NAME_NAME + " ASC";

        Cursor cursor = db.query(
                RankContract.RankEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );

        if (cursor.moveToNext()) {
            Rank r = new Rank();
            r._id = cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID));
            r.sort = cursor.getInt(cursor.getColumnIndexOrThrow(RankContract.RankEntry.COLUMN_NAME_SORT));
            r.name = cursor.getString(cursor.getColumnIndexOrThrow(RankContract.RankEntry.COLUMN_NAME_NAME));
            r.numberOfVasallsHoldingThisRank = cursor.getInt(cursor.getColumnIndexOrThrow(RankContract.RankEntry.COLUMN_NAME_NUMBER_OF_VASALLS));

            cursor.close();
            return r;
        }
        cursor.close();
        db.close();
        return null;
    }

    Rank getRankById(long id) {
        if (listOfRanks.isEmpty()) {
            listOfRanks = getListOfRanks();
        }

        for (Rank r : listOfRanks) {
            if (r._id == id) {
                return r;
            }
        }
        return null;
    }

    Rank getNextHigherRank(Rank rank) {
        if (listOfRanks.isEmpty()) {
            listOfRanks = getListOfRanks();
        }

        int index = listOfRanks.indexOf(rank);

        if (index >= 0 && listOfRanks.size() > (index + 1)) {
            return listOfRanks.get(++index);
        } else {
            Toast toast = Toast.makeText(context, "FEHLER: Kein höherer Rang als " + rank.name + " gefunden!", Toast.LENGTH_LONG);
            toast.show();
        }
        return null;
    }

    public Rank updateNumberOfVasallsHoldingTheRankForRank(int rankId) {
        Rank rank = getRankById(rankId);
        if (rank != null) {
            rank.numberOfVasallsHoldingThisRank = (int) vasallsDBHelper.getNumberOfVasallsForRank(rank);

            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(RankContract.RankEntry.COLUMN_NAME_NAME, rank.name);
            values.put(RankContract.RankEntry.COLUMN_NAME_SORT, rank.sort);
            values.put(RankContract.RankEntry.COLUMN_NAME_NUMBER_OF_VASALLS, rank.numberOfVasallsHoldingThisRank);

            db.update(RankContract.RankEntry.TABLE_NAME, values, "_id = ?", new String[]{String.valueOf(rank._id)});
            db.close();
            return rank;
        }
        return null;
    }

}
