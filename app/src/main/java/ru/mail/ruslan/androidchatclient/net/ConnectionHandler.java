package ru.mail.ruslan.androidchatclient.net;

import java.io.IOException;

public interface ConnectionHandler extends Runnable {
    void sendData(String data) throws IOException;
    void addListener(SocketListener listener);
    void stop();
}
