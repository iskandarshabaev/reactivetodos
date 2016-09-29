package com.ishabaev.reactivetodos.todolist;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ishabaev.reactivetodos.R;
import com.ishabaev.reactivetodos.api.Task;

import java.util.List;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListViewHolder> {

    private List<Task> mTasks;
    private OnTaskClickListener mOnTaskClickListener;

    public TaskListAdapter(@NonNull List<Task> tasks) {
        mTasks = tasks;
    }

    public void changeDataSet(List<Task> tasks){
        mTasks.clear();
        mTasks.addAll(tasks);
        notifyDataSetChanged();
    }

    public void setOnTaskClickListener(OnTaskClickListener onTaskClickListener){
        mOnTaskClickListener = onTaskClickListener;
    }

    @Override
    public TaskListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.li_task, parent, false);
        return new TaskListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TaskListViewHolder holder, int position) {
        holder.bind(mTasks.get(position));
        holder.getView().setOnClickListener(v -> {
            if (mOnTaskClickListener != null) {
                mOnTaskClickListener.onClick(mTasks.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTasks.size();
    }

    public interface OnTaskClickListener {
        void onClick(Task task);
    }
}
