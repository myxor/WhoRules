package de.marcoheiming.whorules;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

class Rank {

    private Context context;
    long _id;
    int sort;
    String name;
    int numberOfVasallsHoldingThisRank = 0; // will be set dynamically

    private RankDBHelper dbHelper;

    public Rank() {
    }

    public Rank(Context context, int sort, String name) {
        this.context = context;
        this.dbHelper = new RankDBHelper(context);
        Rank r = this.dbHelper.getRankFromName(name);
        int nov = (int) new VasallsDBHelper(this.context).getNumberOfVasallsForRank(r);

        if (r == null) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(RankContract.RankEntry.COLUMN_NAME_SORT, sort);
            values.put(RankContract.RankEntry.COLUMN_NAME_NAME, name);
            values.put(RankContract.RankEntry.COLUMN_NAME_NUMBER_OF_VASALLS, nov);
            this._id = db.insert(RankContract.RankEntry.TABLE_NAME, null, values);
        }

        this.sort = sort;
        this.name = name;
        this.numberOfVasallsHoldingThisRank = nov;
    }
}
