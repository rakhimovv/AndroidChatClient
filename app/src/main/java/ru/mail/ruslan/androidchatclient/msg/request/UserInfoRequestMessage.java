package ru.mail.ruslan.androidchatclient.msg.request;

import android.os.Parcel;

import ru.mail.ruslan.androidchatclient.msg.Action;
import ru.mail.ruslan.androidchatclient.msg.BaseMessage;

/*{
    "action":"userinfo",
    "data": {
        "user":"USER_ID",
        "cid":"MY_USER_ID",
        "sid":"MY_SESSION_ID"
    }
}*/
public class UserInfoRequestMessage implements BaseMessage {
    public String user;
    public String cid;  // client id
    public String sid;  // session id

    public UserInfoRequestMessage() {
    }

    public UserInfoRequestMessage(String user, String cid, String sid) {
        this.user = user;
        this.cid = cid;
        this.sid = sid;
    }

    protected UserInfoRequestMessage(Parcel in) {
        user = in.readString();
        cid = in.readString();
        sid = in.readString();
    }

    public static final Creator<UserInfoRequestMessage> CREATOR = new Creator<UserInfoRequestMessage>() {
        @Override
        public UserInfoRequestMessage createFromParcel(Parcel in) {
            return new UserInfoRequestMessage(in);
        }

        @Override
        public UserInfoRequestMessage[] newArray(int size) {
            return new UserInfoRequestMessage[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(user);
        out.writeString(cid);
        out.writeString(sid);
    }

    @Override
    public String getAction() {
        return Action.USER_INFO;
    }
}
