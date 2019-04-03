package ru.itis.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.itis.models.Product;
import ru.itis.models.Review;
import ru.itis.models.User;
import ru.itis.services.ProductService;
import ru.itis.services.ReviewService;
import ru.itis.services.UsersService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final UsersService usersService;
    private final ReviewService reviewService;

    @RequestMapping(value = "/products/{id}", method = RequestMethod.GET)
    public ModelAndView getPage(@PathVariable("id") String stringId, HttpServletRequest req) {
        User user;
        try {
            user = usersService.find(usersService.getCurrentUser(req.getCookies()).getId());
            user.setRole(usersService.getRoleByUser(user));
        } catch (Exception e) {
            user = null;
        }
        Long id = Long.parseLong(stringId);
        Product product = productService.getById(id);
        List<Product> productList = productService.getRandomProducts();
        List<Review> reviewList = reviewService.getReviewsByProductId(Long.valueOf(stringId));
        //name of the template is to be changed when we make it
        ModelAndView modelAndView = new ModelAndView("product_page");
        if(product == null){
            modelAndView.addObject("product", null);
        }
        else {
            modelAndView.addObject("product", product);
            modelAndView.addObject("feedbacks", reviewList);
        }
        modelAndView.addObject("products", productList);
        modelAndView.addObject("user", user);
        return modelAndView;
    }
    @RequestMapping(value = "/feedback", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<List<Review>> saveReview(HttpServletRequest req, HttpServletResponse response) throws IOException {
        User user2;
        try {
            user2 = usersService.find(usersService.getCurrentUser(req.getCookies()).getId());
            user2.setRole(usersService.getRoleByUser(user2));
        } catch (Exception e) {
            user2 = null;
        }
        Long productId = Long.parseLong(    req.getParameter("productId"));
        Review review = Review.builder()
                .productId(productId)
                .text(req.getParameter("text"))
                .authorId(user2.getId())
                .username(user2.getName())
                .rate(Integer.parseInt(req.getParameter("rate")))
                .build();
        reviewService.save(review);
        List<Review> reviews = reviewService.getReviewsByProductId(productId);
        return ResponseEntity.ok(reviewService.getStringTime(reviews));
    }

    @RequestMapping(value = "/rating", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Product> getRating(HttpServletRequest req, HttpServletResponse response) throws IOException {
        Long id = Long.parseLong(req.getParameter("productId"));
        return ResponseEntity.ok(productService.getById(id));
    }

    //todo add post request mapping if needed
}
