package com.ishabaev.ddp;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.ws.WebSocket;
import okhttp3.ws.WebSocketCall;
import okhttp3.ws.WebSocketListener;
import okio.Buffer;

/**
 * Created by ishabaev on 15.08.16.
 */
public class DdpClient {

    public enum ConnectionState {
        Connecting,
        Connected,
        Disconnecting,
        Disconnected
    }

    private final static String DDP_PROTOCOL_VERSION = "1";
    public static final int DEFAULT_PORT = 3000;

    private WebSocket mWebSocket;
    private Gson mGson = new Gson();
    private JsonParser mParser = new JsonParser();
    private String mUrl;
    private int mPort = DEFAULT_PORT;
    private OkHttpClient mOkHttpClient;
    private ConnectionState mConnectionState;
    private final String LOG_TAG = "DdpClient";
    private final String LOG_INPUT = "<-----";
    private final String LOG_OUTPUT = "----->";
    private Map<String, DdpResultListener> mListeners;
    private List<DdpConnectListener> connectListeners;
    private AtomicInteger mCurrentId;

    public DdpClient(OkHttpClient okHttpClient) {
        mOkHttpClient = okHttpClient;
        mListeners = new HashMap<>();
        connectListeners = new ArrayList<>();
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public void setPort(int port) {
        mPort = port;
    }

    public void connect() {
        mConnectionState = ConnectionState.Connecting;
        this.mCurrentId = new AtomicInteger(0);
        final Request request = new Request.Builder().url("ws://" + mUrl + ":" + mPort + "/websocket").build();
        WebSocketCall call = WebSocketCall.create(mOkHttpClient, request);
        call.enqueue(new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                mWebSocket = webSocket;
                connectionOpened();
            }

            @Override
            public void onFailure(IOException e, Response response) {
                for (DdpConnectListener listener : connectListeners) {
                    listener.onException(e);
                }
                e.printStackTrace();
                mConnectionState = ConnectionState.Disconnected;
                //connect();
            }

            @Override
            public void onMessage(ResponseBody responseBody) {
                if (responseBody.contentType() == WebSocket.TEXT) {
                    try {
                        handleTextMessage(responseBody.string());
                    } catch (IOException e) {
                        e.printStackTrace();
                        mConnectionState = ConnectionState.Disconnected;
                    }
                }
                responseBody.close();
            }

            @Override
            public void onPong(Buffer payload) {

            }

            @Override
            public void onClose(int code, String reason) {
                mConnectionState = ConnectionState.Disconnected;
            }
        });
    }

    public void addConnectListener(DdpConnectListener connectListener) {
        connectListeners.add(connectListener);
    }

    public ConnectionState getConnectionState() {
        return mConnectionState;
    }

    public void send(Map<String, Object> msgParams) {
        try {
            String json = mGson.toJson(msgParams);
            Log.d(LOG_TAG, LOG_OUTPUT + json);
            mWebSocket.sendMessage(RequestBody.create(WebSocket.TEXT, json));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void connectionOpened() {
        Map<String, Object> connectMsg = new HashMap<>();
        connectMsg.put(DdpMessageField.MSG, DdpMessageType.CONNECT);
        connectMsg.put(DdpMessageField.VERSION, DDP_PROTOCOL_VERSION);
        connectMsg.put(DdpMessageField.SUPPORT, new String[]{DDP_PROTOCOL_VERSION, "pre2", "pre1"});
        send(connectMsg);
    }

    private void handleTextMessage(String message) {
        try {
            Log.d(LOG_TAG, LOG_INPUT + message);
            JsonObject json = mParser.parse(message).getAsJsonObject();
            if (!json.has(DdpMessageField.MSG)) {
                return;
            }
            String msgType = json.get(DdpMessageField.MSG).getAsString();
            if (msgType == null) {
                return;
            }
            switch (msgType) {
                case DdpMessageType.CONNECTED:
                    mConnectionState = ConnectionState.Connected;
                    for (DdpConnectListener listener : connectListeners) {
                        listener.onConnected(mConnectionState);
                    }
                    break;
                case DdpMessageType.PING:
                    Map<String, Object> pongMsg = new HashMap<>();
                    pongMsg.put(DdpMessageField.MSG, DdpMessageType.PONG);
                    send(pongMsg);
                    break;
                case DdpMessageType.PONG:

                    break;
                case DdpMessageType.CLOSED:
                    mConnectionState = ConnectionState.Disconnected;
                    for (DdpConnectListener listener : connectListeners) {
                        listener.onDisconected(mConnectionState);
                    }
                    break;
                case DdpMessageType.RESULT:

                    break;
                case DdpMessageType.ADDED:
                    //String id = json.get(DdpMessageField.ID).getAsString();
                    for (DdpResultListener listener : mListeners.values()) {

                        String id = json.get(DdpMessageField.ID).toString();
                        String collectionName = json.get(DdpMessageField.COLLECTION).toString();
                        String addedValue = json.get(DdpMessageField.FIELDS).toString();
                        DdpData ddpData = new DdpData(id, DdpData.DataType.ADDED, collectionName, addedValue);

                        listener.onResult(ddpData);
                    }
                    break;
                case DdpMessageType.REMOVED:
                    for (DdpResultListener listener : mListeners.values()) {

                        String id = json.get(DdpMessageField.ID).toString();
                        String collectionName = json.get(DdpMessageField.COLLECTION).toString();
                        DdpData ddpData = new DdpData(id, DdpData.DataType.REMOVED, collectionName, null);

                        listener.onResult(ddpData);
                    }
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void login(String userName, String password, DdpResultListener listener) {
        call(Integer.toString(nextId()), "login", null, listener);
    }

    public void call(String methodName, Object[] params, DdpResultListener listener) {
        call(methodName, params, listener);
    }

    public void call(String methodId, String methodName, Object[] params, DdpResultListener listener) {
        if (listener != null) {
            mListeners.put(methodId, listener);
        }
        Map<String, Object> data = new HashMap<>();
        data.put(DdpMessageField.MSG, DdpMessageType.METHOD);
        data.put(DdpMessageField.METHOD, methodName);
        data.put(DdpMessageField.ID, methodId);
        if (params != null) {
            data.put(DdpMessageField.PARAMS, params);
        }
        send(data);
    }

    private int nextId() {
        return mCurrentId.incrementAndGet();
    }

    private String generateUuid() {
        return UUID.randomUUID().toString();
    }

    public void sub(String subName, Object[] params, DdpResultListener listener) {
        String subId = generateUuid();
        sub(subId, subName, params, listener);
    }

    public void sub(String subId, String subName, Object[] params, DdpResultListener listener) {
        if (listener != null) {
            mListeners.put(subId, listener);
        }
        Map<String, Object> data = new HashMap<>();
        data.put(DdpMessageField.MSG, DdpMessageType.SUB);
        data.put(DdpMessageField.NAME, subName);
        data.put(DdpMessageField.ID, subId);
        if (params != null) {
            data.put(DdpMessageField.PARAMS, params);
        }
        send(data);
    }
}
