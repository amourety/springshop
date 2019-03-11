package ru.itis.repositories;

import ru.itis.models.Basket;
import ru.itis.models.Order;
import ru.itis.models.User;

import java.util.List;

public interface OrderRepository extends CrudRepository<Order> {
    void addOrder(Order order, Basket basket);
    List<Order> findOrders(User user);
    void delete(Long id);
    String getTrack(Order order);
}
