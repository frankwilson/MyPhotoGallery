package ru.pakaz.common.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import ru.pakaz.common.dao.UserDao;
import ru.pakaz.common.model.User;

@Controller
public class RegistrationController {
    private UserDao usersManager;
    
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        return new User();
    }

    public ModelAndView showForm( HttpServletRequest request, HttpServletResponse response,
            BindException errors) throws Exception
    {
        ModelAndView mav = new ModelAndView( "registration" );
        User model = new User();
        mav.addObject( "user", model );
        return mav;
    }

    public void onBindAndValidate(
            HttpServletRequest request,
            Object command,
            BindException errors) throws Exception
    {
        if (errors.hasErrors()) {
            return;
        }

        User formUser = (User) command;

        if( usersManager.getUserByLogin( formUser.getLogin() ) != null ) {
            errors.reject( "error.user.login.exists" );
        }
        else {
            usersManager.setUserToSession( request, formUser );
        }
    }

    public ModelAndView onSubmit( Object command ) throws Exception {
        User formUser = (User) command;
        return new ModelAndView( "registration", "user", formUser );
    }

    public UserDao getUserDao() {
        return this.usersManager;
    }
    public void setUserDao( UserDao userDao ) {
        this.usersManager = userDao;
    }
}
