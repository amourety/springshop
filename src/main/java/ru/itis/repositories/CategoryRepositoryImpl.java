package ru.itis.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.itis.models.Category;

import java.sql.ResultSet;
import java.util.List;

@Component
public class CategoryRepositoryImpl implements CategoryRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<Integer> integerRowMapper = (resultSet, i) ->
            resultSet.getInt("id");

    private RowMapper<Category> categoryRowMapper = (resultSet, i) -> Category.builder()
            .id(resultSet.getLong("id")).type(resultSet.getString("type")).build();

    @Override
    public String getCategoryByString(String type) {
        //language=SQL
        String sql = "select id from category where type = ?";

        try {
            return String.valueOf(jdbcTemplate.queryForObject(sql, integerRowMapper, type));
        } catch (Exception e) {
            return "1";
        }
    }

    @Override
    public List<Category> findAll() {
        return null;
    }

    @Override
    public Category find(Long id) {
        //language=SQL
        String sql = "select * from category where id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, categoryRowMapper, id);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void save(Category model) {
        //language=SQL
        String sql = "insert into category(type) value ?";
        if (this.getCategoryByString(model.getType()).equals("1")){
            jdbcTemplate.update(sql, model.getType());
        }
    }

    @Override
    public void update(Category model) {

    }
}
