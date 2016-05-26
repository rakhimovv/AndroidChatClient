package ru.mail.ruslan.androidchatclient.net;

import java.util.List;

public interface DataProcessor {
    List<String> process(String data);
}