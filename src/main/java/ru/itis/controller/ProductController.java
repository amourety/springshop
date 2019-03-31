package ru.itis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.ModelAndView;
import ru.itis.models.Product;
import ru.itis.models.Review;
import ru.itis.models.User;
import ru.itis.services.ProductService;
import ru.itis.services.ReviewService;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private ReviewService reviewService;

    @RequestMapping("/products/{id}")
    public ModelAndView getPage(@PathVariable("id") String stringId) {
        Long id = Long.parseLong(stringId);
        Product product = productService.getById(id);
        //name of the template is to be changed when we make it
        ModelAndView modelAndView = new ModelAndView("product_page");
        modelAndView.addObject("product", product);
        modelAndView.addObject("reviews", reviewService.getReviewsByProductId(id));
        return modelAndView;
    }

    @RequestMapping("products/{id}")
    public void saveReview(@PathVariable("id") Long id,
                           @RequestParam("text") String text,
                           @SessionAttribute("user") User user) {
        Review review = Review.builder()
                .productId(id)
                .text(text)
                .authorId(user.getId())
                .build();
        reviewService.save(review);
        //todo change from void
    }
}
