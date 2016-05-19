package ru.mail.ruslan.androidchatclient.msg.request;

import android.os.Parcel;

import ru.mail.ruslan.androidchatclient.msg.Action;
import ru.mail.ruslan.androidchatclient.msg.BaseMessage;

/*{
    "action":"setuserinfo",
    "data": {
        "user_status":"STATUS_STRING",
        "cid":"MY_USER_ID",
        "sid":"MY_SESSION_ID"
    }
}*/
public class SetUserInfoRequestMessage implements BaseMessage {
    public String userStatus;
    public String cid;  // client id
    public String sid;  // session id

    public SetUserInfoRequestMessage() {
    }

    public SetUserInfoRequestMessage(String userStatus, String cid, String sid) {
        this.userStatus = userStatus;
        this.cid = cid;
        this.sid = sid;
    }

    protected SetUserInfoRequestMessage(Parcel in) {
        userStatus = in.readString();
        cid = in.readString();
        sid = in.readString();
    }

    public static final Creator<SetUserInfoRequestMessage> CREATOR = new Creator<SetUserInfoRequestMessage>() {
        @Override
        public SetUserInfoRequestMessage createFromParcel(Parcel in) {
            return new SetUserInfoRequestMessage(in);
        }

        @Override
        public SetUserInfoRequestMessage[] newArray(int size) {
            return new SetUserInfoRequestMessage[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(userStatus);
        out.writeString(cid);
        out.writeString(sid);
    }

    @Override
    public String getAction() {
        return Action.SET_USER_INFO;
    }
}
