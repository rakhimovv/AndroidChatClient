package ru.mail.ruslan.androidchatclient.msg.response;

import android.os.Parcel;

import ru.mail.ruslan.androidchatclient.msg.Action;
import ru.mail.ruslan.androidchatclient.msg.BaseMessage;
import ru.mail.ruslan.androidchatclient.msg.Status;

/*{
    "action":"userinfo",
    "data":{
        "status":[0-9]+,
        "error":"TEXT_OF_ERROR",
        "nick":"NICKNAME",
        "user_status":"STATUS_STRING",
    }
}*/
public class UserInfoResponseMessage implements BaseMessage {
    public Status status;
    public String error;
    public String nick;
    public String userStatus;

    public UserInfoResponseMessage() {
    }

    public UserInfoResponseMessage(int status, String error, String nick, String userStatus) {
        this.status = Status.values()[status];
        this.error = error;
        this.nick = nick;
        this.userStatus = userStatus;
    }

    protected UserInfoResponseMessage(Parcel in) {
        status = Status.values()[in.readInt()];
        error = in.readString();
        nick = in.readString();
        userStatus = in.readString();
    }

    public static final Creator<UserInfoResponseMessage> CREATOR = new Creator<UserInfoResponseMessage>() {
        @Override
        public UserInfoResponseMessage createFromParcel(Parcel in) {
            return new UserInfoResponseMessage(in);
        }

        @Override
        public UserInfoResponseMessage[] newArray(int size) {
            return new UserInfoResponseMessage[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(status.ordinal());
        out.writeString(error);
        out.writeString(nick);
        out.writeString(userStatus);
    }

    @Override
    public String getAction() {
        return Action.USER_INFO;
    }
}
