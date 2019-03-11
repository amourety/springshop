package ru.itis.services;

import ru.itis.models.Auth;
import ru.itis.models.User;


public interface AuthService {
    Auth findByCookieValue(String cookieValue);
    void deleteCookieByUserId(User user);
}
