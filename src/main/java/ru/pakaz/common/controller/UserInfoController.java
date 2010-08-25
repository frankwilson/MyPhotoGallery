package ru.pakaz.common.controller;

import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContext;
import org.springframework.web.servlet.view.RedirectView;
import ru.pakaz.common.dao.UserDao;
import ru.pakaz.common.model.User;

@Controller
public class UserInfoController {
    static private Logger logger = Logger.getLogger( UserInfoController.class );

    @Autowired
    private UserDao usersManager;

    @RequestMapping(value = "/changeUsersInfo.html", method = RequestMethod.GET)  
    public ModelAndView get( HttpServletRequest request ) {
        User user = null;
        ModelAndView mav = null;
        if( request != null ) {
            user = this.usersManager.getUserFromSession( request );
        }
        if( user == null ) {
            mav = new ModelAndView( new RedirectView( "login.html" ) );
        }
        else {
            mav = new ModelAndView();
            mav.setViewName( "userInfoForm" );
            mav.addObject( "user", user );
            mav.addObject( "pageName", new RequestContext(request).getMessage( "page.title.userInfoForm" ) );
        }

        return mav;
    }  

    @RequestMapping(value = "/changeUsersInfo.html", method = RequestMethod.POST)  
    public ModelAndView post( @ModelAttribute("user") User user, BindingResult result, HttpServletRequest request ) {
        User dbUser = this.usersManager.getUserFromSession( request );
        user.setLogin(dbUser.getLogin());
        new UserInfoValidator().validate( user, result );

        if( !result.hasErrors() ) {
            dbUser.setFirstName( user.getFirstName() );
            dbUser.setLastName( user.getLastName() );
            dbUser.setEmail( user.getEmail() );
            dbUser.setNickName( user.getNickName() );
            
            logger.debug("Old password: "+ request.getParameter("old_pass"));
            String oldPass = request.getParameter("old_pass");
            logger.debug("New password: "+ request.getParameter("new_pass"));
            String newPass = request.getParameter("new_pass");

            if( dbUser.getPassword().equals(oldPass) && newPass.length() > 0 ) {
                dbUser.setPassword( newPass );
            }

            this.usersManager.updateUser( request, dbUser );
        }

        ModelAndView mav = new ModelAndView();
        mav.setViewName( "userInfoForm" );
        mav.addObject( "pageName", new RequestContext(request).getMessage( "page.title.userInfoForm" ) );
        return mav;
    }

    @RequestMapping(value = "/user_{userId}/info.html", method = RequestMethod.GET)  
    public ModelAndView info( @PathVariable("userId") int userId, HttpServletRequest request ) {
        User user = this.usersManager.getUserById(userId);

        ModelAndView mav = new ModelAndView();
        mav.setViewName( "userInfo" );
        mav.addObject( "user", user );
        mav.addObject( "pageName", new RequestContext(request).getMessage( "page.title.userInfo" ) +" "+ user.getLogin() );

        return mav;
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
