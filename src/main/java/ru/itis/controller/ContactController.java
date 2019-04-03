package ru.itis.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import ru.itis.forms.ContactForm;
import ru.itis.models.Auth;
import ru.itis.models.Contact;
import ru.itis.models.User;
import ru.itis.services.AuthService;
import ru.itis.services.ContactService;
import ru.itis.services.UsersService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping(value = "/contacts")
@RequiredArgsConstructor
public class ContactController {

    private final ContactService contactService;
    private final UsersService usersService;
    private final AuthService authService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getPage(HttpServletRequest req) {
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
    @ResponseBody
    public ResponseEntity<Object> getPost(HttpServletRequest request) {
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

        String action = request.getParameter("action");
        System.out.println(action);

        if (action.equals("delete")) {
            String answer_id = request.getParameter("answer_id");
            Contact contact = Contact.builder().id(Long.valueOf(answer_id)).build();
            contactService.delete(contact);
            List<Contact> contacts = contactService.getAnswers(currentUser);
            return ResponseEntity.ok(contacts);
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
            return ResponseEntity.ok(1);
        }
        return null;
    }


}
