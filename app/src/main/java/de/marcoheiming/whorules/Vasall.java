package de.marcoheiming.whorules;

import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.widget.Toast;

public class Vasall {

    private Context context;

    private long _id;
    private String name;
    private int numberOfReigns = 0;
    private int rankId = DEFAULT_RANK;

    private static final int DEFAULT_RANK = 1;

    private VasallsDBHelper dbHelper;

    public Vasall() {
    }

    public Vasall(Context context) {
        this.context = context;
    }

    public Vasall(Context context, String name) {

        this.context = context;
        this.name = name;

        this.dbHelper = new VasallsDBHelper(this.context);

        this.createVasall(name, this.rankId);
    }

    public Vasall(Context context, String name, String rankId) {

        this.context = context;
        this.name = name;

        RankDBHelper rankDBHelper = new RankDBHelper(this.context);
        Rank r = rankDBHelper.getRankFromName(rankId);

        if (r != null) {
            this.rankId = (int) r._id;
        } else {
            this.rankId = DEFAULT_RANK;
        }

        this.dbHelper = new VasallsDBHelper(this.context);

        this.createVasall(name, this.rankId);
    }

    public Vasall(Context context, String name, int rankId) {

        this.context = context;
        this.name = name;
        this.rankId = rankId;

        this.dbHelper = new VasallsDBHelper(this.context);

        this.createVasall(name, rankId);
    }

    private void createVasall(String name, int rank) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Vasall v = dbHelper.getVasallFromDB(name);
        if (v == null) {
            ContentValues values = new ContentValues();
            values.put(VasallsContract.VasallEntry.COLUMN_NAME_NAME, name);
            values.put(VasallsContract.VasallEntry.COLUMN_NAME_RANK, rank);
            values.put(VasallsContract.VasallEntry.COLUMN_NAME_NOR, 0);

            this._id = db.insert(VasallsContract.VasallEntry.TABLE_NAME, null, values);
        } else {
            this._id = v._id;
            this.name = v.name;
            this.numberOfReigns = v.numberOfReigns;
            this.rankId = v.rankId;
        }

        new RankDBHelper(this.context).updateNumberOfVasallsHoldingTheRankForRank(rank);
    }

    public long getId() {
        return _id;
    }

    public void setId(long id) {
        this._id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {

        if (!name.equals(this.name)) {
            this.name = name;
            updateVasallInDB();
        }
    }

    public int getNumberOfReigns() {

        return numberOfReigns;
    }

    public void setNumberOfReigns(int numberOfReigns) {

        this.numberOfReigns = numberOfReigns;

        updateVasallInDB();
    }

    public void increaseNumberOfReigns() {
        this.numberOfReigns++;


        // Set new rank
        if (this.numberOfReigns > 5) {
            RankDBHelper rankDBHelper = new RankDBHelper(this.context);
            Rank currentRank = rankDBHelper.getRankById(this.rankId);
            if (currentRank != null) {
                Rank newRank = rankDBHelper.getNextHigherRank(currentRank);
                if (newRank != null) {
                    this.setRankId((int) newRank._id);
                    this.setNumberOfReigns(0);

                    Toast toast = Toast.makeText(context, this.name + " aufgestiegen von " + currentRank.name + " zu " + newRank.name, Toast.LENGTH_LONG);
                    toast.show();

                    NotificationCompat.Builder builder = new NotificationCompat.Builder(this.context, MainActivity.NOTIFICATION_CHANNEL_PROMOTION_ID)
                            .setSmallIcon(R.drawable.ic_people)
                            .setContentTitle(this.name + " ist aufgestiegen")
                            .setContentText(this.name + " ist nun vom Rank " + newRank.name)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .setAutoCancel(true)
                            .setOngoing(true);

                    PendingIntent contentIntent = PendingIntent.getActivity(this.context, 0,
                            new Intent(this.context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(contentIntent);

                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this.context);
                    int notificationId = 0; // TODO save it
                    notificationManager.notify(notificationId, builder.build());

                } else {
                    // No new rank found. What do we do now?
                    this.setRank("?");
                    this.setNumberOfReigns(0);

                    Toast toast = Toast.makeText(context, this.name + " aufgestiegen von " + currentRank.name + " zu (unbekannter Rank)", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        }

        updateVasallInDB();
    }

    public String getRankId() {

        RankDBHelper rankDBHelper = new RankDBHelper(this.context);
        Rank r = rankDBHelper.getRankById(this.rankId);
        if (r != null) {
            return r.name;
        } else {
            return "?"; // Not good
        }
    }

    public void setRankId(int rankId) {

        this.rankId = rankId;

        updateVasallInDB();
    }


    public void setRank(String rank) {

        int oldRankId = this.rankId;

        RankDBHelper rankDBHelper = new RankDBHelper(this.context);
        Rank r = rankDBHelper.getRankFromName(rank);
        if (r != null) {
            this.rankId = (int) r._id;
        } else {
            this.rankId = 0; // Not good
        }

        new RankDBHelper(this.context).updateNumberOfVasallsHoldingTheRankForRank(oldRankId);
        new RankDBHelper(this.context).updateNumberOfVasallsHoldingTheRankForRank(this.rankId);

        updateVasallInDB();
    }

    private void updateVasallInDB() {
        if (dbHelper != null) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(VasallsContract.VasallEntry.COLUMN_NAME_NAME, name);
            values.put(VasallsContract.VasallEntry.COLUMN_NAME_RANK, rankId);
            values.put(VasallsContract.VasallEntry.COLUMN_NAME_NOR, numberOfReigns);

            db.update(VasallsContract.VasallEntry.TABLE_NAME, values, "_id = ?", new String[]{String.valueOf(_id)});

            new RankDBHelper(this.context).updateNumberOfVasallsHoldingTheRankForRank(this.rankId);
        }
    }

    public boolean delete() {
        dbHelper = new VasallsDBHelper(this.context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        new RankDBHelper(this.context).updateNumberOfVasallsHoldingTheRankForRank(this.rankId);

        if (_id > 0) {
            return db.delete(VasallsContract.VasallEntry.TABLE_NAME, "_id = ?", new String[]{String.valueOf(_id)}) > 0;
        } else {
            return db.delete(VasallsContract.VasallEntry.TABLE_NAME, VasallsContract.VasallEntry.COLUMN_NAME_NAME + " = ?", new String[]{name}) > 0;
        }
    }

}


