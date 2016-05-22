package ru.mail.ruslan.androidchatclient.msg.response;

import android.os.Parcel;

import ru.mail.ruslan.androidchatclient.msg.Action;
import ru.mail.ruslan.androidchatclient.msg.BaseMessage;
import ru.mail.ruslan.androidchatclient.msg.Status;

/*{
    "action":"ev_leave",
    "data":{
        "chid":"CHANNEL_ID",
        "uid":"USER_ID"
        "nick":"NICKNAME",
    }
}*/
public class LeaveEventMessage implements BaseMessage {
    public String chid; // channel id
    public String uid;  // user id
    public String nick;

    public LeaveEventMessage() {
    }

    public LeaveEventMessage(String chid, String uid, String nick) {
        this.chid = chid;
        this.uid = uid;
        this.nick = nick;
    }

    protected LeaveEventMessage(Parcel in) {
        chid = in.readString();
        uid = in.readString();
        nick = in.readString();
    }

    public static final Creator<LeaveEventMessage> CREATOR = new Creator<LeaveEventMessage>() {
        @Override
        public LeaveEventMessage createFromParcel(Parcel in) {
            return new LeaveEventMessage(in);
        }

        @Override
        public LeaveEventMessage[] newArray(int size) {
            return new LeaveEventMessage[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(chid);
        out.writeString(uid);
        out.writeString(nick);
    }

    @Override
    public String getAction() {
        return Action.EVENT_LEAVE;
    }

    @Override
    public Status getStatus() {
        return Status.NO_STATUS;
    }
}
