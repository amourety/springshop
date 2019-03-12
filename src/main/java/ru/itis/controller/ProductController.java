package ru.itis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import ru.itis.models.Product;
import ru.itis.services.ProductService;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @RequestMapping("/products/{id}")
    public ModelAndView getPage(@PathVariable("id") String stringId) {
        Long id =Long.parseLong(stringId);
        Product product = productService.getById(id);
        //name of the template is to be changed when we make it
        ModelAndView modelAndView = new ModelAndView("product_page");
        modelAndView.addObject("product", product);
        return modelAndView;
    }

    //todo add post request mapping if needed
}
