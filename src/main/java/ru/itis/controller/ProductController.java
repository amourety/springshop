package ru.itis.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private UsersService usersService;
    @Autowired
    private ReviewService reviewService;

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
        System.out.println(product);
        modelAndView.addObject("feedbacks", reviewList);
        modelAndView.addObject("products", productList);
        modelAndView.addObject("product", product);
        modelAndView.addObject("user", user);
        return modelAndView;
    }
    @RequestMapping(value = "/feedback", method = RequestMethod.POST)
    public String saveReview(HttpServletRequest req) {
        User user2;
        try {
            user2 = usersService.find(usersService.getCurrentUser(req.getCookies()).getId());
            user2.setRole(usersService.getRoleByUser(user2));
        } catch (Exception e) {
            user2 = null;
        }
        Long productId = Long.parseLong(    req.getParameter("productId"));
        System.out.println(productId);
        Review review = Review.builder()
                .productId(productId)
                .text(req.getParameter("text"))
                .authorId(user2.getId())
                .username(user2.getName())
                .build();
        System.out.println(review.toString());
        reviewService.save(review);
        //todo change from void
        return "redirect:/products/"+req.getParameter("productId");
    }

    //todo add post request mapping if needed
}
