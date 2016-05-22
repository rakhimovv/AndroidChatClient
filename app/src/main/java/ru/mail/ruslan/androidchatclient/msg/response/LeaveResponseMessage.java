package ru.mail.ruslan.androidchatclient.msg.response;

import android.os.Parcel;

import ru.mail.ruslan.androidchatclient.msg.Action;
import ru.mail.ruslan.androidchatclient.msg.BaseMessage;
import ru.mail.ruslan.androidchatclient.msg.Status;

/*{
    "action":"leave",
    "data":{
        "status":[0-9]+,
        "error":"TEXT_OF_ERROR",
    }
}*/
public class LeaveResponseMessage implements BaseMessage {
    public Status status;
    public String error;

    public LeaveResponseMessage() {
    }

    public LeaveResponseMessage(int status, String error) {
        this.status = Status.values()[status];
        this.error = error;
    }

    protected LeaveResponseMessage(Parcel in) {
        status = Status.values()[in.readInt()];
        error = in.readString();
    }

    public static final Creator<LeaveResponseMessage> CREATOR = new Creator<LeaveResponseMessage>() {
        @Override
        public LeaveResponseMessage createFromParcel(Parcel in) {
            return new LeaveResponseMessage(in);
        }

        @Override
        public LeaveResponseMessage[] newArray(int size) {
            return new LeaveResponseMessage[size];
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
        return Action.LEAVE;
    }

    @Override
    public Status getStatus() {
        return status;
    }
}
