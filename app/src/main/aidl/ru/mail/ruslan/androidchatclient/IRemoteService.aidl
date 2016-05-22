package ru.mail.ruslan.androidchatclient;

interface IRemoteService {
    oneway void sendMessage(String message);
}