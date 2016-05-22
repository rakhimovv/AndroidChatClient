package ru.mail.ruslan.androidchatclient.msg.response;

import android.os.Parcel;

import ru.mail.ruslan.androidchatclient.msg.Action;
import ru.mail.ruslan.androidchatclient.msg.BaseMessage;
import ru.mail.ruslan.androidchatclient.msg.Status;

/*{
    "action":"register",
    "data":{
        "status":[0-9]+,
        "error":"TEXT_OF_ERROR"
    }
}*/
public class RegisterResponseMessage implements BaseMessage {
    public Status status;
    public String error;

    public RegisterResponseMessage() {
    }

    public RegisterResponseMessage(int status, String error) {
        this.status = Status.values()[status];
        this.error = error;
    }

    protected RegisterResponseMessage(Parcel in) {
        status = Status.values()[in.readInt()];
        error = in.readString();
    }

    public static final Creator<RegisterResponseMessage> CREATOR = new Creator<RegisterResponseMessage>() {
        @Override
        public RegisterResponseMessage createFromParcel(Parcel in) {
            return new RegisterResponseMessage(in);
        }

        @Override
        public RegisterResponseMessage[] newArray(int size) {
            return new RegisterResponseMessage[size];
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
        return Action.REGISTER;
    }

    @Override
    public Status getStatus() {
        return status;
    }
}
