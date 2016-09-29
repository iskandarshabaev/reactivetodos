package com.ishabaev.reactivetodos.api;

import com.google.gson.Gson;
import com.ishabaev.ddp.DdpClient;
import com.ishabaev.ddp.DdpData;
import com.ishabaev.ddp.rx.RxDdpClient;

import java.util.concurrent.TimeUnit;

import io.realm.Realm;
import okhttp3.OkHttpClient;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

public class TodoApiImpl implements TodoApi {

    private static TodoApiImpl INSTANCE;

    private PublishSubject<DdpData> taskPublishSubject = PublishSubject.create();
    private OkHttpClient mOkHttpClient;
    private DdpClient mDdpClient;
    private Gson mGson;

    private TodoApiImpl() {

        mOkHttpClient = new OkHttpClient.Builder()
                .readTimeout(0, TimeUnit.NANOSECONDS)
                .build();

        mGson = new Gson();
    }

    public static TodoApiImpl getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TodoApiImpl();
        }
        return INSTANCE;
    }

    public void connect(String address) {
        if (mDdpClient != null &&
                mDdpClient.getConnectionState() == DdpClient.ConnectionState.Connected) {
            return;
        }
        mDdpClient = new DdpClient(mOkHttpClient);
        mDdpClient.setUrl(address);
        mDdpClient.setPort(3000);
        RxDdpClient rxDdpClient = new RxDdpClient(mDdpClient);
        rxDdpClient.connect()
                .flatMap(aVoid -> rxDdpClient.tasks())
                .filter(ddpData -> ddpData != null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(ddpData -> {
                            taskPublishSubject.onNext(ddpData);
                            handleData(ddpData);
                        },
                        throwable -> taskPublishSubject.onError(throwable),
                        () -> taskPublishSubject.onCompleted());
    }

    private void handleData(DdpData ddpData) {
        switch (ddpData.getType()) {
            case ADDED:
                Task task = mGson.fromJson(ddpData.getAddedValue(), Task.class);
                task.setId(ddpData.getId());
                Realm.getDefaultInstance().executeTransaction(realm -> realm.insertOrUpdate(task));
                break;
            case REMOVED:
                Realm.getDefaultInstance().executeTransaction(
                        realm -> realm.where(Task.class)
                                .equalTo("mId", ddpData.getId())
                                .findAll()
                                .deleteAllFromRealm());
                break;
        }
    }
}
