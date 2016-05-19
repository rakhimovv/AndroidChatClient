package ru.mail.ruslan.androidchatclient.msg.request;

import android.os.Parcel;

import ru.mail.ruslan.androidchatclient.msg.Action;
import ru.mail.ruslan.androidchatclient.msg.BaseMessage;

/*{
    "action":"register",
    "data":{
        "login":"MY_LOGIN",
        "pass":"MD5_FROM_PASS",
        "nick":"NICKNAME"
    }
}*/
public class RegisterRequestMessage implements BaseMessage {
    public String login;
    public String pass;
    public String nick;

    public RegisterRequestMessage() {
    }

    public RegisterRequestMessage(String login, String pass, String nick) {
        this.login = login;
        this.pass = pass;
        this.nick = nick;
    }

    protected RegisterRequestMessage(Parcel in) {
        login = in.readString();
        pass = in.readString();
        nick = in.readString();
    }

    public static final Creator<RegisterRequestMessage> CREATOR = new Creator<RegisterRequestMessage>() {
        @Override
        public RegisterRequestMessage createFromParcel(Parcel in) {
            return new RegisterRequestMessage(in);
        }

        @Override
        public RegisterRequestMessage[] newArray(int size) {
            return new RegisterRequestMessage[size];
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
        out.writeString(nick);
    }

    @Override
    public String getAction() {
        return Action.REGISTER;
    }
}
