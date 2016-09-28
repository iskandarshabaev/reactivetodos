package com.ishabaev.reactivetodos.api;

import android.content.ContentResolver;
import android.content.ContentValues;

import com.google.gson.Gson;
import com.ishabaev.ddp.DdpClient;
import com.ishabaev.ddp.DdpData;
import com.ishabaev.ddp.rx.RxDdpClient;
import com.ishabaev.reactivetodos.sqlite.TasksProvider;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

/**
 * Created by ishabaev on 17.08.16.
 */
public class TodoApiImpl implements TodoApi {

    private static TodoApiImpl INSTANCE;

    private PublishSubject<DdpData> taskPublishSubject = PublishSubject.create();
    private OkHttpClient mOkHttpClient;
    private DdpClient mDdpClient;
    private Gson mGson;

    private ContentResolver resolver;

    private TodoApiImpl(ContentResolver resolver) {

        mOkHttpClient = new OkHttpClient.Builder()
                .readTimeout(0, TimeUnit.NANOSECONDS)
                .build();

        mGson = new Gson();

        this.resolver = resolver;
    }

    public static TodoApiImpl getInstance(ContentResolver resolver) {
        if (INSTANCE == null) {
            INSTANCE = new TodoApiImpl(resolver);
        }
        return INSTANCE;
    }

    public void connect(String adress) {

        if (mDdpClient != null &&
                mDdpClient.getConnectionState() == DdpClient.ConnectionState.Connected) {
            return;
        }

        resolver.delete(TasksProvider.URI,null,null);

        mDdpClient = new DdpClient(mOkHttpClient);
        mDdpClient.setUrl(adress);
        mDdpClient.setPort(3000);

        RxDdpClient rxDdpClient = new RxDdpClient(mDdpClient);
        rxDdpClient.connect()
                .flatMap(aVoid -> rxDdpClient.tasks())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(ddpData -> {
                            if (ddpData != null) {
                                taskPublishSubject.onNext(ddpData);

                                switch (ddpData.getType()) {
                                    case ADDED:
                                        try {
                                            Task task = mGson.fromJson(ddpData.getAddedValue(), Task.class);
                                            ContentValues values = new ContentValues();
                                            values.put(TasksProvider.Columns.DOC_ID, ddpData.getId());
                                            values.put(TasksProvider.Columns.TITLE, task.getTitle());
                                            values.put(TasksProvider.Columns.CONTENT, task.getContent());
                                            values.put(TasksProvider.Columns.CREATED_AT, task.getCreatedAt());
                                            values.put(TasksProvider.Columns.OWNER, task.getOwner());
                                            values.put(TasksProvider.Columns.USER_NAME, task.getUseName());
                                            values.put(TasksProvider.Columns.PRIVATE, task.isPrivateField() ? 1 : 0);
                                            values.put(TasksProvider.Columns.CHECKED, task.isChecked() ? 1 : 0);

                                            resolver.insert(TasksProvider.URI, values);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        break;
                                    case REMOVED:
                                        resolver.delete(
                                                TasksProvider.URI, TasksProvider.Columns.DOC_ID +
                                                        " = ? ", new String[]{ddpData.getId()});
                                        break;
                                }


                            }
                        },
                        throwable -> {
                            taskPublishSubject.onError(throwable);
                        },
                        () -> {
                            taskPublishSubject.onCompleted();
                        });
    }
}
