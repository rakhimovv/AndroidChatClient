package ru.mail.ruslan.androidchatclient;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.Toast;

import java.lang.ref.WeakReference;

import ru.mail.ruslan.androidchatclient.encryption.HashUtil;
import ru.mail.ruslan.androidchatclient.msg.Action;
import ru.mail.ruslan.androidchatclient.msg.BaseMessage;
import ru.mail.ruslan.androidchatclient.msg.request.AuthRequestMessage;
import ru.mail.ruslan.androidchatclient.msg.request.ChannelListRequestMessage;
import ru.mail.ruslan.androidchatclient.msg.request.CreateChannelRequestMessage;
import ru.mail.ruslan.androidchatclient.msg.request.EnterRequestMessage;
import ru.mail.ruslan.androidchatclient.msg.request.LeaveRequestMessage;
import ru.mail.ruslan.androidchatclient.msg.request.RegisterRequestMessage;
import ru.mail.ruslan.androidchatclient.msg.request.SendRequestMessage;
import ru.mail.ruslan.androidchatclient.msg.request.SetUserInfoRequestMessage;
import ru.mail.ruslan.androidchatclient.msg.request.UserInfoRequestMessage;
import ru.mail.ruslan.androidchatclient.msg.response.AuthResponseMessage;
import ru.mail.ruslan.androidchatclient.msg.response.Channel;
import ru.mail.ruslan.androidchatclient.msg.response.ChannelListResponseMessage;
import ru.mail.ruslan.androidchatclient.msg.response.ChannelMessage;
import ru.mail.ruslan.androidchatclient.msg.response.EnterEventMessage;
import ru.mail.ruslan.androidchatclient.msg.response.EnterResponseMessage;
import ru.mail.ruslan.androidchatclient.msg.response.LeaveEventMessage;
import ru.mail.ruslan.androidchatclient.msg.response.MessageEventMessage;
import ru.mail.ruslan.androidchatclient.msg.response.UserInfoResponseMessage;

public final class Controller {

    private static final String TAG = "Controller";

    private WeakReference<MainActivity> mMainActivityWeakRef;
    public FragmentReplacer fragmentReplacer;
    public SharedPreferences mPrefs;

