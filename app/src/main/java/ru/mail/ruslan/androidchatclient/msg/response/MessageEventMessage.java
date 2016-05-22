package ru.mail.ruslan.androidchatclient.msg.response;

import android.os.Parcel;

import ru.mail.ruslan.androidchatclient.msg.Action;
import ru.mail.ruslan.androidchatclient.msg.BaseMessage;
import ru.mail.ruslan.androidchatclient.msg.Status;

/*{
    "action":"ev_message",
    "data":{
        "chid":"CHANNEL_ID",
        "from":"USER_ID",
        "nick":"NICKNAME",
        "body":"TEXT_OF_MESSAGE"
    }
}*/
public class MessageEventMessage implements BaseMessage {
    public String chid; // channel id
    public String from;  // user id
    public String nick;
    public String body;

    public MessageEventMessage() {
    }

    public MessageEventMessage(String chid, String from, String nick, String body) {
        this.chid = chid;
        this.from = from;
        this.nick = nick;
        this.body = body;
    }

    protected MessageEventMessage(Parcel in) {
        chid = in.readString();
        from = in.readString();
        nick = in.readString();
        body = in.readString();
    }

    public static final Creator<MessageEventMessage> CREATOR = new Creator<MessageEventMessage>() {
        @Override
        public MessageEventMessage createFromParcel(Parcel in) {
            return new MessageEventMessage(in);
        }

        @Override
        public MessageEventMessage[] newArray(int size) {
            return new MessageEventMessage[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(chid);
        out.writeString(from);
        out.writeString(nick);
        out.writeString(body);
    }

    @Override
    public String getAction() {
        return Action.EVENT_MESSAGE;
    }

    @Override
    public Status getStatus() {
        return Status.NO_STATUS;
    }
}
