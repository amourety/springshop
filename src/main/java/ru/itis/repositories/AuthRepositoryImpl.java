package ru.itis.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.itis.models.Auth;
import ru.itis.models.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Component
public class AuthRepositoryImpl implements AuthRepository {

    //language=SQL
    private static final String SQL_INSERT =
            "insert into auth(user_id, cookie_value) values (?, ?)";

    //language=SQL
    private static final String SQL_SELECT_BY_COOKIE_VALUE =
            "select * from auth where cookie_value = ?";

    //language=SQL
    private static final String SQL_DELETE_COOKIE =
            "DELETE FROM auth where user_id = ?";

    @Autowired
    private JdbcTemplate template;

    private RowMapper<Auth> authRowMapper = (rs, rowNum) -> Auth.builder()
            .id(rs.getLong("id"))
            .user(User.builder()
                    .id(rs.getLong("user_id"))
                    .build())
            .cookieValue(rs.getString("cookie_value"))
            .build();

    @Override
    public List<Auth> findAll() {
        return null;
    }

    @Override
    public Auth find(Long id) {
        return null;
    }

    @Override
    public void save(Auth model) {
        template.update(SQL_INSERT, model.getUser().getId(), model.getCookieValue());
    }

    @Override
    public void update(Auth model) {

    }

    @Override
    public Optional<Auth> findByCookieValue(String cookieValue) {
        try {
            return Optional.of(template.queryForObject(SQL_SELECT_BY_COOKIE_VALUE, authRowMapper, cookieValue));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void deleteCookieByUserId(User user) {
        template.update(SQL_DELETE_COOKIE,user.getId());
    }
}
