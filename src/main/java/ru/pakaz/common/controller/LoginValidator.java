package ru.pakaz.common.controller;

import org.apache.log4j.Logger;
import org.springframework.validation.Errors;
import ru.pakaz.common.dao.UserDao;
import ru.pakaz.common.model.User;

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
//        logger.debug( "We got a form \"User\" object" );

        if( login == null ) {
            logger.debug( "LoginValidator.validate: Login is null!" );
            errors.rejectValue( "login", "error.login.miss" );
        }
        else if( login.length() < 3 ) {
            logger.debug( "LoginValidator.validate: Login is too short!" );
            errors.rejectValue( "login", "error.login.tooShort" );
        }
        else {
            if( password == null || password.trim().length() == 0 ) {
                logger.debug( "LoginValidator.validate: Password is empty!" );
                errors.rejectValue( "password", "error.login.emptyPasswd" );
            }
            if( password.trim().length() > 10 ) {
                logger.debug( "LoginValidator.validate: Password is too long!" );
                errors.rejectValue( "password", "error.login.tooLongPasswd" );
            }

            User dbUser = usersManager.getUserByLogin( login );
//            logger.debug( "We got a DB \"User\" object" );
            if( dbUser == null ) {
                logger.debug( "LoginValidator.validate: User not found!" );
                errors.rejectValue( "login", "error.login.invalid.user" );
            }
            else if( !dbUser.getPassword().contentEquals( password ) ) {
                logger.debug( "LoginValidator.validate: Password is not correct!" );
                errors.rejectValue( "password", "error.login.invalid.pass" );
            }
        }
        
        logger.debug( "LoginValidator.validate: validation of user '"+ login +"' is OK!" );
    }
    
    public void setUsersManager( UserDao usersManager ) {
        this.usersManager = usersManager;
    }
    
}
