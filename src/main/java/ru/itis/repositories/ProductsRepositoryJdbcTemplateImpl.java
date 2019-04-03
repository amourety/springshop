package ru.itis.repositories;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.itis.models.Product;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.util.List;

@Component
public class ProductsRepositoryJdbcTemplateImpl implements ProductsRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private CategoryRepository categoryRepository;

    //language=SQL
    private static final String SQL_SEARCH = "select * from product where product.name alike %?";

    //language=SQL
    private static final String SQL_SELECT_PRODUCT_BY_ID =
            "select * from product p join category c2 on p.category = c2.id where p.id = ?";

    //language=SQL
    private static final String SQL_SELECT_ALL_PRODUCTS =
            "select * from product p join category c2 on p.category = c2.id order by p.countreview desc";

    //language=SQL
    private static final String SQL_INSERT_PRODUCT =
            "insert into product(name,price,img, category ,about) values (?,?,?,?,?)";

    //language=SQL
    private static final String SQL_SELECT_BY_NAME =
            "select * from product where name = ?";

    //language=SQL
    private static final String SQL_DELETE_PRODUCT_BY_ID =
            "DELETE from product where product.id = ? cascade";



    private RowMapper<Product> productRowMapper = (resultSet, i) -> Product.builder()
            .id(resultSet.getLong("id"))
            .name(resultSet.getString("name"))
            .price(resultSet.getString("price"))
            .img(resultSet.getString("img"))
            .about(resultSet.getString("about"))
            .rating(resultSet.getDouble("rating"))
            .build();
    private RowMapper<Product> productRowMapperCategory = (resultSet, i) -> Product.builder()
            .id(resultSet.getLong("id"))
            .name(resultSet.getString("name"))
            .price(resultSet.getString("price"))
            .img(resultSet.getString("img"))
            .category(resultSet.getString("type"))
            .about(resultSet.getString("about"))
            .rating(resultSet.getDouble("rating"))
            .countReviews(resultSet.getInt("countreview"))
            .build();

    @Override
    public List<Product> findAllByTitleSearch(String title) {
        String SQL_SEARCH = "select * from product where product.name ilike ?";
        return jdbcTemplate.query(SQL_SEARCH, productRowMapper, "%" + title + "%");
    }


    @Override
    public List<Product> findAll() {
        return jdbcTemplate.query(SQL_SELECT_ALL_PRODUCTS, productRowMapperCategory);
    }

    @Override
    public Product find(Long id) {
        return jdbcTemplate.queryForObject(SQL_SELECT_PRODUCT_BY_ID,
                productRowMapperCategory, id);
    }

    @Override
    public void save(Product model) {
        jdbcTemplate.update(SQL_INSERT_PRODUCT, model.getName(), model.getPrice(),model.getImg(),Integer.valueOf(model.getCategory()), model.getAbout());
    }


    @Override
    public void update(Product model) {

    }

    @Override
    public Product findByName(String name) {
        try {
            return jdbcTemplate.queryForObject(SQL_SELECT_BY_NAME, productRowMapper, name);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Product> findAllByTitle(String title) {
        return jdbcTemplate.query(SQL_SEARCH, productRowMapper, "%" + title + "%");
    }

    @Override
    public void deleteById(Long id){
        String SQL_DELETE2 = "DELETE from product where product.id = ?";
        String SQL_DELETE1 = "DELETE from product_basket where product_id = ?";

        jdbcTemplate.update(SQL_DELETE1,id);
        jdbcTemplate.update(SQL_DELETE2,id);
    }

    @Override
    public List<Product> getRandomProducts() {
        //language=SQL
        String SQL = "SELECT * FROM product ORDER BY random() LIMIT 3;";
        return jdbcTemplate.query(SQL, productRowMapper);
    }
}