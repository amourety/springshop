package ru.itis.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import ru.itis.forms.LoginForm;
import ru.itis.models.Auth;
import ru.itis.models.User;
import ru.itis.services.AuthService;
import ru.itis.services.LoginService;
import ru.itis.services.UsersService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Controller
@RequestMapping(value = "/login")
@RequiredArgsConstructor
public class LoginController {

    private final UsersService usersService;
    private final LoginService loginService;
    private final AuthService authService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getPage(){
        return new ModelAndView("login");
    }

    @RequestMapping(method = RequestMethod.POST)
    public String getPost(HttpServletRequest req, HttpServletResponse res){
        String name = req.getParameter("name");
        String password = req.getParameter("password");

        LoginForm loginForm = LoginForm.builder()
                .name(name)
                .password(password)
                .build();
        User user = User.builder().name(name).build();
        req.getSession().setAttribute("user", user);
        Optional<String> optionalCookieValue = loginService.login(loginForm);
        System.out.println(loginForm);
        if (optionalCookieValue.isPresent()) {
            Cookie cookie = new Cookie("auth", optionalCookieValue.get());
            res.addCookie(cookie);
            Auth auth = authService.findByCookieValue(cookie.getValue());
            String role = "";
            try {
                role = usersService.getRoleByUser(usersService.find(auth.getUser().getId())).getName();
            }
            catch (Exception e){
                System.out.println("пользователя нет");
            }
            if(role.equals("admin")){
                return "redirect:/admin";
            } else {
                return "redirect:/main";
            }
        } else {
            return "redirect:/login";
        }
    }
}
