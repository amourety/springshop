package ru.itis.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.util.List;

public class CategoryRepositoryImpl implements CategoryRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<Integer> integerRowMapper = ResultSet::getInt;

    @Override
    public String getCategoryByString(String type) {
        //language=SQL
        String sql = "select id from category where type = ?";

        try {
            return String.valueOf(jdbcTemplate.queryForObject(sql,integerRowMapper,type));
        }
        catch (Exception e){
            return "1";
        }
    }

    @Override
    public List findAll() {
        return null;
    }

    @Override
    public Object find(Long id) {
        return null;
    }

    @Override
    public void save(Object model) {

    }

    @Override
    public void update(Object model) {

    }
}
