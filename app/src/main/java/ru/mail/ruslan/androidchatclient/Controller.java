package ru.mail.ruslan.androidchatclient;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import java.lang.ref.WeakReference;

import ru.mail.ruslan.androidchatclient.encryption.HashUtil;
import ru.mail.ruslan.androidchatclient.msg.Action;
import ru.mail.ruslan.androidchatclient.msg.BaseMessage;
import ru.mail.ruslan.androidchatclient.msg.Status;
import ru.mail.ruslan.androidchatclient.msg.request.AuthRequestMessage;
import ru.mail.ruslan.androidchatclient.msg.request.ChannelListRequestMessage;
import ru.mail.ruslan.androidchatclient.msg.request.RegisterRequestMessage;
import ru.mail.ruslan.androidchatclient.msg.response.AuthResponseMessage;
import ru.mail.ruslan.androidchatclient.msg.response.ChannelListResponseMessage;
import ru.mail.ruslan.androidchatclient.msg.response.RegisterResponseMessage;
import ru.mail.ruslan.androidchatclient.msg.response.WelcomeMessage;

public final class Controller {

    private static final String TAG = "Controller";

    private WeakReference<MainActivity> mMainActivityWeakRef;
    public FragmentReplacer fragmentReplacer;
    public SharedPreferences mPrefs;

    public Controller(MainActivity mainActivity) {
        mMainActivityWeakRef = new WeakReference<>(mainActivity);
        fragmentReplacer = new FragmentReplacer(mMainActivityWeakRef, this);
        mPrefs = PreferenceManager.getDefaultSharedPreferences(mainActivity);
    }

    public void processResponse(BaseMessage message) {
        switch (message.getAction()) {
            case Action.AUTH: {
                processAuthMessage((AuthResponseMessage) message);
                break;
            }
            case Action.CHANNEL_LIST: {
                processChannelListMessage((ChannelListResponseMessage) message);
                break;
            }
            case Action.CREATE_CHANNEL: {
                break;
            }
            case Action.EVENT_ENTER: {
                break;
            }
            case Action.ENTER: {
                break;
            }
            case Action.EVENT_LEAVE: {
                break;
            }
            case Action.LEAVE: {
                break;
            }
            case Action.EVENT_MESSAGE: {
                break;
            }
            case Action.REGISTER: {
                processRegisterMessage((RegisterResponseMessage) message);
                break;
            }
            case Action.SET_USER_INFO: {
                break;
            }
            case Action.USER_INFO: {
                break;
            }
            case Action.WELCOME: {
                processWelcomeMessage((WelcomeMessage) message);
                break;
            }
        }
    }

    private void processWelcomeMessage(WelcomeMessage message) {
        MainActivity mainActivity = mMainActivityWeakRef.get();
        if (mainActivity == null) {
            return;
        }
        if (MyPreferences.isLoggedIn(mPrefs)) {
            AuthRequestMessage authRequestMessage = new AuthRequestMessage();
            authRequestMessage.login = MyPreferences.loadLogin(mPrefs);
            authRequestMessage.pass = MyPreferences.loadPass(mPrefs);
            mainActivity.sendMessage(authRequestMessage);
        } else {
            fragmentReplacer.showAuthFragment(false);
        }
    }

    private void processAuthMessage(AuthResponseMessage message) {
        MainActivity mainActivity = mMainActivityWeakRef.get();
        if (mainActivity == null) {
            return;
        }

        if (message.status != Status.ERR_OK) {
            MyPreferences.deleteAuthData(mPrefs);
        }

        switch (message.status) {
            case ERR_OK: {
                Log.d(TAG, "Successful authorization");
                MyPreferences.saveUserId(mPrefs, message.uid);
                MyPreferences.saveSessionId(mPrefs, message.sid);
                getChannelList(message.uid, message.sid);
                break;
            }
            case ERR_ALREADY_EXIST: {
                Toast.makeText(mainActivity.getApplicationContext(),
                        R.string.toast_login_already_exists, Toast.LENGTH_SHORT).show();
                mainActivity.connectToRemoteService();
                break;
            }
            case ERR_INVALID_PASS: {
                Toast.makeText(mainActivity.getApplicationContext(),
                        R.string.toast_invalid_pass, Toast.LENGTH_SHORT).show();
                mainActivity.connectToRemoteService();
                break;
            }
            case ERR_INVALID_DATA: {
                break;
            }
            case ERR_EMPTY_FIELD: {
                break;
            }
            case ERR_ALREADY_REGISTER: {
                break;
            }
            case ERR_NEED_AUTH: {
                break;
            }
            case ERR_NEED_REGISTER: {
                Toast.makeText(mainActivity.getApplicationContext(),
                        R.string.toast_need_register, Toast.LENGTH_SHORT).show();
                mainActivity.connectToRemoteService();
                break;
            }
            case ERR_USER_NOT_FOUND: {
                break;
            }
            case ERR_CHANNEL_NOT_FOUND: {
                break;
            }
            case ERR_INVALID_CHANNEL: {
                break;
            }
        }
    }

