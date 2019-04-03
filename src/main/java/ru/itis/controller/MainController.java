package ru.itis.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import ru.itis.models.Basket;
import ru.itis.models.Product;
import ru.itis.models.User;
import ru.itis.services.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final LoginService loginService;
    private final ShopService shopService;
    private final UsersService usersService;
    private final ProductService productService;
    private final AuthService authService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getPage(){
        return "redirect:/main";
    }


    @RequestMapping(value = "/main", method = RequestMethod.GET)
    public ModelAndView getPage(HttpServletRequest req){
        List<Product> products = productService.findAll();
        User user;
        try {
            user = usersService.find(usersService.getCurrentUser(req.getCookies()).getId());
            user.setRole(usersService.getRoleByUser(user));
        } catch (Exception e){
            user = null;
        }
        ModelAndView modelAndView = new ModelAndView();
        System.out.println(products);
        modelAndView.addObject("user", user);
        modelAndView.addObject("products",products);
        modelAndView.addObject("randomproducts", productService.getRandomProducts());
        modelAndView.setViewName("index");
        return modelAndView;
    }

    @RequestMapping(value = "/main", method = RequestMethod.POST)
    public ResponseEntity<Object> getPost(HttpServletRequest req, HttpServletResponse res){
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
                req.getSession().setAttribute("user", null);
                res.setStatus(200);
                break;
            case "delete":
                Long productId = Long.valueOf(req.getParameter("product_id"));
                basket = shopService.delete(productId,cookies,loginService);
                return ResponseEntity.ok(basket.getProducts());
            case "buy":
                productId = Long.valueOf(req.getParameter("product_id"));
                basket = shopService.buy(productId, cookies, loginService);
                return ResponseEntity.ok(basket.getProducts());
            case "deleteAll":
                basket = shopService.deleteAll(cookies, loginService);
                return ResponseEntity.ok(basket.getProducts());
            case "addOrder":
                shopService.addOrder(cookies,loginService);
                basket = shopService.deleteAll(cookies, loginService);
                return ResponseEntity.ok(basket);
            case "deleteOrder":
                Long orderId = Long.valueOf(req.getParameter("order_id"));
                shopService.deleteOrder(orderId);
                break;
        }
        return null;
    }
}
