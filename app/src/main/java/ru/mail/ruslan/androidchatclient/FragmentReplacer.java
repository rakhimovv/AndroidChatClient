package ru.mail.ruslan.androidchatclient;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import java.lang.ref.WeakReference;
import java.util.List;

import ru.mail.ruslan.androidchatclient.msg.response.Channel;

public class FragmentReplacer {
    private WeakReference<MainActivity> mMainActivityWeakRef;
    private Controller controller;

    public FragmentReplacer(WeakReference<MainActivity> mainActivityWeakRef, Controller controller) {
        this.mMainActivityWeakRef = mainActivityWeakRef;
        this.controller = controller;
    }

    private void replaceFragment(MainActivity mainActivity, Fragment fragment, String tag, boolean addToBackStack) {
        FragmentTransaction ft = mainActivity.getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, fragment, tag);
        if (addToBackStack) {
            ft.addToBackStack(null);
        }
        ft.commit();
    }

    public void showSplashFragment(boolean addToBackStack) {
        MainActivity mainActivity = mMainActivityWeakRef.get();
        if (mainActivity == null) {
            return;
        }
        replaceFragment(mainActivity, new SplashFragment(),
                SplashFragment.TAG, addToBackStack);
    }

    public void showAuthFragment(boolean addToBackStack) {
        MainActivity mainActivity = mMainActivityWeakRef.get();
        if (mainActivity == null) {
            return;
        }
        replaceFragment(mainActivity, AuthFragment.newInstance(controller),
                AuthFragment.TAG, addToBackStack);
    }

    public void showChannelListFragment(List<Channel> channels, boolean addToBackStack) {
        MainActivity mainActivity = mMainActivityWeakRef.get();
        if (mainActivity == null) {
            return;
        }
        replaceFragment(mainActivity, ChannelListFragment.newInstance(controller, channels),
                ChannelListFragment.TAG, addToBackStack);
    }

    public void showChannelFragment(Channel channel, boolean addToBackStack) {
        MainActivity mainActivity = mMainActivityWeakRef.get();
        if (mainActivity == null) {
            return;
        }
        replaceFragment(mainActivity, ChannelFragment.newInstance(controller, channel),
                ChannelFragment.TAG, addToBackStack);
    }
}
