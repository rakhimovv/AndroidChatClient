package ru.mail.ruslan.androidchatclient.net;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.IOException;

import ru.mail.ruslan.androidchatclient.IRemoteService;

public class RemoteService extends Service implements SocketListener {

    public static final String ACTION_CONNECTED = "ACTION_CONNECTED";
    public static final String ACTION_CONNECTION_FAILED = "ACTION_CONNECTION_FAILED";
    public static final String ACTION_DATA_RECEIVED = "ACTION_DATA_RECEIVED";

    private static final String TAG = "RemoteService";
    private static final String HOST = "188.166.49.215";
    private static final int PORT = 7777;

    private SocketConnectionHandler mSocketConnectionHandler;
    private LocalBroadcastManager mLocalBroadcastManager;
    private RemoteServiceBinder mBinder;

    public class RemoteServiceBinder extends IRemoteService.Stub {
        @Override
        public void sendMessage(String message) throws RemoteException {
            try {
                mSocketConnectionHandler.sendData(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void connectToServer() {
        try {
            if (mSocketConnectionHandler != null) {
                mSocketConnectionHandler.stop();
                mSocketConnectionHandler = null;
            }
            mSocketConnectionHandler = new SocketConnectionHandler(HOST, PORT);
            mSocketConnectionHandler.addListener(this);
            Thread thread = new Thread(mSocketConnectionHandler);
            thread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate() started");
        super.onCreate();
        mBinder = new RemoteServiceBinder();
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
        connectToServer();
        Log.d(TAG, "onCreate() ended");
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy(): started");
        if (mSocketConnectionHandler != null) {
            Log.d(TAG, "onDestroy(): mSocketConnectionHandler.stop()");
            mSocketConnectionHandler.stop();
        }
        super.onDestroy();
        Log.d(TAG, "onDestroy(): ended");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onConnected() {
        Intent intent = new Intent(ACTION_CONNECTED);
        mLocalBroadcastManager.sendBroadcast(intent);
    }

    @Override
    public void onConnectionFailed() {
        Intent intent = new Intent(ACTION_CONNECTION_FAILED);
        mLocalBroadcastManager.sendBroadcast(intent);
    }

    @Override
    public void onDataReceived(String data) {
        // TODO обработать отключение сервера
        // TODO кодировать и декодировать сообщения в сервисе

        Intent intent = new Intent(ACTION_DATA_RECEIVED);
        intent.putExtra("data", data);
        mLocalBroadcastManager.sendBroadcast(intent);
    }
}
