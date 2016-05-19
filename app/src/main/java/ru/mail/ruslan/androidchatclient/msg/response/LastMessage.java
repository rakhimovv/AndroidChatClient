package ru.mail.ruslan.androidchatclient.msg.response;

import android.os.Parcel;
import android.os.Parcelable;

/*{
    "mid":"MESSAGE_ID",
    "from":"USER_ID",
    "nick":"USERS_NICKNAME"
    "body":"TEXT_OF_MESSAGE",
    "time":UNIXTIMESTAMP_OF_MESSAGE,
}*/
public class LastMessage implements Parcelable {
    public String mid;  // message id
    public String from; // user id
    public String nick;
    public String body;
    public long time;

    public LastMessage() {
    }

    public LastMessage(String mid, String from, String nick, String body, long time) {
        this.mid = mid;
        this.from = from;
        this.nick = nick;
        this.body = body;
        this.time = time;
    }

    protected LastMessage(Parcel in) {
        mid = in.readString();
        from = in.readString();
        nick = in.readString();
        body = in.readString();
        time = in.readLong();
    }

    public static final Creator<LastMessage> CREATOR = new Creator<LastMessage>() {
        @Override
        public LastMessage createFromParcel(Parcel in) {
            return new LastMessage(in);
        }

        @Override
        public LastMessage[] newArray(int size) {
            return new LastMessage[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(mid);
        out.writeString(from);
        out.writeString(nick);
        out.writeString(body);
        out.writeLong(time);
    }
}
