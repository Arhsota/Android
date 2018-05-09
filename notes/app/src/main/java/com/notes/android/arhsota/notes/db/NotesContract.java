package com.notes.android.arhsota.notes.db;

import android.provider.BaseColumns;

public final class NotesContract {
    private NotesContract() {
}
    public static abstract class Notes implements
            BaseColumns {
        public static final String TABLE_NAME = "notes";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_NOTE = "note";
        public static final String COLUMN_CREATED_TS = "created_ts";
        public static final String COLUMN_UPDATED_TS = "updated_ts";
    }
}