package ru.mail.ruslan.androidchatclient.msg.response;

import android.os.Parcel;

import java.util.ArrayList;
import java.util.List;

import ru.mail.ruslan.androidchatclient.msg.Action;
import ru.mail.ruslan.androidchatclient.msg.BaseMessage;
import ru.mail.ruslan.androidchatclient.msg.Status;

/*{
    "action":"enter",
    "data":{
        "status":[0-9]+,
        "error":"TEXT_OF_ERROR",
        "users":[
            {
                "uid":"USER_ID",
                "nick":"NICKNAME",
            },...
        ],
        "last_msg": [
            {
                "mid":"MESSAGE_ID",
                "from":"USER_ID",
                "nick":"USERS_NICKNAME",
                "body":"TEXT_OF_MESSAGE",
                "time":UNIXTIMESTAMP_OF_MESSAGE,
            }, ...
        ]
    }
}*/
public class EnterResponseMessage implements BaseMessage {
    public Status status;
    public String error;
    public List<User> users;
    public List<LastMessage> lastMsg;

    public EnterResponseMessage() {
        users = new ArrayList<>();
        lastMsg = new ArrayList<>();
    }

    public EnterResponseMessage(int status, String error, List<User> users, List<LastMessage> lastMsg) {
        this.status = Status.values()[status];
        this.error = error;
        this.users = users;
        this.lastMsg = lastMsg;
    }

    protected EnterResponseMessage(Parcel in) {
        status = Status.values()[in.readInt()];
        error = in.readString();
        users = new ArrayList<>();
        in.readTypedList(users, User.CREATOR);
        lastMsg = new ArrayList<>();
        in.readTypedList(lastMsg, LastMessage.CREATOR);
    }

    public static final Creator<EnterResponseMessage> CREATOR = new Creator<EnterResponseMessage>() {
        @Override
        public EnterResponseMessage createFromParcel(Parcel in) {
            return new EnterResponseMessage(in);
        }

        @Override
        public EnterResponseMessage[] newArray(int size) {
            return new EnterResponseMessage[size];
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
        out.writeTypedList(users);
        out.writeTypedList(lastMsg);
    }

    @Override
    public String getAction() {
        return Action.ENTER;
    }

    @Override
    public Status getStatus() {
        return status;
    }
}
