package com.notes.android.arhsota.notes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import static android.provider.BaseColumns._ID;
import static com.notes.android.arhsota.notes.db.NotesContract.Notes.COLUMN_CREATED_TS;
import static com.notes.android.arhsota.notes.db.NotesContract.Notes.COLUMN_NOTE;
import static com.notes.android.arhsota.notes.db.NotesContract.Notes.COLUMN_TITLE;
import static com.notes.android.arhsota.notes.db.NotesContract.Notes.COLUMN_UPDATED_TS;
import static com.notes.android.arhsota.notes.db.NotesContract.Notes.TABLE_NAME;
/*
import static com.notes.android.arhsota.notes.db.NotesContract.Notes.COLUMN_CREATED_TS;
import static com.notes.android.arhsota.notes.db.NotesContract.Notes.COLUMN_NOTE;
import static com.notes.android.arhsota.notes.db.NotesContract.Notes.COLUMN_TITLE;
import static com.notes.android.arhsota.notes.db.NotesContract.Notes.COLUMN_UPDATED_TS;
import static com.notes.android.arhsota.notes.db.NotesContract.Notes.TABLE_NAME;
*/

public class MainActivity extends AppCompatActivity {
    public static final String CREATE_TABLE =
            String.format("CREATE TABLE %s " +
                            "(%s INTEGER PRIMARY KEY, " +
                            "%s TEXT NOT NULL, " +
                            "%s TEXT NOT NULL, " +
                            "%s INTEGER NOT NULL, " +
                            "%s INTEGER NOT NULL);",
                    TABLE_NAME,
                    _ID,
                    COLUMN_TITLE,
                    COLUMN_NOTE,
                    COLUMN_CREATED_TS,
                    COLUMN_UPDATED_TS);
    public static final String CREATE_UPDATED_TS_INDEX =
            String.format("CREATE INDEX updated_ts_index " +
                            "ON %s (%s);",
                    TABLE_NAME,
                    COLUMN_UPDATED_TS);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
