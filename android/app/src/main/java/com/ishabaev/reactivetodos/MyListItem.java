package com.ishabaev.reactivetodos;

/**
 * Created by ishabaev on 19.08.16.
 */
public class MyListItem {
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /*public static MyListItem fromCursor(Cursor cursor) {
        return cursor.getString(TasksProvider.Columns.TEXT);
    }*/
}
