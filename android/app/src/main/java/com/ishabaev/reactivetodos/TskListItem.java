package com.ishabaev.reactivetodos;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ishabaev.reactivetodos.sqlite.TasksProvider;

/**
 * Created by ishabaev on 17.08.16.
 */
public class TskListItem extends LinearLayout implements CursorBinder {

    private TextView mTitle;

    private TextView mAuthor;

    private TextView mPubDate;

    public TskListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    @SuppressLint("StringFormatMatches")
    public void bindCursor(Cursor c) {
        mTitle.setText(TasksProvider.getTitle(c));
        //mAuthor.setText(TasksProvider.getAuthor(c));
        //final long pubDate = TasksProvider.getPubDate(c);
        //if (pubDate > 0) {
        //mPubDate.setText(DateFormat.getDateTimeInstance().format(new Date(pubDate)));
        //}
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mTitle = (TextView) findViewById(R.id.title);
        mAuthor = (TextView) findViewById(R.id.author);
        mPubDate = (TextView) findViewById(R.id.pub_date);
    }

}