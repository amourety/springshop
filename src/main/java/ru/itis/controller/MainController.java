package ru.itis.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import ru.itis.models.Basket;
import ru.itis.models.Product;
import ru.itis.models.User;
import ru.itis.services.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.List;

public class MainController implements Controller {

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
    @Override
    public ModelAndView handleRequest(HttpServletRequest req, HttpServletResponse res) throws Exception {
        if (req.getMethod().equals("GET")) {
            List<Product> products = productService.findAll();
            User user;
            try {
                user = usersService.find(usersService.getCurrentUser(req.getCookies()).getId());
                user.setRole(usersService.getRoleByUser(user));
            } catch (Exception e){
                user = null;
            }
            System.out.println(user);
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("user", user);
            modelAndView.addObject("products",products);
            modelAndView.setViewName("index");
            return modelAndView;
        }
        if(req.getMethod().equals("POST")) {
            Cookie[] cookies = req.getCookies();
            if (cookies == null) {
                cookies = new Cookie[0];
            }
            Basket basket;

            String action = req.getParameter("action");

            System.out.println(req.getParameter("action") + " " + req.getParameter("product_id"));
            if(action.equals("exit")){
                System.out.println("exit");
                authService.deleteCookieByUserId(usersService.find(usersService.getCurrentUser(req.getCookies()).getId()));
                res.setStatus(200);
            }
            if(action.equals("delete")){
                Long productId = Long.valueOf(req.getParameter("product_id"));
                basket = shopService.delete(productId,cookies,loginService);
                res.setStatus(200);
                res.setContentType("application/json");
                String resultJson = mapper.writeValueAsString(basket.getProducts());
                PrintWriter writer = res.getWriter();
                writer.write(resultJson);
            }
            if(action.equals("buy")) {
                Long productId = Long.valueOf(req.getParameter("product_id"));
                basket = shopService.buy(productId, cookies, loginService);
                String resultJson = mapper.writeValueAsString(basket.getProducts());
                System.out.println(resultJson);
                res.setStatus(200);
                res.setContentType("application/json");
                PrintWriter writer = res.getWriter();
                writer.write(resultJson);
            }
            if(action.equals("deleteAll")) {
                basket = shopService.deleteAll(cookies, loginService);
                String resultJson = mapper.writeValueAsString(basket.getProducts());
                res.setStatus(200);
                res.setContentType("application/json");
                PrintWriter writer = res.getWriter();
                writer.write(resultJson);
            }
            if(action.equals("addOrder")) {
                shopService.addOrder(cookies,loginService);
                basket = shopService.deleteAll(cookies, loginService);
                String resultJson = mapper.writeValueAsString(basket);
                res.setStatus(200);
                res.setContentType("application/json");
                PrintWriter writer = res.getWriter();
                writer.write(resultJson);
            }
            if(action.equals("deleteOrder")){
                Long orderId = Long.valueOf(req.getParameter("order_id"));
                shopService.deleteOrder(orderId);
                res.setStatus(200);
            }
        }
        return null;
    }
}