    // FIXME This field needs because server don't send channel_id in enter response
    private String mLastEnterChannelId;
    // FIXME This field needs because server don't send channel_id in leave response
    private String mLastLeaveChannelId;

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
                Toast.makeText(mainActivity.getApplicationContext(),
                        R.string.toast_user_not_found, Toast.LENGTH_SHORT).show();
                mainActivity.connectToRemoteService();
                return;
            }
            case ERR_CHANNEL_NOT_FOUND: {
                Toast.makeText(mainActivity.getApplicationContext(),
                        R.string.toast_channel_not_found, Toast.LENGTH_SHORT).show();
                mainActivity.connectToRemoteService();
                return;
            }
            case ERR_INVALID_CHANNEL: {
                Toast.makeText(mainActivity.getApplicationContext(),
                        R.string.toast_user_out_of_channel, Toast.LENGTH_SHORT).show();
                mainActivity.connectToRemoteService();
                return;
            }
        }

        switch (message.getAction()) {
            case Action.AUTH: {
                //Log.e(TAG, "Successful authorization");
                MyPreferences.saveUserId(mPrefs, ((AuthResponseMessage) message).cid);
                MyPreferences.saveSessionId(mPrefs, ((AuthResponseMessage) message).sid);
                getChannelList(((AuthResponseMessage) message).cid, ((AuthResponseMessage) message).sid);
                break;
            }
            case Action.CHANNEL_LIST: {
                //Log.d(TAG, "Successful getting channel list");
                fragmentReplacer.showChannelListFragment(
                        ((ChannelListResponseMessage) message).channels,
                        MyPreferences.loadUserId(mPrefs),
                        MyPreferences.loadSessionId(mPrefs),
                        false);
                break;
            }
            case Action.CREATE_CHANNEL: {
                //Logger.d("Successful create channel");
                Toast.makeText(mainActivity, R.string.toast_channel_created, Toast.LENGTH_SHORT).show();
                String userId = MyPreferences.loadUserId(mPrefs);
                String sessionId = MyPreferences.loadSessionId(mPrefs);
                getChannelList(userId, sessionId);
                break;
            }
            case Action.EVENT_ENTER: {
                FragmentManager fm = mainActivity.getSupportFragmentManager();
                ChannelListFragment channelListFragment =
                        (ChannelListFragment) fm.findFragmentByTag(ChannelListFragment.TAG);
                if (channelListFragment != null) {
                    channelListFragment.processEnterChannel(((EnterEventMessage) message).uid, ((EnterEventMessage) message).chid);
                }
                break;
            }
            case Action.ENTER: {
                fragmentReplacer.showChannelFragment(
                        ((EnterResponseMessage) message).users,
                        ((EnterResponseMessage) message).lastMsg,
                        MyPreferences.loadUserId(mPrefs),
                        MyPreferences.loadSessionId(mPrefs),
                        mLastEnterChannelId,
                        true);
                break;
            }
            case Action.EVENT_LEAVE: {
                FragmentManager fm = mainActivity.getSupportFragmentManager();
                ChannelListFragment channelListFragment =
                        (ChannelListFragment) fm.findFragmentByTag(ChannelListFragment.TAG);
                if (channelListFragment != null) {
                    channelListFragment.processLeaveChannel(((LeaveEventMessage) message).uid, ((LeaveEventMessage) message).chid);
                }
                break;
            }
            case Action.LEAVE: {
                FragmentManager fm = mainActivity.getSupportFragmentManager();
                ChannelListFragment channelListFragment =
                        (ChannelListFragment) fm.findFragmentByTag(ChannelListFragment.TAG);
                if (channelListFragment != null) {
                    channelListFragment.processLeaveChannel(MyPreferences.loadUserId(mPrefs), mLastLeaveChannelId);
                }
                break;
            }
            case Action.EVENT_MESSAGE: {
                //Log.e(TAG, "ОБРАБАТЫВАЕМ СООБЩЕНИЕ");
                Log.e(TAG, "Process message event (" + ((MessageEventMessage) message).chid + "): " + ((MessageEventMessage) message).body);

                FragmentManager fm = mainActivity.getSupportFragmentManager();
                ChannelFragment channelFragment =
                        (ChannelFragment) fm.findFragmentByTag(ChannelFragment.tag(((MessageEventMessage) message).chid));
                if (channelFragment != null) {
                    channelFragment.processMessage(((MessageEventMessage) message).chid, new ChannelMessage((MessageEventMessage) message));
                }
                break;
            }
            case Action.REGISTER: {
                // При успешной регистрации сразу приходит сообщение об авторизации, поэтому
                // больше нечего обрабатывать
                //Log.e(TAG, "Successful registration");
                Toast.makeText(mainActivity, R.string.successfull_register, Toast.LENGTH_SHORT).show();
                break;
            }
            case Action.SET_USER_INFO: {
                Toast.makeText(mainActivity, R.string.toast_info_changed, Toast.LENGTH_SHORT).show();
                break;
            }
            case Action.USER_INFO: {
                fragmentReplacer.showUserInfoFragment(((UserInfoResponseMessage) message).nick, ((UserInfoResponseMessage) message).userStatus, true);
                break;
            }
            case Action.WELCOME: {
                MyPreferences.deleteAuthData(mPrefs);
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
            case Action.MESSAGE: {
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

    public void enterChannel(Channel channel, String userId, String sessionId) {
        MainActivity mainActivity = mMainActivityWeakRef.get();
        if (mainActivity == null) {
            return;
        }

        if (userId == null || sessionId == null || channel == null) {
            Toast.makeText(mainActivity.getApplicationContext(),
                    R.string.toast_unable_channel_enter, Toast.LENGTH_SHORT).show();
            return;
        }

        mLastEnterChannelId = channel.chid;

        EnterRequestMessage enterMessage = new EnterRequestMessage();
        enterMessage.cid = userId;
        enterMessage.sid = sessionId;
        enterMessage.channel = channel.chid;
        mainActivity.sendMessage(enterMessage);
    }

    public void leaveChannel(Channel channel, String userId, String sessionId) {
        MainActivity mainActivity = mMainActivityWeakRef.get();
        if (mainActivity == null) {
            return;
        }

        mLastLeaveChannelId = channel.chid;

        LeaveRequestMessage leaveMessage = new LeaveRequestMessage();
        leaveMessage.cid = userId;
        leaveMessage.sid = sessionId;
        leaveMessage.channel = channel.chid;
        mainActivity.sendMessage(leaveMessage);
    }

    public void leaveAllChannels(String userId, String sessionId) {
        MainActivity mainActivity = mMainActivityWeakRef.get();
        if (mainActivity == null) {
            return;
        }

        LeaveRequestMessage leaveMessage = new LeaveRequestMessage();
        leaveMessage.cid = userId;
        leaveMessage.sid = sessionId;
        leaveMessage.channel = "*";
        mainActivity.sendMessage(leaveMessage);
    }

    public void send(String body, String channel, String userId, String sessionId) {
        MainActivity mainActivity = mMainActivityWeakRef.get();
        if (mainActivity == null) {
            return;
        }

        SendRequestMessage sendMessage = new SendRequestMessage();
        sendMessage.body = body;
        sendMessage.channel = channel;
        sendMessage.cid = userId;
        sendMessage.sid = sessionId;
        mainActivity.sendMessage(sendMessage);
    }

    public void createChannel(String channelName, String channelDescription, String userId, String sessionId) {
        MainActivity mainActivity = mMainActivityWeakRef.get();
        if (mainActivity == null) {
            return;
        }

        CreateChannelRequestMessage createChannelMessage = new CreateChannelRequestMessage();
        createChannelMessage.name = channelName;
        createChannelMessage.descr = channelDescription;
        createChannelMessage.cid = userId;
        createChannelMessage.sid = sessionId;
        mainActivity.sendMessage(createChannelMessage);
    }

    public void changeUserInfo(String status, String userId, String sessionId) {
        MainActivity mainActivity = mMainActivityWeakRef.get();
        if (mainActivity == null) {
            return;
        }

        SetUserInfoRequestMessage setUserInfoMessage = new SetUserInfoRequestMessage();
        setUserInfoMessage.userStatus = status;
        setUserInfoMessage.cid = userId;
        setUserInfoMessage.sid = sessionId;
        mainActivity.sendMessage(setUserInfoMessage);
    }

    public void getUserInfo(String requestedUserId, String userId, String sessionId) {
        MainActivity mainActivity = mMainActivityWeakRef.get();
        if (mainActivity == null) {
            return;
        }

        //showLoadingDialog();

        UserInfoRequestMessage userInfoMessage = new UserInfoRequestMessage();
        userInfoMessage.user = requestedUserId;
        userInfoMessage.cid = userId;
        userInfoMessage.sid = sessionId;
        mainActivity.sendMessage(userInfoMessage);
    }
}
