package com.ishabaev.reactivetodos.todolist;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ishabaev.reactivetodos.R;
import com.ishabaev.reactivetodos.api.Task;

public class TaskListViewHolder extends RecyclerView.ViewHolder {

    private View mView;
    private TextView mTitleTextView;
    private TextView mContentTextView;
    private TextView mAuthorTextView;
    private TextView mPubDate;

    public TaskListViewHolder(View view) {
        super(view);
        mView = view;
        mTitleTextView = (TextView)view.findViewById(R.id.title);
        mContentTextView = (TextView)view.findViewById(R.id.content);
        mAuthorTextView = (TextView)view.findViewById(R.id.author);
        mPubDate = (TextView)view.findViewById(R.id.pub_date);
    }

    public void bind(@NonNull Task task) {
        mTitleTextView.setText(task.getTitle());
        mContentTextView.setText(task.getContent());
        mAuthorTextView.setText(task.getUseName());
        //mPubDate.setText(task.getCreatedAt());
    }

    public View getView() {
        return mView;
    }
}
