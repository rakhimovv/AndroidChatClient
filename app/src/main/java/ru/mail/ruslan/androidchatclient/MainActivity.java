package ru.mail.ruslan.androidchatclient;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonSyntaxException;

import java.net.ProtocolException;

import ru.mail.ruslan.androidchatclient.msg.BaseMessage;
import ru.mail.ruslan.androidchatclient.net.RemoteService;
import ru.mail.ruslan.androidchatclient.serialization.JsonProtocol;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private IRemoteService mRemoteService;
    private boolean mRemoteServiceConnectedToServer;
    private JsonProtocol mJsonProtocol;
    private Controller mController;

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mRemoteService = IRemoteService.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mController.processConnectionFailed();
        }
    };

    private BroadcastReceiver mRemoteServiceReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case RemoteService.ACTION_CONNECTED: {
                    Log.e(TAG, "Successful connection to server");
                    Toast.makeText(getApplicationContext(), "Connected to server", Toast.LENGTH_SHORT).show();
                    mRemoteServiceConnectedToServer = true;
                    break;
                }
                case RemoteService.ACTION_CONNECTION_FAILED: {
                    Log.e(TAG, "Connection to server failed");
                    Toast.makeText(getApplicationContext(), "Connection to server failed", Toast.LENGTH_SHORT).show();

                    mRemoteServiceConnectedToServer = false;
                    mController.processConnectionFailed();
                    break;
                }
                case RemoteService.ACTION_DATA_RECEIVED: {
                    String data = intent.getStringExtra("data");
                    Log.e(TAG, "Message received: " + data);
                    try {
                        BaseMessage message = mJsonProtocol.decode(data);
                        if (message != null) {
                            mController.processResponse(message);
                        } else {
                            Log.e(TAG, "Message is null");
                            mRemoteServiceConnectedToServer = false;
                            mController.processConnectionFailed();
                        }
                    } catch (ProtocolException e) {
                        Log.e(TAG, "JsonProtocol exception: " + data);
                    } catch (JsonSyntaxException ex) {
                        Log.e(TAG, "JsonProtocolSyntax exception: " + data);
                    }
                    break;
                }
            }
        }
    };

    public boolean isInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager) this.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate()");
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, new SplashFragment(), SplashFragment.TAG);
            ft.commit();
        }

        if (isInternetConnection()) {
            mJsonProtocol = new JsonProtocol();
            mController = new Controller(this);
        } else {
            Toast.makeText(this.getApplicationContext(), "Internet connection is absent", Toast.LENGTH_SHORT).show();
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
                    Log.e(TAG, "Send message: " + messageData);
                    mRemoteService.sendMessage(messageData);
                } else {
                    Log.e(TAG, "Attempt to send unknown message for class " + message.getClass().getSimpleName());
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
        Log.e(TAG, "onResume()");
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
        manager.registerReceiver(mRemoteServiceReceiver, new IntentFilter(RemoteService.ACTION_CONNECTED));
        manager.registerReceiver(mRemoteServiceReceiver, new IntentFilter(RemoteService.ACTION_CONNECTION_FAILED));
        manager.registerReceiver(mRemoteServiceReceiver, new IntentFilter(RemoteService.ACTION_DATA_RECEIVED));

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG, "onStart()");
        if (isInternetConnection()) {
            connectToRemoteService();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "onPause()");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRemoteServiceReceiver);
    }

    @Override
    protected void onStop() {
        /*
        if (mController != null) {
            //MyPreferences.deleteAuthData(mController.mPrefs);
            //Log.e(TAG, "onStop(): Delete Auth Data");
        }
        */
        Log.e(TAG, "onStop()");
        disconnect();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy()");
    }
}