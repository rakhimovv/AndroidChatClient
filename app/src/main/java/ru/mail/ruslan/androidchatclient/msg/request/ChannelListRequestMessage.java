package ru.mail.ruslan.androidchatclient.msg.request;

import android.os.Parcel;

import ru.mail.ruslan.androidchatclient.msg.Action;
import ru.mail.ruslan.androidchatclient.msg.BaseMessage;
import ru.mail.ruslan.androidchatclient.msg.Status;

/*{
    "action":"channellist",
    "data":{
        "cid":"MY_USER_ID",
        "sid":"MY_SESSION_ID"
    }
}*/
public class ChannelListRequestMessage implements BaseMessage {
    public String cid;  // client id
    public String sid;  // session id

    public ChannelListRequestMessage() {
    }

    public ChannelListRequestMessage(String cid, String sid) {
        this.cid = cid;
        this.sid = sid;
    }

    protected ChannelListRequestMessage(Parcel in) {
        cid = in.readString();
        sid = in.readString();
    }

    public static final Creator<ChannelListRequestMessage> CREATOR = new Creator<ChannelListRequestMessage>() {
        @Override
        public ChannelListRequestMessage createFromParcel(Parcel in) {
            return new ChannelListRequestMessage(in);
        }

        @Override
        public ChannelListRequestMessage[] newArray(int size) {
            return new ChannelListRequestMessage[size];
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
    }

    @Override
    public String getAction() {
        return Action.CHANNEL_LIST;
    }

    @Override
    public Status getStatus() {
        return Status.NO_STATUS;
    }
}
