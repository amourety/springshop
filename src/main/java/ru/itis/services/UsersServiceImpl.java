package ru.itis.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.itis.forms.UserForm;
import ru.itis.models.Image;
import ru.itis.models.Role;
import ru.itis.models.User;
import ru.itis.repositories.AuthRepository;
import ru.itis.repositories.UsersRepository;

import javax.servlet.http.Cookie;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;


@Component
public class UsersServiceImpl implements UsersService {
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private LoginService loginService;

    @Override
    public void addUser(UserForm userForm) {
        User user = User.builder()
                .name(userForm.getName())
                .passwordHash(userForm.getHashPassword())
                .email(userForm.getEmail())
                .first_name(userForm.getFirst_name())
                .surname(userForm.getSurname())
                .build();

        usersRepository.save(user);
    }

    @Override
    public List<User> getUsers() {
        return usersRepository.findAll();
    }
    public Role getRoleByUser(User user){
        return usersRepository.getRoleByUser(user);
    }

    @Override
    public User findByName(String username) {
        return usersRepository.findByName(username);
    }

    @Override
    public User find(Long id) {
        return usersRepository.find(id);
    }

    @Override
    public void setLogo(User user, String logoName, InputStream logo) throws SQLException {
        usersRepository.setLogo(user,logoName,logo);
    }

    @Override
    public Image getLogo(User user) {
        return usersRepository.getLogo(user);
    }

    public User getCurrentUser(Cookie[] cookies){
        if (cookies == null) {
            cookies = new Cookie[0];
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("auth")) {
                if (loginService.isExistByCookie(cookie.getValue())) {
                    return loginService.getUserByCookie(cookie.getValue());
                }
                else return null;
            }
        }
        return null;
    }
}