    private void processRegisterMessage(RegisterResponseMessage message) {
        MainActivity mainActivity = mMainActivityWeakRef.get();
        if (mainActivity == null) {
            return;
        }

        if (message.status != Status.ERR_OK) {
            MyPreferences.deleteAuthData(mPrefs);
        }

        switch (message.status) {
            case ERR_OK: {
                Log.d(TAG, "Successful registration");
                break;
            }
            case ERR_ALREADY_EXIST: {
                Toast.makeText(mainActivity.getApplicationContext(),
                        R.string.toast_login_already_exists, Toast.LENGTH_SHORT).show();
                mainActivity.connectToRemoteService();
                break;
            }
            case ERR_INVALID_PASS: {
                Toast.makeText(mainActivity.getApplicationContext(),
                        R.string.toast_invalid_pass, Toast.LENGTH_SHORT).show();
                mainActivity.connectToRemoteService();
                break;
            }
            case ERR_INVALID_DATA: {
                break;
            }
            case ERR_EMPTY_FIELD: {
                break;
            }
            case ERR_ALREADY_REGISTER: {
                Toast.makeText(mainActivity.getApplicationContext(),
                        R.string.toast_login_already_exists, Toast.LENGTH_SHORT).show();
                mainActivity.connectToRemoteService();
                break;
            }
            case ERR_NEED_AUTH: {
                break;
            }
            case ERR_NEED_REGISTER: {
                Toast.makeText(mainActivity.getApplicationContext(),
                        R.string.toast_need_register, Toast.LENGTH_SHORT).show();
                mainActivity.connectToRemoteService();
                break;
            }
            case ERR_USER_NOT_FOUND: {
                break;
            }
            case ERR_CHANNEL_NOT_FOUND: {
                break;
            }
            case ERR_INVALID_CHANNEL: {
                break;
            }
        }
    }

    private void processChannelListMessage(ChannelListResponseMessage message) {
        MainActivity mainActivity = mMainActivityWeakRef.get();
        if (mainActivity == null) {
            return;
        }

        switch (message.status) {
            case ERR_OK: {
                Log.d(TAG, "Successful getting channel list");
                fragmentReplacer.showChannelListFragment(message.channels, false);
                break;
            }
            case ERR_ALREADY_EXIST: {
                break;
            }
            case ERR_INVALID_PASS: {
                break;
            }
            case ERR_INVALID_DATA: {
                break;
            }
            case ERR_EMPTY_FIELD: {
                break;
            }
            case ERR_ALREADY_REGISTER: {
                break;
            }
            case ERR_NEED_AUTH: {
                fragmentReplacer.showAuthFragment(false);
                break;
            }
            case ERR_NEED_REGISTER: {
                fragmentReplacer.showAuthFragment(false);
                break;
            }
            case ERR_USER_NOT_FOUND: {
                break;
            }
            case ERR_CHANNEL_NOT_FOUND: {
                break;
            }
            case ERR_INVALID_CHANNEL: {
                break;
            }
        }
    }

    public void processConnectionFailed() {
        MainActivity mainActivity = mMainActivityWeakRef.get();
        if (mainActivity == null) {
            return;
        }

        fragmentReplacer.showSplashFragment(false);
        mainActivity.connectToRemoteService();
    }

    public void authorize(String login, String pass) {
        MainActivity mainActivity = mMainActivityWeakRef.get();
        if (mainActivity == null) {
            return;
        }

        AuthRequestMessage authMessage = new AuthRequestMessage();
        authMessage.login = login;
        authMessage.pass = HashUtil.md5(pass);

        MyPreferences.saveLogin(mPrefs, authMessage.login);
        MyPreferences.savePass(mPrefs, authMessage.pass);

        mainActivity.sendMessage(authMessage);
    }

    public void register(String login, String nick, String pass) {
        MainActivity mainActivity = mMainActivityWeakRef.get();
        if (mainActivity == null) {
            return;
        }

        RegisterRequestMessage registerMessage = new RegisterRequestMessage();
        registerMessage.login = login;
        registerMessage.nick = nick;
        registerMessage.pass = HashUtil.md5(pass);

        MyPreferences.saveLogin(mPrefs, registerMessage.login);
        MyPreferences.savePass(mPrefs, registerMessage.pass);

        mainActivity.sendMessage(registerMessage);
    }

    public void getChannelList(String userId, String sessionId) {
        MainActivity mainActivity = mMainActivityWeakRef.get();
        if (mainActivity == null) {
            return;
        }

        ChannelListRequestMessage channelListMessage = new ChannelListRequestMessage();
        channelListMessage.cid = userId;
        channelListMessage.sid = sessionId;
        mainActivity.sendMessage(channelListMessage);
    }


}
