package com.ishabaev.reactivetodos;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ishabaev.reactivetodos.sqlite.TasksProvider;

/**
 * Created by ishabaev on 19.08.16.
 */
public class MyListCursorAdapter extends CursorRecyclerViewAdapter<MyListCursorAdapter.ViewHolder> {

    public MyListCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTitleView;
        public TextView mContentView;

        public ViewHolder(View view) {
            super(view);
            mTitleView = (TextView) view.findViewById(R.id.title);
            mContentView = ( TextView) view.findViewById(R.id.content);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.li_task, parent, false);
        ViewHolder vh = new ViewHolder(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {
        //MyListItem myListItem = MyListItem.fromCursor(cursor);
        viewHolder.mTitleView.setText(TasksProvider.getTitle(cursor));
        viewHolder.mContentView.setText(TasksProvider.getContent(cursor));
    }
}
