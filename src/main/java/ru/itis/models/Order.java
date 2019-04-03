package ru.itis.models;


import lombok.*;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class Order {

    private Long id;
    private Long user_id;
    private Long basket_id;
    private String text;
    private String track;
    private Timestamp time;
}
