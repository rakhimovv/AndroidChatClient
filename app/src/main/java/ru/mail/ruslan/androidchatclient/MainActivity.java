package ru.mail.ruslan.androidchatclient;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.net.ProtocolException;

import ru.mail.ruslan.androidchatclient.msg.BaseMessage;
import ru.mail.ruslan.androidchatclient.net.RemoteService;
import ru.mail.ruslan.androidchatclient.serialization.JsonProtocol;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private IRemoteService mRemoteService;
    private boolean mRemoteServiceConnectedToServer;
    private JsonProtocol mJsonProtocol;

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "Connected to service");
            mRemoteService = IRemoteService.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "Service is disconnected");
            //mController.processConnectionFailed();
        }
    };

    private BroadcastReceiver mRemoteServiceReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case RemoteService.ACTION_CONNECTED: {
                    Log.d(TAG, "RemoteService is connected to Server");
                    mRemoteServiceConnectedToServer = true;
                    break;
                }
                case RemoteService.ACTION_CONNECTION_FAILED: {
                    Log.d(TAG, "RemoteService's connection to Server failed");
                    mRemoteServiceConnectedToServer = false;
                    //mController.processConnectionFailed();
                    //Toast.makeText(getApplicationContext(), R.string.toast_connection_failed, Toast.LENGTH_SHORT).show();
                    break;
                }
                case RemoteService.ACTION_DATA_RECEIVED: {
                    String data = intent.getStringExtra("data");
                    Log.e(TAG, "Message received: " + data);
                    //BaseMessage message = mMessageParser.parseMessage(data);
                    try {
                        BaseMessage message = mJsonProtocol.decode(data);
                        if (message != null) {
                            //mController.processResponse(message);
                        } else {
                            Log.e(TAG, "Message is null");
                            mRemoteServiceConnectedToServer = false;
                            //mController.processConnectionFailed();
                        }
                    } catch (ProtocolException e) {
                        Log.e(TAG, "JsonProtocol exception: " + data);
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mJsonProtocol = new JsonProtocol();
        //mController = new Controller(this);

        if (savedInstanceState == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, new SplashFragment(), SplashFragment.TAG);
            ft.commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    public void sendMessage(BaseMessage message) {
        if (mRemoteServiceConnectedToServer) {
            try {
                String messageData = mJsonProtocol.encode(message);
                if (messageData != null) {
                    Log.d(TAG, "Send message: " + messageData);
                    mRemoteService.sendMessage(messageData);
                } else {
                    Log.d(TAG, "Attempt to send unknown message for class " + message.getClass().getSimpleName());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void connectToRemoteService() {
        disconnect();
        Intent intent = new Intent(MainActivity.this, RemoteService.class);
        bindService(intent, mServiceConnection, BIND_AUTO_CREATE);
    }

    private void disconnect() {
        if (mRemoteService != null) {
            unbindService(mServiceConnection);
            mRemoteService = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
        manager.registerReceiver(mRemoteServiceReceiver, new IntentFilter(RemoteService.ACTION_CONNECTED));
        manager.registerReceiver(mRemoteServiceReceiver, new IntentFilter(RemoteService.ACTION_CONNECTION_FAILED));
        manager.registerReceiver(mRemoteServiceReceiver, new IntentFilter(RemoteService.ACTION_DATA_RECEIVED));
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart() started");
        super.onStart();
        connectToRemoteService();
        Log.d(TAG, "onStart() ended");
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRemoteServiceReceiver);
    }

    @Override
    protected void onStop() {
        disconnect();
        super.onStop();
    }
}