package com.notes.android.arhsota.notes.db;

import android.net.Uri;
import android.provider.BaseColumns;

public final class NotesContract {

        public static final String DB_NAME = "notes.db";
        public static final int DB_VERSION = 1;

    public static final String AUTHORITY =
            "com.notes.android.arhsota.notes.provider";
    public static final String URI = "content://" +
            AUTHORITY;
        public static final String[] CREATE_DATABASE_QUERIES
                = {
                   Notes.CREATE_TABLE,
                   Notes.CREATE_UPDATED_TS_INDEX
        };

    public static abstract class Notes implements
            BaseColumns {
        public static final String CREATE_TABLE = "create_table";
        public static final String TABLE_NAME = "notes";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_NOTE = "note";
        public static final String COLUMN_CREATED_TS = "created_ts";
        public static final String COLUMN_UPDATED_TS = "updated_ts";
        public static final String CREATE_UPDATED_TS_INDEX = "create_updated_ts_index";

        public static final Uri URI = Uri.parse(NotesContract.URI + "/" + TABLE_NAME);

        // Список заметок
        public static final String URI_TYPE_NOTE_DIR =
                "vnd.android.cursor.dir/vnd.skillberg.note";
        // Одна заметка
        public static final String URI_TYPE_NOTE_ITEM =
                "vnd.android.cursor.item/vnd.skillberg.note";
    }
}