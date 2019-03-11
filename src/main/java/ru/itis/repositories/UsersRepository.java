package ru.itis.repositories;

import ru.itis.models.Image;
import ru.itis.models.Role;
import ru.itis.models.User;

import java.io.InputStream;
import java.sql.SQLException;


public interface UsersRepository extends CrudRepository<User> {
    User findByName(String name);
    Role getRoleByUser(User user);
    void setLogo(User user, String logoName, InputStream logo)  throws SQLException;
    Image getLogo(User user);
}