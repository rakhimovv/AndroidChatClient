package ru.mail.ruslan.androidchatclient.serialization;

import java.net.ProtocolException;

import ru.mail.ruslan.androidchatclient.msg.BaseMessage;

public interface Protocol {

    String encode(BaseMessage msg) throws ProtocolException;

    BaseMessage decode(String data) throws ProtocolException;
}
