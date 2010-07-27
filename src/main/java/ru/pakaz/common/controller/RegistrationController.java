package ru.pakaz.common.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import ru.pakaz.common.dao.UserDao;
import ru.pakaz.common.model.User;

@Controller
public class RegistrationController {
    static private Logger logger = Logger.getLogger( RegistrationController.class );

    @Autowired
    private UserDao usersManager;

    protected Object formBackingObject(HttpServletRequest request) {
        return new User();
    }

    @RequestMapping(value = "/registration.html", method = RequestMethod.GET)
    public ModelAndView showForm( HttpServletRequest request, HttpServletResponse response ) {
        ModelAndView mav = new ModelAndView( "registration" );
        mav.addObject( "user", new User() );
        return mav;
    }

    @RequestMapping(value = "/registration.html", method = RequestMethod.POST)
    public ModelAndView register( @ModelAttribute("user") User user, BindingResult result, 
    		HttpServletRequest request, HttpServletResponse response ) {

    	new UserInfoValidator().validate( user, result );
/*
        if (errors.hasErrors()) {
            return;
        }

        User formUser = user;

        if( usersManager.getUserByLogin( formUser.getLogin() ) != null ) {
            errors.reject( "error.user.login.exists" );
        }
        else {
            usersManager.setUserToSession( request, formUser );
        }
*/

    	if( !result.hasErrors() ) {
    		this.usersManager.createUser(request, user);
    		this.usersManager.setUserToSession(request, user);
    		
	        try {
				response.sendRedirect(request.getContextPath() +"/index.html");
			} catch (IOException e) {
				logger.error("Error on sending redirect to the main page!");
			}
			
			return null;
    	}
    	else {
            ModelAndView mav = new ModelAndView( "registration" );
            mav.addObject( "user", user );
            return mav;
    	}
    }

/*
    public UserDao getUserDao() {
        return this.usersManager;
    }
    public void setUserDao( UserDao userDao ) {
        this.usersManager = userDao;
    }
*/
}
