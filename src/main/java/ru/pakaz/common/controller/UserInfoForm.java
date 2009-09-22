package ru.pakaz.common.controller;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import ru.pakaz.common.dao.UserDao;
import ru.pakaz.common.model.User;

public class UserInfoForm extends SimpleFormController {
    private UserDao usersManager;
    
    public UserInfoForm() {
        setCommandClass( User.class );
        setCommandName( "user" );
    }
    
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        return new User();
    }

    public ModelAndView showForm( HttpServletRequest request, HttpServletResponse response,
            BindException errors, Map controlModel ) throws Exception
    {
        if( usersManager.getUserFromSession( request ) != null ) {
            return new ModelAndView( getSuccessView() );
        }

        return super.showForm(request, response, errors, controlModel);
    }

    public void onBindAndValidate(
            HttpServletRequest request,
            Object command,
            BindException errors) throws Exception
    {
        if( errors.hasErrors() )
            return;

        User currentUser = this.usersManager.getUserFromSession( request );
        User formUser = (User) command;
        currentUser.setNickName( formUser.getNickName() );
        currentUser.setFirstName( formUser.getFirstName() );
        currentUser.setLastName( formUser.getLastName() );
        currentUser.setEmail( formUser.getEmail() );
        
        usersManager.setUserToSession( request, currentUser );
    }

    public ModelAndView onSubmit(
            HttpServletRequest request,
            HttpServletResponse response,
            Object command,
            BindException errors) throws Exception
    {
        return new ModelAndView(getSuccessView());
    }

    public UserDao getUserDao() {
        return this.usersManager;
    }
    public void setUserDao( UserDao userDao ) {
        this.usersManager = userDao;
    }
}
