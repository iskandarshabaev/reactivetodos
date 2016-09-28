package com.ishabaev.reactivetodos.sqlite;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by ishabaev on 17.08.16.
 */
public class TasksProvider extends SQLiteTableProvider {

    public static final String TABLE_NAME = "tasks";

    public static final Uri URI = Uri.parse("content://com.ishabaev.simpletodos/" + TABLE_NAME);

    public TasksProvider() {
        super(TABLE_NAME);
    }

    public static String getDocId(Cursor c) {
        return c.getString(c.getColumnIndex(Columns.DOC_ID));
    }

    public static String getTitle(Cursor c) {
        return c.getString(c.getColumnIndex(Columns.TITLE));
    }

    public static String getContent(Cursor c) {
        return c.getString(c.getColumnIndex(Columns.CONTENT));
    }

    public static long getCreatedDate(Cursor c) {
        return c.getLong(c.getColumnIndex(Columns.CREATED_AT));
    }

    public static String getOwner(Cursor c) {
        return c.getString(c.getColumnIndex(Columns.OWNER));
    }

    public static String getUserName(Cursor c) {
        return c.getString(c.getColumnIndex(Columns.USER_NAME));
    }

    public static int getPrivate(Cursor c) {
        return c.getInt(c.getColumnIndex(Columns.PRIVATE));
    }

    public static int getChecked(Cursor c) {
        return c.getInt(c.getColumnIndex(Columns.CHECKED));
    }

    @Override
    public Uri getBaseUri() {
        return URI;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists " + TABLE_NAME +
                "(" + Columns.DOC_ID + " text primary key on conflict replace, "
                + Columns.TITLE + " text, "
                + Columns.CONTENT + " text, "
                + Columns.CREATED_AT + " integer, "
                + Columns.OWNER + " text, "
                + Columns.USER_NAME + " text, "
                + Columns.PRIVATE + " integer, "
                + Columns.CHECKED + " integer);");

        db.execSQL("create index if not exists " +
                TABLE_NAME + "_" + Columns.DOC_ID + "_index" +
                " on " + TABLE_NAME + "(" + Columns.DOC_ID + ");");
    }

    public interface Columns extends BaseColumns {
        String DOC_ID = "_id";
        String TITLE = "title";
        String CONTENT = "content";
        String CREATED_AT = "createdAt";
        String OWNER = "owner";
        String USER_NAME = "username";
        String PRIVATE = "private";
        String CHECKED = "checked";
    }
}
