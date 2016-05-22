package ru.mail.ruslan.androidchatclient.msg.request;

import android.os.Parcel;

import ru.mail.ruslan.androidchatclient.msg.Action;
import ru.mail.ruslan.androidchatclient.msg.BaseMessage;
import ru.mail.ruslan.androidchatclient.msg.Status;

/*{
    "action":"auth",
    "data":{
        "login":"MY_LOGIN",
        "pass":"MD5_FROM_PASS"
    }
}*/
public class AuthRequestMessage implements BaseMessage {
    public String login;
    public String pass;

    public AuthRequestMessage() {
    }

    public AuthRequestMessage(String login, String pass) {
        this.login = login;
        this.pass = pass;
    }

    protected AuthRequestMessage(Parcel in) {
        login = in.readString();
        pass = in.readString();
    }

    public static final Creator<AuthRequestMessage> CREATOR = new Creator<AuthRequestMessage>() {
        @Override
        public AuthRequestMessage createFromParcel(Parcel in) {
            return new AuthRequestMessage(in);
        }

        @Override
        public AuthRequestMessage[] newArray(int size) {
            return new AuthRequestMessage[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(login);
        out.writeString(pass);
    }

    @Override
    public String getAction() {
        return Action.AUTH;
    }

    @Override
    public Status getStatus() {
        return Status.NO_STATUS;
    }
}
