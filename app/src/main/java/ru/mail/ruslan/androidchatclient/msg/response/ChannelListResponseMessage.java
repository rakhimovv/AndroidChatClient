package ru.mail.ruslan.androidchatclient.msg.response;

import android.os.Parcel;

import ru.mail.ruslan.androidchatclient.msg.Action;
import ru.mail.ruslan.androidchatclient.msg.BaseMessage;
import ru.mail.ruslan.androidchatclient.msg.Status;

import java.util.ArrayList;
import java.util.List;

/*{
    "action":"channellist",
    "data":{
        "status":[0-9]+,
        "error":"TEXT_OF_ERROR",
        "channels":[
            {
                "chid":"NEED_CHANNEL_ID",
                "name":"NAME_OF_CHANNEL",
                "descr":"DESCRIPTION_OF_CHANNEL",
                "online":ONLINE_NUM,
            }, ....
        ]
    }
}*/
public class ChannelListResponseMessage implements BaseMessage {
    public Status status;
    public String error;
    public List<Channel> channels;

    public ChannelListResponseMessage() {
        channels = new ArrayList<>();
    }

    public ChannelListResponseMessage(int status, String error, List<Channel> channels) {
        this.status = Status.values()[status];
        this.error = error;
        this.channels = channels;
    }

    protected ChannelListResponseMessage(Parcel in) {
        status = Status.values()[in.readInt()];
        error = in.readString();
        channels = new ArrayList<>();
        in.readTypedList(channels, Channel.CREATOR);
    }

    public static final Creator<ChannelListResponseMessage> CREATOR = new Creator<ChannelListResponseMessage>() {
        @Override
        public ChannelListResponseMessage createFromParcel(Parcel in) {
            return new ChannelListResponseMessage(in);
        }

        @Override
        public ChannelListResponseMessage[] newArray(int size) {
            return new ChannelListResponseMessage[size];
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
        out.writeTypedList(channels);
    }

    @Override
    public String getAction() {
        return Action.CHANNEL_LIST;
    }
}
