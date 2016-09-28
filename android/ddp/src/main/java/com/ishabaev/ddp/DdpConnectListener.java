package com.ishabaev.ddp;

/**
 * Created by ishabaev on 16.08.16.
 */
public interface DdpConnectListener {

    void onConnected(DdpClient.ConnectionState state);

    void onDisconected(DdpClient.ConnectionState state);

    void onException(Exception e);
}
