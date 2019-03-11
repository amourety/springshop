package ru.itis.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.itis.forms.LoginForm;
import ru.itis.models.Auth;
import ru.itis.models.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.itis.repositories.AuthRepository;
import ru.itis.repositories.UsersRepository;

import java.util.Optional;
import java.util.UUID;

@Component
public class LoginServiceImpl implements LoginService {
    @Autowired
    private AuthRepository authRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private BCryptPasswordEncoder encoder;

    @Override
    public Optional<String> login(LoginForm loginForm) {
        User user = usersRepository.findByName(loginForm.getName());
        if (user !=null && encoder.matches(loginForm.getPassword(), user.getPasswordHash())) {
            String cookieValue = UUID.randomUUID().toString();
            Auth auth = Auth.builder()
                    .cookieValue(cookieValue)
                    .user(user)
                    .build();
            authRepository.save(auth);

            return Optional.of(cookieValue);
        } else
            return Optional.empty();
    }

    @Override
    public boolean isExistByCookie(String cookieValue) {
        return authRepository.findByCookieValue(cookieValue).isPresent();
    }

    @Override
    public User getUserByCookie(String cookieValue) {
        Auth auth = authRepository.findByCookieValue(cookieValue).get();
        return User.builder()
                .id((usersRepository.find(auth.getUser().getId())).getId())
                .name((usersRepository.find(auth.getUser().getId())).getName())
                .passwordHash((usersRepository.find(auth.getUser().getId())).getPasswordHash())
                .build();
    }
}