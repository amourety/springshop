package ru.itis.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.itis.models.Auth;
import ru.itis.models.User;
import ru.itis.repositories.AuthRepository;

@Component
public class AuthServiceImpl implements AuthService {
    @Autowired
    private AuthRepository authRepository;

    @Override
    public Auth findByCookieValue(String cookieValue) {
        return authRepository.findByCookieValue(cookieValue).get();
    }

    @Override
    public void deleteCookieByUserId(User user) {
        authRepository.deleteCookieByUserId(user);
    }
}
