package ru.mail.ruslan.androidchatclient;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import java.lang.ref.WeakReference;
import java.util.List;

import ru.mail.ruslan.androidchatclient.msg.response.Channel;
import ru.mail.ruslan.androidchatclient.msg.response.LastMessage;
import ru.mail.ruslan.androidchatclient.msg.response.User;

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

    public void showChannelListFragment(List<Channel> channels, String userId, String sessionId, boolean addToBackStack) {
        MainActivity mainActivity = mMainActivityWeakRef.get();
        if (mainActivity == null) {
            return;
        }
        replaceFragment(mainActivity, ChannelListFragment.newInstance(controller, userId, sessionId, channels),
                ChannelListFragment.TAG, addToBackStack);
    }

    public void showChannelFragment(List<User> users,
                                    List<LastMessage> lastMessages,
                                    String userId,
                                    String sessionId,
                                    String channelId,
                                    boolean addToBackStack) {
        MainActivity mainActivity = mMainActivityWeakRef.get();
        if (mainActivity == null) {
            return;
        }
        replaceFragment(
                mainActivity,
                ChannelFragment.newInstance(
                        controller,
                        userId,
                        sessionId,
                        channelId,
                        users,
                        lastMessages
                ),
                ChannelFragment.tag(channelId),
                addToBackStack
        );
    }

    public void showChangeUserInfoFragment(boolean addToBackStack) {
        MainActivity mainActivity = mMainActivityWeakRef.get();
        if (mainActivity == null) {
            return;
        }

        String userId = MyPreferences.loadUserId(controller.mPrefs);
        String sessionId = MyPreferences.loadSessionId(controller.mPrefs);
        String login = MyPreferences.loadLogin(controller.mPrefs);
        replaceFragment(mainActivity, ChangeInfoFragment.newInstance(controller, userId, sessionId, login),
                ChangeInfoFragment.tag(), addToBackStack);
    }

    public void showCreateChannelFragment(boolean addToBackStack) {
        MainActivity mainActivity = mMainActivityWeakRef.get();
        if (mainActivity == null) {
            return;
        }

        String userId = MyPreferences.loadUserId(controller.mPrefs);
        String sessionId = MyPreferences.loadSessionId(controller.mPrefs);
        replaceFragment(mainActivity, CreateChannelFragment.newInstance(controller, userId, sessionId),
                CreateChannelFragment.tag(), addToBackStack);
    }

    public void showUserListFragment(List<User> users,
                                     boolean addToBackStack) {
        MainActivity mainActivity = mMainActivityWeakRef.get();
        if (mainActivity == null) {
            return;
        }

        String userId = MyPreferences.loadUserId(controller.mPrefs);
        String sessionId = MyPreferences.loadSessionId(controller.mPrefs);
        replaceFragment(mainActivity, UserListFragment.newInstance(controller,
                        userId, sessionId, users),
                UserListFragment.tag(), addToBackStack);
    }

    public void showUserInfoFragment(String nickname,
                                     String status,
                                     boolean addToBackStack) {
        MainActivity mainActivity = mMainActivityWeakRef.get();
        if (mainActivity == null) {
            return;
        }

        replaceFragment(mainActivity, UserInfoFragment.newInstance(nickname, status),
                UserInfoFragment.tag(), addToBackStack);
    }
}
