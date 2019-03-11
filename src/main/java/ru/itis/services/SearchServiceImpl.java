package ru.itis.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import  ru.itis.models.Product;
import  ru.itis.repositories.ProductsRepository;

import java.util.List;

@Component
public class SearchServiceImpl implements SearchService {
    @Autowired
    private ProductsRepository productsRepository;

    @Override
    public List<Product> search(String title) {
        return productsRepository.findAllByTitleSearch(title);
    }
}
