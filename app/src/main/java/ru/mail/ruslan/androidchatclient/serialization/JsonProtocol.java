package ru.mail.ruslan.androidchatclient.serialization;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
import ru.mail.ruslan.androidchatclient.msg.response.CreateChannelResponseMessage;
import ru.mail.ruslan.androidchatclient.msg.response.EnterEventMessage;
import ru.mail.ruslan.androidchatclient.msg.response.EnterResponseMessage;
import ru.mail.ruslan.androidchatclient.msg.response.LastMessage;
import ru.mail.ruslan.androidchatclient.msg.response.LeaveEventMessage;
import ru.mail.ruslan.androidchatclient.msg.response.LeaveResponseMessage;
import ru.mail.ruslan.androidchatclient.msg.response.MessageEventMessage;
import ru.mail.ruslan.androidchatclient.msg.response.MessageMessage;
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
    public String encode(BaseMessage msg) throws ProtocolException {
        // builder

        JsonObject json = new JsonObject();
        JsonObject data = new JsonObject();

        switch (msg.getAction()) {
            case Action.AUTH: {
                data.addProperty("login", ((AuthRequestMessage) msg).login);
                data.addProperty("pass", ((AuthRequestMessage) msg).pass);
                json.addProperty("action", Action.AUTH);
                json.add("data", data);
                return json.toString();
            }
            case Action.CHANNEL_LIST: {
                data.addProperty("cid", ((ChannelListRequestMessage) msg).cid);
                data.addProperty("cid", ((ChannelListRequestMessage) msg).sid);
                json.addProperty("action", Action.CHANNEL_LIST);
                json.add("data", data);
                return json.toString();
            }
            case Action.CREATE_CHANNEL: {
                data.addProperty("cid", ((CreateChannelRequestMessage) msg).cid);
                data.addProperty("sid", ((CreateChannelRequestMessage) msg).sid);
                data.addProperty("name", ((CreateChannelRequestMessage) msg).name);
                data.addProperty("descr", ((CreateChannelRequestMessage) msg).descr);
                json.addProperty("action", Action.CREATE_CHANNEL);
                json.add("data", data);
                return json.toString();
            }
            case Action.ENTER: {
                data.addProperty("cid", ((EnterRequestMessage) msg).cid);
                data.addProperty("sid", ((EnterRequestMessage) msg).sid);
                data.addProperty("channel", ((EnterRequestMessage) msg).channel);
                json.addProperty("action", Action.ENTER);
                json.add("data", data);
                return json.toString();
            }
            case Action.LEAVE: {
                data.addProperty("cid", ((LeaveRequestMessage) msg).cid);
                data.addProperty("sid", ((LeaveRequestMessage) msg).sid);
                data.addProperty("channel", ((LeaveRequestMessage) msg).channel);
                json.addProperty("action", Action.LEAVE);
                json.add("data", data);
                return json.toString();
            }
            case Action.REGISTER: {
                data.addProperty("login", ((RegisterRequestMessage) msg).login);
                data.addProperty("pass", ((RegisterRequestMessage) msg).pass);
                data.addProperty("nick", ((RegisterRequestMessage) msg).nick);
                json.addProperty("action", Action.REGISTER);
                json.add("data", data);
                return json.toString();
            }
            case Action.SEND_MESSAGE: {
                data.addProperty("cid", ((SendRequestMessage) msg).cid);
                data.addProperty("sid", ((SendRequestMessage) msg).sid);
                data.addProperty("channel", ((SendRequestMessage) msg).channel);
                data.addProperty("body", ((SendRequestMessage) msg).body);
                json.addProperty("action", Action.SEND_MESSAGE);
                json.add("data", data);
                return json.toString();
            }
            case Action.SET_USER_INFO: {
                data.addProperty("user_status", ((SetUserInfoRequestMessage) msg).userStatus);
                data.addProperty("cid", ((SetUserInfoRequestMessage) msg).cid);
                data.addProperty("sid", ((SetUserInfoRequestMessage) msg).sid);
                json.addProperty("action", Action.SET_USER_INFO);
                json.add("data", data);
                return json.toString();
            }
            case Action.USER_INFO: {
                data.addProperty("user", ((UserInfoRequestMessage) msg).user);
                data.addProperty("cid", ((UserInfoRequestMessage) msg).cid);
                data.addProperty("sid", ((UserInfoRequestMessage) msg).sid);
                json.addProperty("action", Action.USER_INFO);
                json.add("data", data);
                return json.toString();
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

        JsonObject data = json.get("data").getAsJsonObject();//null;

        
        if (json.has("data")) {
            data = json.get("data").getAsJsonObject();
        }

        switch (action) {
            case Action.AUTH: {
                String sid = null;
                String cid = null;
                if (data.has("sid")) {
                    sid = data.get("sid").getAsString();
                }
                if (data.has("cid")) {
                    cid = data.get("cid").getAsString();
                }
                return new AuthResponseMessage(
                        data.get("status").getAsInt(),
                        data.get("error").getAsString(),
                        sid,
                        cid
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
                                c.get("online").getAsInt(),
                                false
                        );
                        channelList.add(channel);
                    }
                }

                Collections.sort(channelList, new Comparator<Channel>() {
                    @Override
                    public int compare(Channel lhs, Channel rhs) {
                        return lhs.name.compareTo(rhs.name);
                    }
                });

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
            case Action.MESSAGE: {
                return new MessageMessage(
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
                return new WelcomeMessage(
                        json.get("message").getAsString(),
                        json.get("time").getAsLong()
                );
            }
        }

        return null;
    }
}
