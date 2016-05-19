package ru.mail.ruslan.androidchatclient.msg.response;

import android.os.Parcel;
import android.os.Parcelable;

import ru.mail.ruslan.androidchatclient.msg.Action;
import ru.mail.ruslan.androidchatclient.msg.BaseMessage;
import ru.mail.ruslan.androidchatclient.msg.Status;

/*{
    "uid":"USER_ID",
    "nick":"NICKNAME",
}*/
public class User implements Parcelable {
    public String uid; // user id
    public String nick;

    public User() {
    }

    public User(String uid, String nick) {
        this.uid = uid;
        this.nick = nick;
    }

    protected User(Parcel in) {
        uid = in.readString();
        nick = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(uid);
        out.writeString(nick);
    }
}
