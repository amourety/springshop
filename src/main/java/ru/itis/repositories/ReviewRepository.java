package ru.itis.repositories;

import ru.itis.models.Review;

import java.util.List;

public interface ReviewRepository extends CrudRepository<Review> {

    List<Review> getByPostId(Long id);
}
