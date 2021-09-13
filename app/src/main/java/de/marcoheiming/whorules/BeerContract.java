package de.marcoheiming.whorules;

import android.provider.BaseColumns;

public class BeerContract {

    private BeerContract() {
    }

    public static class BeerEntry implements BaseColumns {
        public static final String TABLE_NAME = "beers";
        public static final String COLUMN_NAME_ID = "_id";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_DEFENDANT = "defendant";
        public static final String COLUMN_NAME_DEFENDANT_ID = "defendant_id";
        public static final String COLUMN_NAME_PROSECUTORS = "prosecutors";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_COUNT = "count";

    }
}
