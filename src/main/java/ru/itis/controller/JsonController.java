package ru.itis.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.itis.models.*;
import ru.itis.services.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class JsonController {

    private final ProductService productService;
    private final AuthService authService;
    private final ContactService contactService;
    private final UsersService usersService;
    private final LoginService loginService;
    private final ShopService shopService;

    @RequestMapping(value = "/img", method = RequestMethod.GET)
    public void getImg(HttpServletRequest req) {
        User user;
        try {
            user = usersService.find(usersService.getCurrentUser(req.getCookies()).getId());
            user.setRole(usersService.getRoleByUser(user));
        } catch (Exception e) {
            user = null;
        }
        Image image = usersService.getLogo(user);

        //TODO

//        String contentType = servletContext.getMimeType(image.getImageFileName());
//
//        resp.setHeader("Content-Type", contentType);
//
//        resp.setHeader("Content-Length", String.valueOf(image.getImageData().length));
//
//        resp.setHeader("Content-Disposition", "inline; filename=\"" + image.getImageFileName() + "\"");
//
//        // Write image data to Response.
//        resp.getOutputStream().write(image.getImageData());
    }

    @SneakyThrows
    @ResponseBody
    @RequestMapping(value = "/allusers.json", method = RequestMethod.GET)
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = usersService.getUsers();
        for (User user : users) {
            user.setRole(usersService.getRoleByUser(user));
        }
        return ResponseEntity.ok(users);
    }

    @SneakyThrows
    @ResponseBody
    @RequestMapping(value = "/answers.json", method = RequestMethod.GET)
    public ResponseEntity<List<Contact>> getAnswers(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            cookies = new Cookie[0];
        }
        User currentUser = new User();
        for(Cookie cookie:cookies){
            if(cookie.getName().equals("auth")) {
                Auth auth = authService.findByCookieValue(cookie.getValue());
                currentUser = usersService.find(auth.getUser().getId());
            }
        }
        return ResponseEntity.ok(contactService.getAnswers(currentUser));
    }

    @SneakyThrows
    @ResponseBody
    @RequestMapping(value = "/catalog.json", method = RequestMethod.GET)
    public ResponseEntity<List<Product>> getCatalog(){
        return ResponseEntity.ok(productService.findAll());
    }

    @SneakyThrows
    @ResponseBody
    @RequestMapping(value = "/messages.json", method = RequestMethod.GET)
    public ResponseEntity<List<Contact>> getMessages(){
        return ResponseEntity.ok(contactService.getMessages());

    }

    @SneakyThrows
    @ResponseBody
    @RequestMapping(value = "/main.json", method = RequestMethod.GET)
    public ResponseEntity<List<Product>> getData(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            cookies = new Cookie[0];
        }

        Basket basket = shopService.getUserBasket(loginService, cookies);
        System.out.println(basket);
        return ResponseEntity.ok(basket.getProducts());
    }

    @SneakyThrows
    @ResponseBody
    @RequestMapping(value = "/orders", method = RequestMethod.POST)
    public ResponseEntity<List<Basket>> getUserOrders(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            cookies = new Cookie[0];
        }
        User currentUser = new User();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("auth")) {
                Auth auth = authService.findByCookieValue(cookie.getValue());
                currentUser = usersService.find(auth.getUser().getId());

            }
        }
        Basket basket = shopService.findByOwnerId(currentUser.getId());
        List<Order> orders = shopService.getUserOrders(currentUser);
        List<Basket> baskets = new ArrayList<>();
        //TODO FIX
        for (Order order : orders) {
            Basket newBasket = Basket.builder().order_id(order.getId())
                    .track(shopService.getTrack(order))
                    .products(shopService.getProductsByOrder(basket, order))
                    .time(shopService.getStringTime(order.getTime()))
                    .build();
            baskets.add(newBasket);
        }

        return ResponseEntity.ok(baskets);
    }
}
