package fr.colourz.mynotes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;



public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "notesManager";
    private static final String TABLE_NOTES = "notes";

    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_CONTENT = "content";
    private static final String KEY_DATE_CREATED = "dateCreated";
    private static final String KEY_DATE_MODIFIED = "dateModified";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NOTES + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_TITLE + " TEXT,"
                + KEY_CONTENT + " TEXT,"
                + KEY_DATE_CREATED + " TEXT,"
                + KEY_DATE_MODIFIED + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        onCreate(db);
    }

    public void addNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, note.getTitle());
        values.put(KEY_CONTENT, note.getContent());
        values.put(KEY_DATE_CREATED, note.getDateCreated());
        values.put(KEY_DATE_MODIFIED, note.getDateModified());

        db.insert(TABLE_NOTES, null, values);
        db.close();
    }

    public Note getNote(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NOTES,
            new String[] {
                KEY_ID,
                KEY_TITLE,
                KEY_CONTENT,
                KEY_DATE_CREATED,
                KEY_DATE_MODIFIED,
            },
            KEY_ID + "=?",
            new String[] { String.valueOf(id) }, null, null, null, null
        );
        if (cursor != null)
            cursor.moveToFirst();

        Note note = new Note(
            Integer.parseInt(cursor.getString(0)),
            cursor.getString(1),
            cursor.getString(2),
            cursor.getString(3),
            cursor.getString(4)
        );
        return note;
    }

    public List<Note> getAllNotes() {
        List<Note> noteList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_NOTES +
                " ORDER BY " + KEY_DATE_CREATED + " DESC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Note note = new Note();
                note.setId(Integer.parseInt(cursor.getString(0)));
                note.setTitle(cursor.getString(1));
                note.setContent(cursor.getString(2));
                note.setDateCreated(cursor.getString(3));
                note.setDateModified(cursor.getString(4));
                noteList.add(note);
            } while (cursor.moveToNext());
        }
        return noteList;
    }

    public List<Note> getNotesOrder(int filter, int order) {
        List<Note> noteList = new ArrayList<>();
        String selectQuery;
        if (filter == 0 && order == 0)
            selectQuery = "SELECT  * FROM " + TABLE_NOTES +
                    " ORDER BY " + KEY_TITLE + " ASC";
        else if (filter == 0 && order == 1)
            selectQuery = "SELECT  * FROM " + TABLE_NOTES +
                    " ORDER BY " + KEY_TITLE + " DESC";
        else if (filter == 1 && order == 0)
            selectQuery = "SELECT  * FROM " + TABLE_NOTES +
                    " ORDER BY " + KEY_DATE_MODIFIED + " ASC";
        else if (filter == 1 && order == 1)
            selectQuery = "SELECT  * FROM " + TABLE_NOTES +
                    " ORDER BY " + KEY_DATE_MODIFIED + " DESC";
        else if (filter == 2 && order == 0)
            selectQuery = "SELECT  * FROM " + TABLE_NOTES +
                    " ORDER BY " + KEY_DATE_CREATED + " ASC";
        else
            selectQuery = "SELECT  * FROM " + TABLE_NOTES +
                    " ORDER BY " + KEY_DATE_CREATED + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Note note = new Note();
                note.setId(Integer.parseInt(cursor.getString(0)));
                note.setTitle(cursor.getString(1));
                note.setContent(cursor.getString(2));
                note.setDateCreated(cursor.getString(3));
                note.setDateModified(cursor.getString(4));
                noteList.add(note);
            } while (cursor.moveToNext());
        }
        return noteList;
    }

    public List<Note> getNotesByTitle(String title) {
        List<Note> noteList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_NOTES +
                " WHERE " + KEY_TITLE + " LIKE '%" + title + "%' " +
                "OR " + KEY_CONTENT + " LIKE '%" + title + "%'" +
                " ORDER BY " + KEY_DATE_CREATED + " DESC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Note note = new Note();
                note.setId(Integer.parseInt(cursor.getString(0)));
                note.setTitle(cursor.getString(1));
                note.setContent(cursor.getString(2));
                note.setDateCreated(cursor.getString(3));
                note.setDateModified(cursor.getString(4));
                noteList.add(note);
            } while (cursor.moveToNext());
        }
        return noteList;
    }

    public boolean exists(String title) {
        String selectQuery = "SELECT  * FROM " + TABLE_NOTES +
                " WHERE " + KEY_TITLE + " = '" + title + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        List<Note> noteList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                Note note = new Note();
                note.setId(Integer.parseInt(cursor.getString(0)));
                note.setTitle(cursor.getString(1));
                note.setContent(cursor.getString(2));
                note.setDateCreated(cursor.getString(3));
                note.setDateModified(cursor.getString(4));
                noteList.add(note);
            } while (cursor.moveToNext());
        }
        return !noteList.isEmpty();
    }

    public int updateNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, note.getTitle());
        values.put(KEY_CONTENT, note.getContent());
        values.put(KEY_DATE_CREATED, note.getDateCreated());
        values.put(KEY_DATE_MODIFIED, note.getDateModified());
        return db.update(
            TABLE_NOTES,
            values,
            KEY_ID + " = ?",
            new String[] {
                String.valueOf(note.getId())
            }
        );
    }

    public void deleteNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOTES, KEY_ID + " = ?",
            new String[] { String.valueOf(note.getId()) });
        db.close();
    }
}
