package ru.mail.ruslan.androidchatclient.msg;

import android.os.Parcelable;

import com.google.gson.JsonObject;

public interface BaseMessage extends Parcelable {
    String getAction();
}
