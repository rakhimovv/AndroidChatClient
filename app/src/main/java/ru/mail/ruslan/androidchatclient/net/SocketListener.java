package ru.mail.ruslan.androidchatclient.net;

public interface SocketListener {
    void onConnected();
    void onConnectionFailed();

    void onDataReceived(String data);
}
