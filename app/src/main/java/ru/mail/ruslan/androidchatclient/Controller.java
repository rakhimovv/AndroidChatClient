package ru.mail.ruslan.androidchatclient;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import java.lang.ref.WeakReference;

import ru.mail.ruslan.androidchatclient.encryption.HashUtil;
import ru.mail.ruslan.androidchatclient.msg.Action;
import ru.mail.ruslan.androidchatclient.msg.BaseMessage;
import ru.mail.ruslan.androidchatclient.msg.request.AuthRequestMessage;
import ru.mail.ruslan.androidchatclient.msg.request.ChannelListRequestMessage;
import ru.mail.ruslan.androidchatclient.msg.request.RegisterRequestMessage;
import ru.mail.ruslan.androidchatclient.msg.response.AuthResponseMessage;
import ru.mail.ruslan.androidchatclient.msg.response.ChannelListResponseMessage;

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

    public void processConnectionFailed() {
        MainActivity mainActivity = mMainActivityWeakRef.get();
        if (mainActivity == null) {
            return;
        }

        fragmentReplacer.showSplashFragment(false);
        mainActivity.connectToRemoteService();
    }

    public void processResponse(BaseMessage message) {
        MainActivity mainActivity = mMainActivityWeakRef.get();
        if (mainActivity == null) {
            return;
        }

        switch (message.getStatus()) {
            case ERR_ALREADY_EXIST: {
                Toast.makeText(mainActivity.getApplicationContext(),
                        R.string.toast_login_already_exists, Toast.LENGTH_SHORT).show();
                MyPreferences.deleteAuthData(mPrefs);
                mainActivity.connectToRemoteService();
                return;
            }
            case ERR_INVALID_PASS: {
                Toast.makeText(mainActivity.getApplicationContext(),
                        R.string.toast_invalid_login_or_pass, Toast.LENGTH_SHORT).show();
                MyPreferences.deleteAuthData(mPrefs);
                mainActivity.connectToRemoteService();
                return;
            }
            case ERR_INVALID_DATA: {
                Toast.makeText(mainActivity.getApplicationContext(),
                        R.string.toast_invalid_json, Toast.LENGTH_SHORT).show();
                MyPreferences.deleteAuthData(mPrefs);
                mainActivity.connectToRemoteService();
                return;
            }
            case ERR_EMPTY_FIELD: {
                Toast.makeText(mainActivity.getApplicationContext(),
                        R.string.toast_empty_login_or_pass_or_channel, Toast.LENGTH_SHORT).show();
                MyPreferences.deleteAuthData(mPrefs);
                mainActivity.connectToRemoteService();
                return;
            }
            case ERR_ALREADY_REGISTER: {
                Toast.makeText(mainActivity.getApplicationContext(),
                        R.string.toast_login_already_exists, Toast.LENGTH_SHORT).show();
                MyPreferences.deleteAuthData(mPrefs);
                mainActivity.connectToRemoteService();
                return;
            }
            case ERR_NEED_AUTH: {
                Toast.makeText(mainActivity.getApplicationContext(),
                        R.string.toast_need_auth, Toast.LENGTH_SHORT).show();
                MyPreferences.deleteAuthData(mPrefs);
                fragmentReplacer.showAuthFragment(false);
                mainActivity.connectToRemoteService();
                return;
            }
            case ERR_NEED_REGISTER: {
                Toast.makeText(mainActivity.getApplicationContext(),
                        R.string.toast_need_register, Toast.LENGTH_SHORT).show();
                MyPreferences.deleteAuthData(mPrefs);
                fragmentReplacer.showAuthFragment(false);
                mainActivity.connectToRemoteService();
                return;
            }
            case ERR_USER_NOT_FOUND: {
                mainActivity.connectToRemoteService();
                return;
            }
            case ERR_CHANNEL_NOT_FOUND: {
                mainActivity.connectToRemoteService();
                return;
            }
            case ERR_INVALID_CHANNEL: {
                mainActivity.connectToRemoteService();
                return;
            }
        }

        // Ошибки обработаны ранее
        Log.e(TAG, "No errors");

        switch (message.getAction()) {
            case Action.AUTH: {
                Log.e(TAG, "Successful authorization");
                MyPreferences.saveUserId(mPrefs, ((AuthResponseMessage) message).uid);
                MyPreferences.saveSessionId(mPrefs, ((AuthResponseMessage) message).sid);
                getChannelList(((AuthResponseMessage) message).uid, ((AuthResponseMessage) message).sid);
                break;
            }
            case Action.CHANNEL_LIST: {
                Log.d(TAG, "Successful getting channel list");
                fragmentReplacer.showChannelListFragment(((ChannelListResponseMessage) message).channels, false);
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
                // При успешной регистрации сразу приходит сообщение об авторизации, поэтому
                // больше нечего обрабатывать
                Log.e(TAG, "Successful registration");
                break;
            }
            case Action.SET_USER_INFO: {
                break;
            }
            case Action.USER_INFO: {
                break;
            }
            case Action.WELCOME: {
                if (MyPreferences.isLoggedIn(mPrefs)) {
                    AuthRequestMessage authRequestMessage = new AuthRequestMessage();
                    authRequestMessage.login = MyPreferences.loadLogin(mPrefs);
                    authRequestMessage.pass = MyPreferences.loadPass(mPrefs);
                    mainActivity.sendMessage(authRequestMessage);
                } else {
                    fragmentReplacer.showAuthFragment(false);
                }
                break;
            }
        }
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
