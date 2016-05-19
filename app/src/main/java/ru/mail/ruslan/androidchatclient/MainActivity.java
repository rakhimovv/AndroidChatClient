package ru.mail.ruslan.androidchatclient;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import ru.mail.ruslan.androidchatclient.msg.BaseMessage;
import ru.mail.ruslan.androidchatclient.net.MyService;
import ru.mail.ruslan.androidchatclient.net.MyService.MyBinder;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private MyService mService;
    boolean mBound = false;

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            MyBinder binder = (MyBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            SplashFragment splashFragment = new SplashFragment();
            ft.replace(R.id.fragment_container, splashFragment, splashFragment.getTag());
            ft.commit();
        }
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart() started");
        super.onStart();
        // Bind to LocalService
        Intent intent = new Intent(this, MyService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

        /*
        while (!mBound) {
        }
        Log.e(TAG, "Sending hello");
        */
        //mService.sendData("HELLO");

        //startService(intent);
        Log.d(TAG, "onStart() ended");
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy() started");
        super.onDestroy();
        // Unbind from the service
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
        Log.d(TAG, "onDestroy() ended");
    }

    public void sendMessage(BaseMessage msg) {
    }
}