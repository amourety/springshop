package ru.itis.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.itis.models.Category;
import ru.itis.models.Product;
import ru.itis.repositories.CategoryRepository;
import ru.itis.repositories.ProductsRepository;

import java.util.List;

@Component
public class ProductServiceImpl implements ProductService{
    @Autowired
    private ProductsRepository productsRepository;

    @Autowired
    private CategoryRepository categoryRepository;


    public List<Product> findAll(){
        return productsRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        productsRepository.deleteById(id);
    }

    @Override
    public void addProduct(Product model, String newCategory){
        if(newCategory.equals("true")){
            Category category = Category.builder().type(model.getCategory()).build();
            if(categoryRepository.findByType(model.getCategory()) == null){
                categoryRepository.save(category);
            }
        }
        model.setCategory(categoryRepository.getCategoryByString(model.getCategory()));
        productsRepository.save(model);
    }

    @Override
    public Product getById(Long id) {
        return productsRepository.find(id);
    }

    @Override
    public List<Product> getRandomProducts() {
        return productsRepository.getRandomProducts();
    }

}
