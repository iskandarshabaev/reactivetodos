package com.ishabaev.reactivetodos;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

/**
 * Created by ishabaev on 17.08.16.
 */
public class CursorBinderAdapter extends CursorAdapter {

    private final LayoutInflater mInflater;

    private final int mLayoutResId;

    public CursorBinderAdapter(Context context, int layoutResId) {
        super(context, null, FLAG_REGISTER_CONTENT_OBSERVER);
        mInflater = LayoutInflater.from(context);
        mLayoutResId = layoutResId;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return mInflater.inflate(mLayoutResId, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ((CursorBinder) view).bindCursor(cursor);
    }
}