package de.marcoheiming.whorules;

import android.provider.BaseColumns;

public final class VasallsContract {
    private VasallsContract() {
    }

    public static class VasallEntry implements BaseColumns {
        public static final String TABLE_NAME = "vasalls";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_RANK = "rank";
        public static final String COLUMN_NAME_NOR = "nor"; // numberOfReigns
    }
}
