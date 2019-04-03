package ru.itis.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.itis.models.Product;
import ru.itis.services.SearchService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.List;

@Controller
@RequestMapping(value = "/search")
public class SearchController {

    @Autowired
    private SearchService service;

    @SneakyThrows
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Product>> search(HttpServletRequest req, HttpServletResponse resp){
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        String query = req.getParameter("q");
        if (query != null) {
            List<Product> result = service.search(query);
            return ResponseEntity.ok(result);
        }
    return null;
    }
}
