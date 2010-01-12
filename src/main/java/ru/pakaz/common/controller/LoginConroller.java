package ru.pakaz.common.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import ru.pakaz.common.dao.UserDao;
import ru.pakaz.common.model.User;

@Controller
public class LoginConroller {
    @Autowired
    private UserDao usersManager;

    @RequestMapping(value = "/login.html", method = RequestMethod.GET)
    public ModelAndView get() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName( "login" );
        mav.addObject( "user", new User() );
        mav.addObject( "pageName", "Логин" );
        return mav;
//        return new ModelAndView( "login" ); 
    }

    @RequestMapping(value = "/login.html", method = RequestMethod.POST)
    public ModelAndView post( @ModelAttribute("user") User user, BindingResult result, HttpServletRequest request ) {
        LoginValidator validator = new LoginValidator();
        validator.setUsersManager( this.usersManager );
        validator.validate(user, result);
        
        ModelAndView mav = new ModelAndView();

        if( result.hasErrors() ) {
            mav.setViewName( "login" );
            mav.addObject( "user", user );
            mav.addObject( "pageName", "Логин" );
            return mav;
//            return "redirect:login.html";
        }
        else {
            this.usersManager.setUserToSession( request, this.usersManager.getUserByLogin( user.getLogin() ) );
            mav.setViewName( "redirect:index.html" );
            return mav;
//            return "redirect:index.html";
        }
    }

    public UserDao getUserDao() {
        return this.usersManager;
    }
    public void setUserDao( UserDao userDao ) {
        this.usersManager = userDao;
    }
}
