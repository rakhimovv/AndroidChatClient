package ru.mail.ruslan.androidchatclient.serialization;

import android.util.Log;

import com.google.gson.*;

import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.List;

import ru.mail.ruslan.androidchatclient.msg.Action;
import ru.mail.ruslan.androidchatclient.msg.BaseMessage;
import ru.mail.ruslan.androidchatclient.msg.response.AuthResponseMessage;
import ru.mail.ruslan.androidchatclient.msg.response.Channel;
import ru.mail.ruslan.androidchatclient.msg.response.ChannelListResponseMessage;
import ru.mail.ruslan.androidchatclient.msg.response.CreateChannelResponseMessage;
import ru.mail.ruslan.androidchatclient.msg.response.EnterEventMessage;
import ru.mail.ruslan.androidchatclient.msg.response.EnterResponseMessage;
import ru.mail.ruslan.androidchatclient.msg.response.LastMessage;
import ru.mail.ruslan.androidchatclient.msg.response.LeaveEventMessage;
import ru.mail.ruslan.androidchatclient.msg.response.LeaveResponseMessage;
import ru.mail.ruslan.androidchatclient.msg.response.MessageEventMessage;
import ru.mail.ruslan.androidchatclient.msg.response.RegisterResponseMessage;
import ru.mail.ruslan.androidchatclient.msg.response.SetUserInfoResponseMessage;
import ru.mail.ruslan.androidchatclient.msg.response.User;
import ru.mail.ruslan.androidchatclient.msg.response.UserInfoResponseMessage;
import ru.mail.ruslan.androidchatclient.msg.response.WelcomeMessage;

public class JsonProtocol implements Protocol {

    private static final String TAG = "JsonProtocol";

    JsonParser jsonParser;

    public JsonProtocol() {
        jsonParser = new JsonParser();
    }

    @Override
    public String encode(BaseMessage data) throws ProtocolException {
        // builder
        Log.d(TAG, "Build message for action: " + data.getAction());

        switch (data.getAction()) {
            case Action.AUTH: {
                //return buildAuthMessage((AuthRequestMessage) data);
            }
            case Action.CHANNEL_LIST: {
                //return buildChannelListMessage((ChannelListRequestMessage) data);
            }
            case Action.CREATE_CHANNEL: {
                //return buildCreateChannelMessage((CreateChannelRequestMessage) data);
            }
            case Action.ENTER: {
                //return buildEnterMessage((EnterRequestMessage) data);
            }
            case Action.LEAVE: {
                //return buildLeaveMessage((LeaveRequestMessage) data);
            }
            case Action.REGISTER: {
                //return buildRegisterMessage((RegisterRequestMessage) data);
            }
            case Action.SEND_MESSAGE: {
                //return buildSendMessage((SendRequestMessage) data);
            }
            case Action.SET_USER_INFO: {
                //return buildSetUserInfoMessage((SetUserInfoRequestMessage) data);
            }
            case Action.USER_INFO: {
                //return buildUserInfoMessage((UserInfoRequestMessage) data);
            }
        }
        return null;
    }

    @Override
    public BaseMessage decode(String stringData) throws ProtocolException {
        // parser
        if (stringData == null) {
            return null;
        }

        JsonObject json = jsonParser.parse(stringData.trim()).getAsJsonObject();
        if (!json.has("action")) {
            return null;
        }

        String action = json.get("action").getAsString();

        JsonObject data = null;

        if (json.has("data")) {
            Log.e(TAG, "json.has(\"data\")");
            data = json.get("data").getAsJsonObject();
        }

        switch (action) {
            case Action.AUTH: {
                return new AuthResponseMessage(
                        data.get("status").getAsInt(),
                        data.get("error").getAsString(),
                        data.get("sid").getAsString(),
                        data.get("uid").getAsString()
                );
            }
            case Action.CHANNEL_LIST: {
                List<Channel> channelList = new ArrayList<>();
                if (data.has("channels")) {
                    JsonArray channels = data.get("channels").getAsJsonArray();
                    for (int i = 0; i < channels.size(); i++) {
                        JsonObject c = channels.get(i).getAsJsonObject();
                        Channel channel = new Channel(
                                c.get("chid").getAsString(),
                                c.get("name").getAsString(),
                                c.get("descr").getAsString(),
                                c.get("online").getAsInt()
                        );
                        channelList.add(channel);
                    }
                }
                return new ChannelListResponseMessage(
                        data.get("status").getAsInt(),
                        data.get("error").getAsString(),
                        channelList
                );
            }
            case Action.CREATE_CHANNEL: {
                return new CreateChannelResponseMessage(
                        data.get("status").getAsInt(),
                        data.get("error").getAsString(),
                        data.get("chid").getAsString()
                );
            }
            case Action.EVENT_ENTER: {
                //return parseEnterEventMessage(json);
                return new EnterEventMessage(
                        data.get("chid").getAsString(),
                        data.get("uid").getAsString(),
                        data.get("nick").getAsString()
                );
            }
            case Action.ENTER: {
                List<User> userList = new ArrayList<>();
                if (data.has("users")) {
                    JsonArray users = data.get("users").getAsJsonArray();
                    for (int i = 0; i < users.size(); i++) {
                        JsonObject u = users.get(i).getAsJsonObject();
                        User user = new User(
                                u.get("uid").getAsString(),
                                u.get("nick").getAsString()
                        );
                        userList.add(user);
                    }
                }
                List<LastMessage> lastMessageList = new ArrayList<>();
                if (data.has("last_msg")) {
                    JsonArray lastMessages = data.get("last_msg").getAsJsonArray();
                    for (int i = 0; i < lastMessages.size(); i++) {
                        JsonObject l = lastMessages.get(i).getAsJsonObject();
                        LastMessage lastMessage = new LastMessage(
                                l.get("mid").getAsString(),
                                l.get("from").getAsString(),
                                l.get("nick").getAsString(),
                                l.get("body").getAsString(),
                                l.get("time").getAsLong()
                        );
                        lastMessageList.add(lastMessage);
                    }
                }
                return new EnterResponseMessage(
                        data.get("status").getAsInt(),
                        data.get("error").getAsString(),
                        userList,
                        lastMessageList
                );
            }
            case Action.EVENT_LEAVE: {
                return new LeaveEventMessage(
                        data.get("chid").getAsString(),
                        data.get("uid").getAsString(),
                        data.get("nick").getAsString()
                );
            }
            case Action.LEAVE: {
                return new LeaveResponseMessage(
                        data.get("status").getAsInt(),
                        data.get("error").getAsString()
                );
            }
            case Action.EVENT_MESSAGE: {
                return new MessageEventMessage(
                        data.get("chid").getAsString(),
                        data.get("from").getAsString(),
                        data.get("nick").getAsString(),
                        data.get("body").getAsString()
                );
            }
            case Action.REGISTER: {
                return new RegisterResponseMessage(
                        data.get("status").getAsInt(),
                        data.get("error").getAsString()
                );
            }
            case Action.SET_USER_INFO: {
                return new SetUserInfoResponseMessage(
                        data.get("status").getAsInt(),
                        data.get("error").getAsString()
                );
            }
            case Action.USER_INFO: {
                return new UserInfoResponseMessage(
                        data.get("status").getAsInt(),
                        data.get("error").getAsString(),
                        data.get("nick").getAsString(),
                        data.get("user_status").getAsString()
                );
            }
            case Action.WELCOME: {
                Log.e(TAG, "Action.Welcome");
                return new WelcomeMessage(
                        json.get("message").getAsString(),
                        json.get("time").getAsLong()
                );
            }
        }

        return null;
    }
}
