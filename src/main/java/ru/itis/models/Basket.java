package ru.itis.models;

import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class Basket {
    private Long id;
    private Long order_id;
    private User user;
    private List<Product> products;
    private String track;
    private String time;

    public Basket(User basketOwner) {
        this.user = basketOwner;
    }


    public Product findByName(String name){
        for (Product product : products){
            if(product.getName().equals(name)){
                return product;
            }
        }
        return null;
    }

    public void add(Product product){
        products.remove(findByName(product.getName()));
        products.add(product);

    }

    public void delete(Product product) {
        products.remove(findByName(product.getName()));
        products.add(product);
    }

    public void deleteAll() {
        products.clear();
    }
}