package ru.mail.ruslan.androidchatclient.msg;

public enum Status {
    ERR_OK,                 // 0, Все хорошо
    ERR_ALREADY_EXIST,      // 1, Логин или ник или канал уже заняты
    ERR_INVALID_PASS,       // 2, Невалидный логин или пароль
    ERR_INVALID_DATA,       // 3, Невалидный JSON
    ERR_EMPTY_FIELD,        // 4, Пустой ник, логин, пароль или имя канала
    ERR_ALREADY_REGISTER,   // 5, Пользователь уже зарегистрирован (лишний вызов register)
    ERR_NEED_AUTH,          // 6, Нужна авторизация
    ERR_NEED_REGISTER,      // 7, Нужна регистрация
    ERR_USER_NOT_FOUND,     // 8, По указанному UserId не найден пользователь
    ERR_CHANNEL_NOT_FOUND,  // 9, Не найден канал по ID
    ERR_INVALID_CHANNEL     // 10, Пользователь не в канале
}
