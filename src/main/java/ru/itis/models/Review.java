package ru.itis.models;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Review {

    private Long id;
    private String text;
    private Timestamp time;
    private Long authorId;
    private Long productId;
    private String username;
}
