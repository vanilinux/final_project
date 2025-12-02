package com.example.final_project;
import android.provider.BaseColumns;
public final class DatabaseContract {
    private DatabaseContract(){}
    public static class NoteEntry implements BaseColumns{
        public static final String TABLE_NAME = "notes";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_CONTENT = "content";
        public static final String COLUMN_DATE = "date";
    }
}
