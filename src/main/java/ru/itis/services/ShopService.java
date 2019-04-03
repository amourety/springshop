package ru.itis.services;

import  ru.itis.models.Basket;
import ru.itis.models.Order;
import ru.itis.models.Product;
import ru.itis.models.User;

import javax.servlet.http.Cookie;
import java.sql.Timestamp;
import java.util.List;

public interface ShopService {

    Basket findByOwnerId(Long id);

    Basket buy(Long productId, Cookie[] cookies, LoginService loginService);

    Basket getUserBasket(LoginService loginService, Cookie[] cookies);

    List<Product> getProducts(Basket basket);

    Basket delete(Long productId, Cookie[] cookies, LoginService loginService);

    Basket deleteAll(Cookie[] cookies, LoginService loginService);

    void addOrder(Cookie[] cookies, LoginService loginService);

    List<Order> getUserOrders(User user);
    void deleteOrder(Long id);

    List<Product> getProductsByOrder(Basket basket, Order order);
    String getTrack(Order order);
    String getStringTime(Timestamp time);
}