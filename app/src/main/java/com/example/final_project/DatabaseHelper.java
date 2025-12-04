package com.example.final_project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "notes.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_NOTES_TABLE = "CREATE TABLE " +
                DatabaseContract.NoteEntry.TABLE_NAME + " (" +
                DatabaseContract.NoteEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DatabaseContract.NoteEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                DatabaseContract.NoteEntry.COLUMN_CONTENT + " TEXT, " +
                DatabaseContract.NoteEntry.COLUMN_DATE + " TEXT);";

        db.execSQL(SQL_CREATE_NOTES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.NoteEntry.TABLE_NAME);
        onCreate(db);
    }

    public long addNote(String title, String content, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DatabaseContract.NoteEntry.COLUMN_TITLE, title);
        values.put(DatabaseContract.NoteEntry.COLUMN_CONTENT, content);
        values.put(DatabaseContract.NoteEntry.COLUMN_DATE, date);

        long id = db.insert(DatabaseContract.NoteEntry.TABLE_NAME, null, values);
        db.close();

        return id;
    }

    public List<Note> getAllNotes() {
        List<Note> notes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + DatabaseContract.NoteEntry.TABLE_NAME +
                " ORDER BY " + DatabaseContract.NoteEntry.COLUMN_ID + " DESC";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Note note = new Note();
                note.setId(cursor.getInt(0)); // id
                note.setTitle(cursor.getString(1)); // title
                note.setContent(cursor.getString(2)); // content
                note.setDate(cursor.getString(3)); // date

                notes.add(note);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return notes;
    }

    public int updateNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseContract.NoteEntry.COLUMN_TITLE, note.getTitle());
        values.put(DatabaseContract.NoteEntry.COLUMN_CONTENT, note.getContent());
        values.put(DatabaseContract.NoteEntry.COLUMN_DATE, note.getDate());

        int rowsAffected = db.update(
                DatabaseContract.NoteEntry.TABLE_NAME,
                values,
                DatabaseContract.NoteEntry.COLUMN_ID + " = ?",
                new String[]{String.valueOf(note.getId())}
        );

        db.close();
        return rowsAffected;
    }
    public void deleteNote(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DatabaseContract.NoteEntry.TABLE_NAME,
                DatabaseContract.NoteEntry.COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    public int getNotesCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        String countQuery = "SELECT * FROM " + DatabaseContract.NoteEntry.TABLE_NAME;
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }
}
