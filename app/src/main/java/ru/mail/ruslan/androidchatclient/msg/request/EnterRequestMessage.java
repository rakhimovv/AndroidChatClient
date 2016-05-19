package ru.mail.ruslan.androidchatclient.msg.request;

import android.os.Parcel;

import ru.mail.ruslan.androidchatclient.msg.Action;
import ru.mail.ruslan.androidchatclient.msg.BaseMessage;

/*{
    "action":"enter",
    "data": {
        "cid":"MY_USER_ID",
        "sid":"MY_SESSION_ID"
        "channel":"NEED_CHANNEL_ID"
    }
}*/
public class EnterRequestMessage implements BaseMessage {
    public String cid;  // client id
    public String sid;  // session id
    public String channel;

    public EnterRequestMessage() {
    }

    public EnterRequestMessage(String cid, String sid, String channel) {
        this.cid = cid;
        this.sid = sid;
        this.channel = channel;
    }

    protected EnterRequestMessage(Parcel in) {
        cid = in.readString();
        sid = in.readString();
        channel = in.readString();
    }

    public static final Creator<EnterRequestMessage> CREATOR = new Creator<EnterRequestMessage>() {
        @Override
        public EnterRequestMessage createFromParcel(Parcel in) {
            return new EnterRequestMessage(in);
        }

        @Override
        public EnterRequestMessage[] newArray(int size) {
            return new EnterRequestMessage[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(cid);
        out.writeString(sid);
        out.writeString(channel);
    }

    @Override
    public String getAction() {
        return Action.ENTER;
    }
}
