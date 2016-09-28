package com.ishabaev.ddp;

/**
 * Created by ishabaev on 16.08.16.
 */
public class DdpMessageType {
    // client -> server
    public static final String CONNECT = "connect";
    public static final String METHOD = "method";
    // server -> client
    public static final String CONNECTED = "connected";
    public static final String UPDATED = "updated";
    public static final String READY = "ready";
    public static final String NOSUB = "nosub";
    public static final String RESULT = "result";
    public static final String SUB = "sub";
    public static final String UNSUB = "unsub";
    public static final String ERROR = "error";
    public static final String CLOSED = "closed";
    public static final String ADDED = "added";
    public static final String REMOVED = "removed";
    public static final String CHANGED = "changed";
    public static final String PING = "ping";
    public static final String PONG = "pong";
}