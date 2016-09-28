package com.ishabaev.reactivetodos.sqlite;

/**
 * Created by ishabaev on 17.08.16.
 */
public interface SQLiteOperation {

    int INSERT = 1;

    int UPDATE = 2;

    int DELETE = 3;

    String KEY_LAST_ID = "com.ishabaev.simpletodos.sqlite.KEY_LAST_ID";

    String KEY_AFFECTED_ROWS = "com.ishabaev.simpletodos.sqlite.KEY_AFFECTED_ROWS";

}
