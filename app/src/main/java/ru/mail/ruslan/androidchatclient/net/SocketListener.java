package ru.mail.ruslan.androidchatclient.net;

import ru.mail.ruslan.androidchatclient.msg.Message;

public interface SocketListener {
    void onConnected();
    void onConnectionFailed();
    void onDataReceived(Message msg);
}
