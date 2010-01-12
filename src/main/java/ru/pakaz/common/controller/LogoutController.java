package ru.pakaz.common.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Класс предназначен для разлогинивания пользователя
 * 
 * @author wilson
 *
 */
@Controller
public class LogoutController {
    @RequestMapping(value = "/logout.html", method = RequestMethod.GET)
    public String get( HttpServletRequest request ) {
        request.getSession(true).removeAttribute( "User" );
//        removeUser( request );
        return "redirect:index.html";
    }
/*
    public void removeUser(HttpServletRequest request) {
        request.getSession(true).removeAttribute( "User" );
    }*/
}
