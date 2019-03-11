package ru.itis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import ru.itis.models.User;
import ru.itis.services.UsersService;

import javax.servlet.http.*;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

@Controller
@RequestMapping(value = "/profile")
public class ProfileController {

    @Autowired
    private UsersService usersService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getPage(HttpServletRequest request, HttpServletResponse response) {
        User user;
        try {
            user = usersService.find(usersService.getCurrentUser(request.getCookies()).getId());
            user.setRole(usersService.getRoleByUser(user));
        } catch (Exception e) {
            user = null;
        }

        ModelAndView modelAndView = new ModelAndView("profile");
        if (user != null) {
            modelAndView.addObject("user", user);
        } else {
            modelAndView.addObject("user", null);
        }
        return modelAndView;
    }

    //TODO DO LOADING PICTURES

//    @RequestMapping(method = RequestMethod.POST)
//    public ModelAndView getPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        Part filePart = request.getPart("file");
//        String fileName = getSubmittedFileName(filePart);
//        InputStream fileContent = filePart.getInputStream();
//        User user = usersService.find(usersService.getCurrentUser(request.getCookies()).getId());
//        try {
//            usersService.setLogo(user, fileName, fileContent);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

    private static String getSubmittedFileName(Part part) {
        for (String cd : part.getHeader("content-disposition").split(";")) {
            if (cd.trim().startsWith("filename")) {
                String fileName = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
                return fileName.substring(fileName.lastIndexOf('/') + 1).substring(fileName.lastIndexOf('\\') + 1); // MSIE fix.
            }
        }
        return null;
    }
}
