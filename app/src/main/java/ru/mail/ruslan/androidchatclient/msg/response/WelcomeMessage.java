package ru.mail.ruslan.androidchatclient.msg.response;

import android.os.Parcel;
import android.os.Parcelable;

import ru.mail.ruslan.androidchatclient.msg.Action;
import ru.mail.ruslan.androidchatclient.msg.BaseMessage;

public class WelcomeMessage implements BaseMessage {
    public String message;
    public long time;

    public WelcomeMessage() {
    }

    public WelcomeMessage(String message, long time) {
        this.message = message;
        this.time = time;
    }

    public WelcomeMessage(Parcel in) {
        message = in.readString();
        time = in.readLong();
    }

    public static final Parcelable.Creator<WelcomeMessage> CREATOR = new Parcelable.Creator<WelcomeMessage>() {
        public WelcomeMessage createFromParcel(Parcel in) {
            return new WelcomeMessage(in);
        }

        public WelcomeMessage[] newArray(int size) {
            return new WelcomeMessage[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(message);
        out.writeLong(time);
    }

    @Override
    public String getAction() {
        return Action.WELCOME;
    }
}
