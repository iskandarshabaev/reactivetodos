package com.ishabaev.reactivetodos.todolist;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.RelativeLayout;

import com.ishabaev.reactivetodos.R;
import com.ishabaev.reactivetodos.api.Task;
import com.ishabaev.reactivetodos.api.TodoApiImpl;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    private String mFeedId;
    private TaskListAdapter mTaskListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
        mTaskListAdapter = initTaskListAdapter();
        initRecyclerView(recyclerView, mTaskListAdapter);
        initRealmChangeListener();

        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.task_add_form);
        CardView cardView = (CardView) findViewById(R.id.card_task_add_form);

        initFloatingButton();

    }

    private void initRecyclerView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.Adapter adapter){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView.ItemAnimator animator = new DefaultItemAnimator();
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(animator);
        recyclerView.setAdapter(adapter);
    }

    private TaskListAdapter initTaskListAdapter(){
        TaskListAdapter adapter = new TaskListAdapter(new ArrayList<>());
        adapter.setOnTaskClickListener(task -> {

        });
        return adapter;
    }

    private void initRealmChangeListener() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Task> realmResults = realm.where(Task.class).findAllAsync();
        realmResults.addChangeListener(tasks -> mTaskListAdapter.changeDataSet(tasks));
        mTaskListAdapter.changeDataSet(realmResults);
    }

    @Override
    public void onResume() {
        super.onResume();
        TodoApiImpl todoApi = TodoApiImpl.getInstance();
        todoApi.connect("192.168.1.5");
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void initFloatingButton(){
        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_task);
        fab.setOnClickListener(new View.OnClickListener() {
            boolean clicked;

            @Override
            public void onClick(View view) {
                if (!clicked) {
                    clicked = true;
                    int cx = (fab.getLeft() + fab.getRight()) / 2;
                    int cy = (fab.getTop() + fab.getBottom()) / 2;

                    int finalRadius = Math.max(cardView.getWidth(), cardView.getHeight());

                    AnimatorSet animatorSet = new AnimatorSet();

                    *//*Animator anim =
                            ViewAnimationUtils.createCircularReveal(cardView, cx, cy, 0, finalRadius);*//*

                    ValueAnimator anim2 = ValueAnimator.ofFloat(0, 1);
                    anim2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            relativeLayout.setAlpha((Float) animation.getAnimatedValue());
                        }
                    });
                    *//*ValueAnimator anim2 = ValueAnimator.ofArgb(R.color.colorAccent, R.attr.background);
                    anim2.addUpdateListener(valueAnimator -> view.setBackgroundColor((Integer)valueAnimator.getAnimatedValue()));*//*


                    //animatorSet.playTogether(anim, anim2);

                    cardView.setVisibility(View.VISIBLE);
                    relativeLayout.setVisibility(View.VISIBLE);

                    *//*anim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                        }
                    });*//*
                    animatorSet.start();
                    //anim.start();


                } else {
                    clicked = false;
                    int cx = (fab.getLeft() + fab.getRight()) / 2;
                    int cy = (fab.getTop() + fab.getBottom()) / 2;

                    int initialRadius = cardView.getWidth();

                    *//*Animator anim =
                            ViewAnimationUtils.createCircularReveal(cardView, cx, cy, initialRadius, 0);*//*

                    //ValueAnimator anim2 = ValueAnimator.ofArgb(R.attr.background, R.color.colorAccent);

                    ValueAnimator anim2 = ValueAnimator.ofFloat(1, 0);
                    anim2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            relativeLayout.setAlpha((Float) animation.getAnimatedValue());
                        }
                    });


                    AnimatorSet animatorSet = new AnimatorSet();
                    //animatorSet.playTogether(anim, anim2);
                    *//*anim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            relativeLayout.setVisibility(View.GONE);
                            cardView.setVisibility(View.GONE);
                        }
                    });*//*
                    animatorSet.start();
                    //anim.start();
                }
            }
        });*/
    }

}
