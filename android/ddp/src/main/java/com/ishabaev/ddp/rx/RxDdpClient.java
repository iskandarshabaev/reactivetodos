package com.ishabaev.ddp.rx;

import com.ishabaev.ddp.DdpClient;
import com.ishabaev.ddp.DdpConnectListener;
import com.ishabaev.ddp.DdpData;
import com.ishabaev.ddp.DdpResultListener;
import com.ishabaev.ddp.DdpResultListenerImpl;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by ishabaev on 16.08.16.
 */
public class RxDdpClient {

    private DdpClient mDdpClient;

    public RxDdpClient(DdpClient client) {
        mDdpClient = client;
    }

    public Observable<DdpClient.ConnectionState> connect() {
        Observable<DdpClient.ConnectionState> observable = Observable.create(new Observable.OnSubscribe<DdpClient.ConnectionState>() {
            @Override
            public void call(Subscriber<? super DdpClient.ConnectionState> subscriber) {
                DdpConnectListener listener = new DdpConnectListener() {
                    @Override
                    public void onConnected(DdpClient.ConnectionState state) {
                        subscriber.onNext(state);
                    }

                    @Override
                    public void onDisconected(DdpClient.ConnectionState state) {
                        subscriber.onNext(state);
                    }

                    @Override
                    public void onException(Exception e) {
                        subscriber.onError(e);
                    }
                };
                mDdpClient.addConnectListener(listener);
                mDdpClient.connect();
            }
        });
        return observable;
    }

    public Observable<DdpData> login(String userName, String password) {
        return Observable.create(new Observable.OnSubscribe<DdpData>() {
            @Override
            public void call(Subscriber<? super DdpData> subscriber) {
                mDdpClient.login(userName, password, makeListener(subscriber));
            }
        });
    }

    public Observable<DdpData> tasks() {
        return Observable.create(new Observable.OnSubscribe<DdpData>() {
            @Override
            public void call(Subscriber<? super DdpData> subscriber) {
                mDdpClient.sub("tasks", new Object[]{}, makeListener(subscriber));
            }
        });
    }

    private DdpResultListener makeListener(Subscriber<? super DdpData> subscriber) {
        return new DdpResultListenerImpl() {
            @Override
            public void onResult(DdpData result) {
                subscriber.onNext(result);
                subscriber.onCompleted();
            }

            @Override
            public void onException(Exception e) {
                subscriber.onError(e);
            }
        };
    }
}
