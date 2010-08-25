package ru.pakaz.common.controller;

import org.apache.log4j.Logger;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import ru.pakaz.common.dao.UserDao;
import ru.pakaz.common.model.User;

public class LoginValidator implements org.springframework.validation.Validator {
    static private Logger logger = Logger.getLogger( LoginValidator.class );
    private UserDao usersManager;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals( User.class );
    }

    public void validate(Object command, Errors errors) {
        User user = (User) command;
        if( user == null ) {
            return;
        }

        String login    = user.getLogin();
        String password = user.getPassword();

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "login", "error.login.miss");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "error.login.emptyPasswd");

        if( login.length() < 3 ) {
            logger.debug( "LoginValidator.validate: Login is too short!" );
            errors.rejectValue( "login", "error.login.tooShort" );
        }
        else {/*
            if( password.trim().length() > 10 ) {
                logger.debug( "LoginValidator.validate: Password is too long!" );
                errors.rejectValue( "password", "error.login.tooLongPasswd" );
            }
*/
            User dbUser = usersManager.getUserByLogin( login );

            if( dbUser == null ) {
                logger.debug( "LoginValidator.validate: User not found!" );
                errors.rejectValue( "login", "error.login.invalid.user" );
            }
            else if( dbUser.getBlocked() == true ) {
                if( dbUser.getActivationCode() != null && dbUser.getActivationCode().length() > 0 ) {
                    logger.debug( "LoginValidator.validate: User not activated!" );
                    errors.rejectValue( "login", "error.login.notActivated" );
                }
                else {
                    logger.debug( "LoginValidator.validate: User blocked!" );
                    errors.rejectValue( "login", "error.login.blocked" );
                }
            }
            else if( !dbUser.getPassword().contentEquals( password ) ) {
                logger.debug( "LoginValidator.validate: Password is not correct!" );
                errors.rejectValue( "password", "error.login.invalid.pass" );
            }
        }
        
        if( errors.hasErrors() )
            logger.debug( "validation of user '"+ login +"' is failed!" );
        else
            logger.debug( "validation of user '"+ login +"' is OK!" );
    }
    
    public void setUsersManager( UserDao usersManager ) {
        this.usersManager = usersManager;
    }
}
