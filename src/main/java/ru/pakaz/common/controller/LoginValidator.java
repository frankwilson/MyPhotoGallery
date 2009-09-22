package ru.pakaz.common.controller;

import org.apache.log4j.Logger;
import org.springframework.validation.Errors;
import ru.pakaz.common.dao.UserDao;
import ru.pakaz.common.model.User;

/**
 * Validator for SignInController
 * @author anil
 * @see com.visualpatterns.timex.controller.SignInController
 */
public class LoginValidator implements org.springframework.validation.Validator {
    static private Logger logger = Logger.getLogger( LoginValidator.class );
    private UserDao usersManager;
    
    public boolean supports(Class clazz) {
        return clazz.equals( User.class );
    }

    public void validate(Object command, Errors errors) {
        User user = (User) command;
        if( user == null ) {
            return;
        }

        String login    = user.getLogin();
        String password = user.getPassword();

        if( login == null ) {
            logger.debug( "LoginValidator.validate: Login is null!" );
            errors.reject( "error.login.miss" );
//            System.out.println( "Login is null!" );
        }
        else if( login.length() < 3 ) {
            logger.debug( "LoginValidator.validate: Login is too short!" );
            errors.reject( "error.login.tooShort" );
//            System.out.println( "Login is not null but too short!" );
        }
        else {
//            System.out.println( "Login is not null!" );
            if( password == null || password.trim().length() > 10 ) {
                logger.debug( "LoginValidator.validate: Password is null or too long!" );
                errors.reject( "error.login.invalid" );
            }

            User dbUser = usersManager.getUserByLogin( login );
            if( dbUser == null ) {
                logger.debug( "LoginValidator.validate: User not found!" );
                errors.reject( "error.login.invalid" );
            }
            else if( !dbUser.getPassword().contentEquals( password ) ) {
                logger.debug( "LoginValidator.validate: Password is not correct!" );
                errors.reject( "error.login.invalid" );
            }
        }
    }
    
    public void setUsersManager( UserDao usersManager ) {
        this.usersManager = usersManager;
    }
    
}
