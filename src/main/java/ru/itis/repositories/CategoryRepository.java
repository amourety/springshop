package ru.itis.repositories;

public interface CategoryRepository extends CrudRepository {
    String getCategoryByString(String type);
}
