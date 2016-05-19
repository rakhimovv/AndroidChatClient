package ru.mail.ruslan.androidchatclient.net;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.net.ProtocolException;

import ru.mail.ruslan.androidchatclient.msg.BaseMessage;
import ru.mail.ruslan.androidchatclient.msg.response.WelcomeMessage;
import ru.mail.ruslan.androidchatclient.serialization.JsonProtocol;
import ru.mail.ruslan.androidchatclient.serialization.Protocol;

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

    private void connectToServer() {
        try {
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

        connectToServer();

        //sendData("hello");

        //sendData("aloha");

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

    }

    @Override
    public void onConnectionFailed() {

    }

    @Override
    public void onDataReceived(String data) {
        Log.e(TAG, "onDataReceived: " + data);

        JsonProtocol protocol = new JsonProtocol();

        try {
            //BaseMessage baseMessage = protocol.decode(data);
            Log.e(TAG, "baseMessage: " + protocol.decode(data).toString());
        } catch (ProtocolException e) {
            e.printStackTrace();
        }

        Log.e(TAG, "shutDown: " + mSocketConnectionHandler.isShutDown());
    }

    public void sendData(String data) {
        if (mSocketConnectionHandler != null) {
            mSocketConnectionHandler.sendData(data);
        }
        // TODO если пришла ошибка - переподключиться
    }
}
