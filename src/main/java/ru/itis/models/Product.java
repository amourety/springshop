package ru.itis.models;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class Product {
    private Long id;
    private String name;
    private String price;
    private String img;
    private String category;
    private String about;
    private Double rating;
    private Integer countReviews;
    private Integer amount;
}