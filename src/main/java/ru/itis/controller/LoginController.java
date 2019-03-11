package ru.itis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import ru.itis.forms.LoginForm;
import ru.itis.models.Auth;
import ru.itis.services.AuthService;
import ru.itis.services.LoginService;
import ru.itis.services.UsersService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

public class LoginController implements Controller {

    @Autowired
    private UsersService usersService;
    @Autowired
    private LoginService loginService;
    @Autowired
    private AuthService authService;

    @Override
    public ModelAndView handleRequest(HttpServletRequest req, HttpServletResponse res) throws Exception {
        if (req.getMethod().equals("GET")) {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("login");
            return modelAndView;
        }
        if (req.getMethod().equals("POST")) {
            String name = req.getParameter("name");
            String password = req.getParameter("password");

            LoginForm loginForm = LoginForm.builder()
                    .name(name)
                    .password(password)
                    .build();

            Optional<String> optionalCookieValue = loginService.login(loginForm);


            if (optionalCookieValue.isPresent()) {
                Cookie cookie = new Cookie("auth", optionalCookieValue.get());
                res.addCookie(cookie);
                Auth auth = authService.findByCookieValue(cookie.getValue());
                String role = "";
                try {
                    role = usersService.getRoleByUser(usersService.find(auth.getUser().getId())).getName();
                } catch (Exception e) {
                    System.out.println("пользователя нет");
                }
                if (role.equals("admin")) {
                    res.setStatus(201);
                    res.sendRedirect("/admin");
                } else {
                    res.setStatus(201);
                    res.sendRedirect("/main");
                }
            } else {
                res.setStatus(200);
                res.sendRedirect("/login");
            }
        }
        return null;
    }
}
