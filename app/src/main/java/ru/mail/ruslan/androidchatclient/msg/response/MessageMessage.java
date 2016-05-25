package ru.mail.ruslan.androidchatclient.msg.response;

import android.os.Parcel;

import ru.mail.ruslan.androidchatclient.msg.Action;
import ru.mail.ruslan.androidchatclient.msg.BaseMessage;
import ru.mail.ruslan.androidchatclient.msg.Status;

/*{
    "action":"message",
    "data":{
        "status":[0-9]+,
        "error":"TEXT_OF_ERROR"
    }
}*/
public class MessageMessage implements BaseMessage {
    public Status status;
    public String error;

    public MessageMessage() {
    }

    public MessageMessage(int status, String error) {
        this.status = Status.values()[status];
        this.error = error;
    }

    protected MessageMessage(Parcel in) {
        status = Status.values()[in.readInt()];
        error = in.readString();
    }

    public static final Creator<MessageMessage> CREATOR = new Creator<MessageMessage>() {
        @Override
        public MessageMessage createFromParcel(Parcel in) {
            return new MessageMessage(in);
        }

        @Override
        public MessageMessage[] newArray(int size) {
            return new MessageMessage[size];
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
    }

    @Override
    public String getAction() {
        return Action.SET_USER_INFO;
    }

    @Override
    public Status getStatus() {
        return status;
    }
}
