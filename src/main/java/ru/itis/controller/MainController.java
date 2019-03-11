package ru.itis.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ru.itis.models.Basket;
import ru.itis.models.Product;
import ru.itis.models.User;
import ru.itis.services.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Controller
public class MainController {

    @Autowired
    private LoginService loginService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private UsersService usersService;
    @Autowired
    private ProductService productService;
    @Autowired
    private AuthService authService;

    private ObjectMapper mapper = new ObjectMapper();

    @RequestMapping(value = "/main", method = RequestMethod.GET)
    public ModelAndView getPage(HttpServletRequest req, HttpServletResponse res){
        List<Product> products = productService.findAll();
        User user;
        try {
            user = usersService.find(usersService.getCurrentUser(req.getCookies()).getId());
            user.setRole(usersService.getRoleByUser(user));
        } catch (Exception e){
            user = null;
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("user", user);
        modelAndView.addObject("products",products);
        modelAndView.setViewName("index");
        return modelAndView;
    }

    @RequestMapping(value = "/main", method = RequestMethod.POST)
    public ModelAndView getPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        Cookie[] cookies = req.getCookies();
        if (cookies == null) {
            cookies = new Cookie[0];
        }
        Basket basket;

        String action = req.getParameter("action");

        System.out.println(req.getParameter("action") + " " + req.getParameter("product_id"));
        switch (action){
            case "exit":
                System.out.println("exit");
                authService.deleteCookieByUserId(usersService.find(usersService.getCurrentUser(req.getCookies()).getId()));
                res.setStatus(200);
                break;
            case "delete":
                Long productId = Long.valueOf(req.getParameter("product_id"));
                basket = shopService.delete(productId,cookies,loginService);
                res.setStatus(200);
                res.setContentType("application/json");
                String resultJson = mapper.writeValueAsString(basket.getProducts());
                PrintWriter writer = res.getWriter();
                writer.write(resultJson);
                break;
            case "buy":
                productId = Long.valueOf(req.getParameter("product_id"));
                basket = shopService.buy(productId, cookies, loginService);
                resultJson = mapper.writeValueAsString(basket.getProducts());
                System.out.println(resultJson);
                res.setStatus(200);
                res.setContentType("application/json");
                writer = res.getWriter();
                writer.write(resultJson);
                break;
            case "deleteAll":
                basket = shopService.deleteAll(cookies, loginService);
                resultJson = mapper.writeValueAsString(basket.getProducts());
                res.setStatus(200);
                res.setContentType("application/json");
                writer = res.getWriter();
                writer.write(resultJson);
                break;
            case "addOrder":
                shopService.addOrder(cookies,loginService);
                basket = shopService.deleteAll(cookies, loginService);
                resultJson = mapper.writeValueAsString(basket);
                res.setStatus(200);
                res.setContentType("application/json");
                writer = res.getWriter();
                writer.write(resultJson);
                break;
            case "deleteOrder":
                Long orderId = Long.valueOf(req.getParameter("order_id"));
                shopService.deleteOrder(orderId);
                res.setStatus(200);
                break;
        }
        return null;
    }
}
