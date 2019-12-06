package de.marcoheiming.whorules;

import android.provider.BaseColumns;

public class RankContract {

    private RankContract() {
    }

    public static class RankEntry implements BaseColumns {
        public static final String TABLE_NAME = "ranks";
        public static final String COLUMN_NAME_SORT = "sort";
        public static final String COLUMN_NAME_NAME = "name";
    }
}
