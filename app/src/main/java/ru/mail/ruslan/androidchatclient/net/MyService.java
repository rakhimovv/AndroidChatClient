package ru.mail.ruslan.androidchatclient.net;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Messenger;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import ru.mail.ruslan.androidchatclient.msg.Message;

public class MyService extends Service implements SocketListener {

    private static final String TAG = "MyService";
    private static final String HOST = "188.166.49.215";
    private static final int PORT = 7777;

    private final IBinder mBinder = new MyBinder();
    private SocketConnectionHandler mSocketConnectionHandler;

    public class MyBinder extends Binder {
        public MyService getService() {
            // Return this instance of LocalService so clients can call public methods
            return MyService.this;
        }
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate() started");
        // TODO ASYNC TASK
        super.onCreate();
        try {
            mSocketConnectionHandler = new SocketConnectionHandler(HOST, PORT);
            mSocketConnectionHandler.addListener(this);
            mSocketConnectionHandler.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "onCreate() ended");
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy() started");
        if (mSocketConnectionHandler != null) {
            mSocketConnectionHandler.stop();
        }
        super.onDestroy();
        Log.d(TAG, "onDestroy() ended");
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

    }

    @Override
    public void onConnectionFailed() {

    }

    @Override
    public void onDataReceived(Message msg) {

    }
}
