package ru.itis.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itis.models.Review;
import ru.itis.repositories.ReviewRepository;

import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    public List<Review> getReviewsByProductId(Long id) {
        return reviewRepository.getByPostId(id);
    }

    public void save(Review review) {
        reviewRepository.save(review);
    }
}
