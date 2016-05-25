package ru.mail.ruslan.androidchatclient.msg.response;

import android.os.Parcel;
import android.os.Parcelable;

public class ChannelMessage implements Parcelable {
    public String from; // user id
    public String nick;
    public String body;

    public ChannelMessage() {
    }

    public ChannelMessage(LastMessage lastMessage) {
        from = lastMessage.from;
        nick = lastMessage.nick;
        body = lastMessage.body;
    }

    public ChannelMessage(MessageEventMessage eventMessage) {
        from = eventMessage.from;
        nick = eventMessage.nick;
        body = eventMessage.body;
    }

    protected ChannelMessage(Parcel in) {
        from = in.readString();
        nick = in.readString();
        body = in.readString();
    }

    public static final Creator<ChannelMessage> CREATOR = new Creator<ChannelMessage>() {
        @Override
        public ChannelMessage createFromParcel(Parcel in) {
            return new ChannelMessage(in);
        }

        @Override
        public ChannelMessage[] newArray(int size) {
            return new ChannelMessage[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(from);
        out.writeString(nick);
        out.writeString(body);
    }
}