package ru.pakaz.common.controller;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import ru.pakaz.common.dao.UserDao;
import ru.pakaz.common.model.User;

public class RegistrationController extends SimpleFormController
{
    private UserDao usersManager;
    
    public RegistrationController() {
        setCommandClass( User.class );
        setCommandName( "user" );
    }
    
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        return new User();
    }

    public ModelAndView showForm( HttpServletRequest request, HttpServletResponse response,
            BindException errors, Map controlModel ) throws Exception
    {
        ModelAndView mav = new ModelAndView( getFormView() );
        User model = new User();
        mav.addObject( "user", model );
//        return super.showForm(request, response, errors, controlModel);
//        ModelAndView mav = new ModelAndView( getFormView(), new ModelMap( "user", new User() ) );
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

    public ModelAndView onSubmit(
            HttpServletRequest request,
            HttpServletResponse response,
            Object command,
            BindException errors) throws Exception
    {
        User formUser = (User) command;

        return new ModelAndView( getSuccessView(), "user", formUser );
    }

    public UserDao getUserDao() {
        return this.usersManager;
    }
    public void setUserDao( UserDao userDao ) {
        this.usersManager = userDao;
    }
}
