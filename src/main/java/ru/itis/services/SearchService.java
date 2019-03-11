package ru.itis.services;

import ru.itis.models.Product;

import java.util.List;

public interface SearchService {
    List<Product> search(String title);
}