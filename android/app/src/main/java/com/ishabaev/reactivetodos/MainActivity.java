package com.ishabaev.reactivetodos;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.SyncStatusObserver;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.RelativeLayout;

import com.ishabaev.reactivetodos.api.TodoApiImpl;
import com.ishabaev.reactivetodos.sqlite.TasksProvider;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>,
        SyncStatusObserver {

    private Object mSyncMonitor;

    private String mFeedId;
    //private CursorAdapter mListAdapter;
    //private ListView mListView;
    private MyListCursorAdapter cursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //mListView = (ListView)findViewById(R.id.list);
        getLoaderManager().initLoader(999, null, this);
        //mListAdapter = new CursorBinderAdapter(this, R.layout.li_task);
        //setListAdapter(mListAdapter);
        cursorAdapter = new MyListCursorAdapter(this, null);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView.ItemAnimator animator = new DefaultItemAnimator();

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(animator);
        recyclerView.setAdapter(cursorAdapter);

        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.task_add_form);
        CardView cardView = (CardView) findViewById(R.id.card_task_add_form);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_task);
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

                    Animator anim =
                            ViewAnimationUtils.createCircularReveal(cardView, cx, cy, 0, finalRadius);

                    ValueAnimator anim2 = ValueAnimator.ofFloat(0, 1);
                    anim2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            relativeLayout.setAlpha((Float) animation.getAnimatedValue());
                        }
                    });
                    /*ValueAnimator anim2 = ValueAnimator.ofArgb(R.color.colorAccent, R.attr.background);
                    anim2.addUpdateListener(valueAnimator -> view.setBackgroundColor((Integer)valueAnimator.getAnimatedValue()));*/


                    animatorSet.playTogether(anim, anim2);

                    cardView.setVisibility(View.VISIBLE);
                    relativeLayout.setVisibility(View.VISIBLE);

                    anim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                        }
                    });
                    animatorSet.start();
                    //anim.start();


                } else {
                    clicked = false;
                    int cx = (fab.getLeft() + fab.getRight()) / 2;
                    int cy = (fab.getTop() + fab.getBottom()) / 2;

                    int initialRadius = cardView.getWidth();

                    Animator anim =
                            ViewAnimationUtils.createCircularReveal(cardView, cx, cy, initialRadius, 0);

                    //ValueAnimator anim2 = ValueAnimator.ofArgb(R.attr.background, R.color.colorAccent);

                    ValueAnimator anim2 = ValueAnimator.ofFloat(1, 0);
                    anim2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            relativeLayout.setAlpha((Float) animation.getAnimatedValue());
                        }
                    });


                    AnimatorSet animatorSet = new AnimatorSet();
                    animatorSet.playTogether(anim, anim2);
                    anim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            relativeLayout.setVisibility(View.GONE);
                            cardView.setVisibility(View.GONE);
                        }
                    });
                    animatorSet.start();
                    //anim.start();
                }
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        mSyncMonitor = ContentResolver.addStatusChangeListener(
                ContentResolver.SYNC_OBSERVER_TYPE_ACTIVE
                        | ContentResolver.SYNC_OBSERVER_TYPE_PENDING,
                this
        );
        ContentResolver resolver = getContentResolver();
        TodoApiImpl todoApi = TodoApiImpl.getInstance(resolver);
        todoApi.connect("192.168.1.5");
    }

    /*public void setListAdapter(ListAdapter adapter) {
        //final DataSetObserver dataSetObserver = mSwipeToDismissController.getDataSetObserver();
        final ListAdapter oldAdapter = mListView.getAdapter();
        if (oldAdapter != null) {
            //oldAdapter.unregisterDataSetObserver(dataSetObserver);
        }
        mListView.setAdapter(adapter);
        //adapter.registerDataSetObserver(dataSetObserver);

        cursorAdapter  = new MyListCursorAdapter(this, null);
    }*/

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                getApplicationContext(),
                TasksProvider.URI, null,
                null,
                null,
                TasksProvider.Columns.CREATED_AT + " DESC"
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        //mListAdapter.swapCursor(data);
        cursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //mListAdapter.swapCursor(null);
        cursorAdapter.swapCursor(null);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ContentResolver.removeStatusChangeListener(mSyncMonitor);
    }

    @Override
    public void onStatusChanged(int i) {

    }
}
