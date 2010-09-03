package ru.pakaz.common.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.pakaz.common.dao.UserDao;
import ru.pakaz.common.model.User;

@Controller
public class LoginController {
	static private Logger logger = Logger.getLogger( LoginController.class );

    @Autowired
    private UserDao usersManager;

    @RequestMapping(value = "/login.html", method = RequestMethod.GET)
    public ModelAndView get( HttpServletRequest request, @RequestParam(value="activation", required=false) String activation ) {
        ModelAndView mav = new ModelAndView();
        mav.addObject( "user", new User() );

        if( activation != null && activation.length() > 0 ) {
            logger.debug( "Activation code is: "+ activation );
            
            User user = usersManager.getUserByActivationCode(activation);
            if( user == null ) {
                logger.debug( "Activation code is incorrect" );

                // Активационный код недействителен
                mav.addObject( "activationResult", false );
            }
            else {
                logger.debug( "Activation code is correct and is owned by "+ user.getLogin() );

                // Активационный код найден
                user.setActivationCode(null);
                user.setTemporary(false);
                user.setBlocked(false);

                this.usersManager.updateUser(request, user);
                mav.addObject( "activationResult", true );
            }
        }
        
        mav.setViewName("login");

        mav.addObject( "pageName", "Логин" );
        return mav;
    }
}
