package ru.itis.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.itis.models.*;
import ru.itis.services.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.List;

@Controller
public class JsonController {

    @Autowired
    private ProductService productService;
    @Autowired
    private AuthService authService;
    @Autowired
    private ContactService contactService;
    @Autowired
    private UsersService usersService;
    @Autowired
    private LoginService loginService;
    @Autowired
    private ShopService shopService;

    private ObjectMapper mapper = new ObjectMapper();

    @RequestMapping(value = "/img", method = RequestMethod.GET)
    public void getImg(HttpServletRequest req, HttpServletResponse resp) {
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
    @RequestMapping(value = "/allusers.json", method = RequestMethod.GET)
    public void getAllUsers(HttpServletResponse response) {
        List<User> users = usersService.getUsers();
        for (User user : users) {
            user.setRole(usersService.getRoleByUser(user));
        }
        String resultJson = mapper.writeValueAsString(users);
        response.setStatus(200);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter writer = response.getWriter();
        writer.write(resultJson);
    }

    @SneakyThrows
    @RequestMapping(value = "/answers.json", method = RequestMethod.GET)
    public void getAnswers(HttpServletRequest request,HttpServletResponse response) {
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
        List<Contact> contacts = contactService.getAnswers(currentUser);

        String resultJson = mapper.writeValueAsString(contacts);
        response.setStatus(200);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter writer = response.getWriter();
        writer.write(resultJson);
    }

    @SneakyThrows
    @RequestMapping(value = "/catalog.json", method = RequestMethod.GET)
    public void getCatalog(HttpServletRequest request,HttpServletResponse response){
        List<Product> productList = productService.findAll();
        String resultJson = mapper.writeValueAsString(productList);
        response.setStatus(200);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter writer = response.getWriter();
        writer.write(resultJson);
    }

    @SneakyThrows
    @RequestMapping(value = "/messages.json", method = RequestMethod.GET)
    public void getMessages(HttpServletRequest request,HttpServletResponse response){
        List<Contact> contacts = contactService.getMessages();
        String resultJson = mapper.writeValueAsString(contacts);
        response.setStatus(200);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter writer = response.getWriter();
        writer.write(resultJson);
    }

    @SneakyThrows
    @RequestMapping(value = "/main.json", method = RequestMethod.GET)
    public void getData(HttpServletRequest request,HttpServletResponse response){
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            cookies = new Cookie[0];
        }

        Basket basket = shopService.getUserBasket(loginService, cookies);
        String resultJson = mapper.writeValueAsString(basket.getProducts());
        response.setStatus(200);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter writer = response.getWriter();
        writer.write(resultJson);
    }

    @SneakyThrows
    @RequestMapping(value = "/orders.json", method = RequestMethod.GET)
    public void getUserOrders(HttpServletRequest request,HttpServletResponse response){
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
        Basket basket = shopService.findByOwnerId(currentUser.getId());
        List<Order> orders = shopService.getUserOrders(currentUser);
        Basket[] baskets = new Basket[orders.size()];
        int i = 0;

        //TODO FIX
        for(Order order: orders){
            baskets[i] = basket;
            baskets[i].setOrder_id(order.getId());
            baskets[i].setTrack(shopService.getTrack(order));
            baskets[i].setProducts(shopService.getProductsByOrder(basket,order));
            System.out.println(baskets[i]);
            i++;
        }

        String resultJson = mapper.writeValueAsString(baskets);
        response.setStatus(200);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter writer = response.getWriter();
        writer.write(resultJson);
    }
}
