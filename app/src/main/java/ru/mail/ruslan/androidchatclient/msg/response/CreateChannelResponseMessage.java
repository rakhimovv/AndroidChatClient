package ru.mail.ruslan.androidchatclient.msg.response;

import android.os.Parcel;

import ru.mail.ruslan.androidchatclient.msg.Action;
import ru.mail.ruslan.androidchatclient.msg.BaseMessage;
import ru.mail.ruslan.androidchatclient.msg.Status;

/*{
    "action":"createchannel",
    "data":{
        "status":[0-9]+,
        "error":"TEXT_OF_ERROR",
        "chid":"CHANNEL_ID"
    }
}*/
public class CreateChannelResponseMessage implements BaseMessage {
    public Status status;
    public String error;
    public String chid;

    public CreateChannelResponseMessage() {
    }

    public CreateChannelResponseMessage(int status, String error, String chid) {
        this.status = Status.values()[status];
        this.error = error;
        this.chid = chid;
    }

    protected CreateChannelResponseMessage(Parcel in) {
        status = Status.values()[in.readInt()];
        error = in.readString();
        chid = in.readString();
    }

    public static final Creator<CreateChannelResponseMessage> CREATOR = new Creator<CreateChannelResponseMessage>() {
        @Override
        public CreateChannelResponseMessage createFromParcel(Parcel in) {
            return new CreateChannelResponseMessage(in);
        }

        @Override
        public CreateChannelResponseMessage[] newArray(int size) {
            return new CreateChannelResponseMessage[size];
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
        out.writeString(chid);
    }

    @Override
    public String getAction() {
        return Action.CREATE_CHANNEL;
    }

    @Override
    public Status getStatus() {
        return status;
    }
}
