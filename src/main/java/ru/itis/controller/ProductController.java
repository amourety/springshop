package ru.itis.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
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
    public void saveReview(HttpServletRequest req, HttpServletResponse response) throws IOException {
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
                .rate(Integer.parseInt(req.getParameter("rate")))
                .build();
        System.out.println(review.toString());
        reviewService.save(review);
        //todo change from void
//        return "redirect:/products/"+req.getParameter("productId");
        List<Review> reviews = reviewService.getReviewsByProductId(productId);
        reviews = reviewService.getStringTime(reviews);
        ObjectMapper mapper = new ObjectMapper();
        String resultJson = mapper.writeValueAsString(reviews);
        response.setStatus(200);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter writer = response.getWriter();
        writer.write(resultJson);
    }
    @RequestMapping(value = "/rating", method = RequestMethod.POST)
    public void getRating(HttpServletRequest req, HttpServletResponse response) throws IOException {
        Long id = Long.parseLong(req.getParameter("productId"));
        Product product = productService.getById(id);
        System.out.println(product);
        ObjectMapper mapper = new ObjectMapper();
        String resultJson = mapper.writeValueAsString(product);
        response.setStatus(200);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter writer = response.getWriter();
        writer.write(resultJson);
    }

    //todo add post request mapping if needed
}
