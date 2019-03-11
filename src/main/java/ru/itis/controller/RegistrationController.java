package ru.itis.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import ru.itis.forms.UserForm;
import ru.itis.services.UsersService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class RegistrationController implements Controller {

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private UsersService usersService;

    private ObjectMapper mapper = new ObjectMapper();


    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (request.getMethod().equals("GET")) {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("registration");
            return modelAndView;
        }
        if (request.getMethod().equals("POST")) {
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
            if (usersService.findByName(username) == null) {
                usersService.addUser(userForm);
                String resultJson = mapper.writeValueAsString(1);
                response.setStatus(200);
                response.setContentType("application/json");
                PrintWriter writer = response.getWriter();
                writer.write(resultJson);
            } else {
                String resultJson = mapper.writeValueAsString(0);
                response.setStatus(200);
                response.setContentType("application/json");
                PrintWriter writer = response.getWriter();
                writer.write(resultJson);
            }
        }
        return null;
    }
}
