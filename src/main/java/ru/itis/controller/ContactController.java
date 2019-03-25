package ru.itis.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import ru.itis.forms.ContactForm;
import ru.itis.models.Auth;
import ru.itis.models.Contact;
import ru.itis.models.Product;
import ru.itis.models.User;
import ru.itis.services.AuthService;
import ru.itis.services.ContactService;
import ru.itis.services.UsersService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/contacts")
public class ContactController {
    @Autowired
    private ContactService contactService;
    @Autowired
    private UsersService usersService;
    @Autowired
    private AuthService authService;

    private ObjectMapper mapper = new ObjectMapper();

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getPage(HttpServletRequest req, HttpServletResponse res) {
        User user;

        try {
            user = usersService.find(usersService.getCurrentUser(req.getCookies()).getId());
            user.setRole(usersService.getRoleByUser(user));
        } catch (Exception e) {
            user = null;
        }
        List<Contact> contacts = contactService.getAnswers(user);
        ModelAndView modelAndView = new ModelAndView("contacts");
        modelAndView.addObject("user", user);
        modelAndView.addObject("answers", contacts);
        return modelAndView;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView getPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Cookie[] cookies = request.getCookies();
        boolean exists = false;

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

        String action = request.getParameter("action");
        System.out.println(action);

        if (action.equals("delete")) {
            String answer_id = request.getParameter("answer_id");
            Contact contact = Contact.builder().id(Long.valueOf(answer_id)).build();
            contactService.delete(contact);
            List<Contact> contacts = contactService.getAnswers(currentUser);
            String resultJson = mapper.writeValueAsString(contacts);
            response.setStatus(200);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter writer = response.getWriter();
            writer.write(resultJson);
        }
        if (action.equals("sending")) {
            String name = request.getParameter("name");
            String surname = request.getParameter("surname");
            String email = request.getParameter("email");
            String text = request.getParameter("text");

            ContactForm contactForm = ContactForm.builder().
                    name(name).
                    surname(surname).
                    email(email).
                    letter(text).
                    userid(currentUser.getId()).
                    build();
            contactService.addContact(contactForm);
//            String resultJson = mapper.writeValueAsString("1");
//            response.setStatus(200);
//            response.setContentType("application/json");
//            response.setCharacterEncoding("UTF-8");
//            PrintWriter writer = response.getWriter();
//            writer.write(resultJson);
        }
        return new ModelAndView("contacts");
    }


}
