package ru.mail.ruslan.androidchatclient.net;

import java.io.IOException;
import ru.mail.ruslan.androidchatclient.msg.Message;

public interface ConnectionHandler {
    void sendMessage(Message msg) throws IOException;
    void addListener(SocketListener listener);
    void start();
    void stop();
}
