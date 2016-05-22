package ru.mail.ruslan.androidchatclient.msg.request;

import android.os.Parcel;

import ru.mail.ruslan.androidchatclient.msg.Action;
import ru.mail.ruslan.androidchatclient.msg.BaseMessage;
import ru.mail.ruslan.androidchatclient.msg.Status;

/*{
    "action":"createchannel",
    "data":{
        "cid":"MY_USER_ID",
        "sid":"MY_SESSION_ID",
        "name":"NAME_OF_CHANNEL",
        "descr":"DESCRIPTION_OF_CHANNEL"
    }
}*/
public class CreateChannelRequestMessage implements BaseMessage {
    public String cid;  // client id
    public String sid;  // session id
    public String name;
    public String descr;

    public CreateChannelRequestMessage() {
    }

    public CreateChannelRequestMessage(String cid, String sid, String name, String descr) {
        this.cid = cid;
        this.sid = sid;
        this.name = name;
        this.descr = descr;
    }

    protected CreateChannelRequestMessage(Parcel in) {
        cid = in.readString();
        sid = in.readString();
        name = in.readString();
        descr = in.readString();
    }

    public static final Creator<CreateChannelRequestMessage> CREATOR = new Creator<CreateChannelRequestMessage>() {
        @Override
        public CreateChannelRequestMessage createFromParcel(Parcel in) {
            return new CreateChannelRequestMessage(in);
        }

        @Override
        public CreateChannelRequestMessage[] newArray(int size) {
            return new CreateChannelRequestMessage[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(cid);
        out.writeString(sid);
        out.writeString(name);
        out.writeString(descr);
    }

    @Override
    public String getAction() {
        return Action.CREATE_CHANNEL;
    }

    @Override
    public Status getStatus() {
        return Status.NO_STATUS;
    }
}
