package ru.itis.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import ru.itis.forms.UserForm;
import ru.itis.services.UsersService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping(value = "/registration")
@RequiredArgsConstructor
public class RegistrationController{

    private final BCryptPasswordEncoder encoder;
    private final UsersService usersService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getPage(){
        return new ModelAndView("registration");
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Integer> getPost(HttpServletRequest request) {

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String first_name = request.getParameter("first_name");
        String surname = request.getParameter("surname");
        String hashPassword = encoder.encode(password);

        UserForm userForm = UserForm.builder().
                name(username).
                hashPassword(hashPassword).
                email(email).
                first_name(first_name).
                surname(surname).
                build();
        if(usersService.findByName(username) == null){
            usersService.addUser(userForm);
            return ResponseEntity.ok(1);
        }
        else {
            return ResponseEntity.ok(0);
        }
    }
}
