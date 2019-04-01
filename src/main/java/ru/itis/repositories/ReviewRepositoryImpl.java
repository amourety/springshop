package ru.itis.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.itis.models.Review;

import java.util.List;

/*
 * reviews:
 * id
 * productId
 * authorId
 * time
 * text
 */
@Repository
public class ReviewRepositoryImpl implements ReviewRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    //language=SQL
    private static final String SQL_SELECT_BY_PRODUCT_ID_QUERY =
            "select name as username, reviews.id, \"text\", time " +
                    "from reviews join users on reviews.author_id = users.id " +
                    "where product_id = ?";
    //language=SQL
    private static final String SQL_INSERT_QUERY = "insert into reviews (author_id, text, product_id, time) values (?, ?, ?, now())";

    private RowMapper<Review> rowMapperWithUsername = (resultSet, i) ->
            Review.builder()
                    .id(resultSet.getLong("id"))
                    .text(resultSet.getString("text"))
                    .time(resultSet.getTimestamp("time"))
                    .username(resultSet.getString("username"))
                    .build();

    @Override
    public List<Review> getByPostId(Long id) {
        return jdbcTemplate.query(SQL_SELECT_BY_PRODUCT_ID_QUERY, rowMapperWithUsername, id);
    }

    @Override
    public List<Review> findAll() {
        return null;
    }

    @Override
    public Review find(Long id) {
        return null;
    }

    @Override
    public void save(Review model) {
        jdbcTemplate.update(SQL_INSERT_QUERY, model.getAuthorId(), model.getText(), model.getProductId());
    }

    @Override
    public void update(Review model) {

    }
}