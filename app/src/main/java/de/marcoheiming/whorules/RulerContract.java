package de.marcoheiming.whorules;

import android.provider.BaseColumns;

public class RulerContract {

    private RulerContract() {
    }

    public static class RulerEntry implements BaseColumns {
        public static final String TABLE_NAME = "ruler";
        public static final String COLUMN_NAME_VASALL_ID = "vasall_id";
        public static final String COLUMN_NAME_SINCE = "since";
        public static final String COLUMN_NAME_RANK = "rank";
    }
}
