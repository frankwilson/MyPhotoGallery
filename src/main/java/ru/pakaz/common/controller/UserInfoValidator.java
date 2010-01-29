package ru.pakaz.common.controller;

import org.apache.commons.validator.EmailValidator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import ru.pakaz.common.dao.UserDao;
import ru.pakaz.common.model.User;

public class UserInfoValidator extends EmailValidator {
    static private Logger logger = Logger.getLogger( UserInfoValidator.class );

    @Autowired
    private UserDao usersManager;
    
    public boolean supports(Class clazz) {
        return clazz.equals( User.class );
    }

    public void validate(Object command, Errors errors) {
        User user = (User) command;
        if( user == null ) {
            return;
        }

        String email     = user.getEmail();
        String password  = user.getPassword();

        if( email == null || email.length() == 0 ) {
            logger.error( "Ошибка валидации e-mail! адрес пустой!" );
            errors.rejectValue( "email", "error.user.email.tooShort" );
        }
        else if( !this.isValid( email ) ) {
            logger.error( "Ошибка валидации e-mail! адрес не соответствует маске!" );
            errors.rejectValue( "email", "error.user.email.formatError" );
        }
        else {
            logger.debug( "Email: "+ email );
        }
        
        if( password != null && password.length() > 0 ) {
            if( password.length() < 8 ) {
                logger.error( "Ошибка валидации пароля! Длина пароля меньше 8 символов!" );
                errors.rejectValue( "password", "error.user.passwd.tooShort" );
            }
        }
    }
    
    public void setUsersManager( UserDao usersManager ) {
        this.usersManager = usersManager;
    }
}
