package ru.mail.ruslan.androidchatclient.msg.request;

import android.os.Parcel;

import ru.mail.ruslan.androidchatclient.msg.Action;
import ru.mail.ruslan.androidchatclient.msg.BaseMessage;
import ru.mail.ruslan.androidchatclient.msg.Status;

/*{
    "action":"leave",
    "data": {
        "cid":"MY_USER_ID",
        "sid":"MY_SESSION_ID"
        "channel":"NEED_CHANNEL_ID"|"*"
    }
}*/
public class LeaveRequestMessage implements BaseMessage {
    public String cid;  // client id
    public String sid;  // session id
    public String channel;

    public LeaveRequestMessage() {
    }

    public LeaveRequestMessage(String cid, String sid, String channel) {
        this.cid = cid;
        this.sid = sid;
        this.channel = channel;
    }

    protected LeaveRequestMessage(Parcel in) {
        cid = in.readString();
        sid = in.readString();
        channel = in.readString();
    }

    public static final Creator<LeaveRequestMessage> CREATOR = new Creator<LeaveRequestMessage>() {
        @Override
        public LeaveRequestMessage createFromParcel(Parcel in) {
            return new LeaveRequestMessage(in);
        }

        @Override
        public LeaveRequestMessage[] newArray(int size) {
            return new LeaveRequestMessage[size];
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
        return Action.LEAVE;
    }

    @Override
    public Status getStatus() {
        return Status.NO_STATUS;
    }
}
