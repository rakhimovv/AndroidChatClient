package ru.mail.ruslan.androidchatclient.msg.response;

import android.os.Parcel;

import ru.mail.ruslan.androidchatclient.msg.Action;
import ru.mail.ruslan.androidchatclient.msg.BaseMessage;
import ru.mail.ruslan.androidchatclient.msg.Status;

/*{
    "action":"setuserinfo",
    "data":{
        "status":[0-9]+,
        "error":"TEXT_OF_ERROR"
    }
}*/
public class SetUserInfoResponseMessage implements BaseMessage {
    public Status status;
    public String error;

    public SetUserInfoResponseMessage() {
    }

    public SetUserInfoResponseMessage(int status, String error) {
        this.status = Status.values()[status];
        this.error = error;
    }

    protected SetUserInfoResponseMessage(Parcel in) {
        status = Status.values()[in.readInt()];
        error = in.readString();
    }

    public static final Creator<SetUserInfoResponseMessage> CREATOR = new Creator<SetUserInfoResponseMessage>() {
        @Override
        public SetUserInfoResponseMessage createFromParcel(Parcel in) {
            return new SetUserInfoResponseMessage(in);
        }

        @Override
        public SetUserInfoResponseMessage[] newArray(int size) {
            return new SetUserInfoResponseMessage[size];
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
