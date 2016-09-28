package com.ishabaev.ddp;

/**
 * Created by ishabaev on 16.08.16.
 */
public interface DdpResultListener {

    void onResult(DdpData result);

    void onException(Exception e);
}
