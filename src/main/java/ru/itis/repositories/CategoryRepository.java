package ru.itis.repositories;

import ru.itis.models.Category;

public interface CategoryRepository extends CrudRepository<Category> {
    String getCategoryByString(String type);
    Category findByType(String type);
}
