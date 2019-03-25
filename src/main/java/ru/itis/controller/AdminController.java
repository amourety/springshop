package ru.itis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import ru.itis.models.Auth;
import ru.itis.models.Product;
import ru.itis.models.User;
import ru.itis.services.AuthService;
import ru.itis.services.ContactService;
import ru.itis.services.ProductService;
import ru.itis.services.UsersService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {
    @Autowired
    private ProductService productService;
    @Autowired
    private AuthService authService;
    @Autowired
    private UsersService usersService;
    @Autowired
    private ContactService contactService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getPage(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            cookies = new Cookie[0];
        }
        User currentUser = new User();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("auth")) {
                Auth auth = authService.findByCookieValue(cookie.getValue());
                currentUser = usersService.find(auth.getUser().getId());
            }
        }
        if (usersService.getRoleByUser(currentUser).getName().equals("admin")) {
            return new ModelAndView("adminpanel");
        } else {
            return new ModelAndView("redirect:/index");
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView getPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            cookies = new Cookie[0];
        }

        String action = request.getParameter("action");

        System.out.println(request.getParameter("action") + " " + request.getParameter("product_id"));
        if (action.equals("deleteProduct")) {
            Long productId = Long.valueOf(request.getParameter("product_id"));
            productService.deleteById(productId);
        }
        if (action.equals("addProduct")) {
            Product product = Product.builder().name(request.getParameter("name")).price(request.getParameter("price")).img(request.getParameter("img")).about(request.getParameter("about")).category(request.getParameter("category")).build();
            productService.addProduct(product);
        }
        if (action.equals("replyMessage")) {
            Long messageId = Long.valueOf(request.getParameter("id_message"));
            contactService.addReply(messageId, request.getParameter("text"));
        }
        return null;
    }

}
