package ru.pakaz.common.controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.springframework.validation.Errors;
import ru.pakaz.common.model.User;

public class UserInfoValidator {
    private Logger logger = Logger.getLogger( UserInfoValidator.class );
    
    private final String EMAIL_PATTERN = "^[-a-z0-9!#$%&'*+/=?^_`{|}~]+(?:\\.[-a-z0-9!#$%&'*+/=?^_`{|}~]+)*@(?:[a-z0-9]([-a-z0-9]{0,61}[a-z0-9])?\\.)"+
    	"(aero|arpa|asia|biz|cat|com|coop|edu|gov|info|int|jobs|mil|mobi|museum|name|net|org|pro|tel|travel|[a-z][a-z]|рф)$";

    public void validate(Object command, Errors errors) {
        User user = (User) command;
        if( user == null ) {
            return;
        }

        String email     = user.getEmail();
        String password  = user.getPassword();
        String login     = user.getLogin();

        if( email == null || email.length() == 0 ) {
            logger.error( "Ошибка валидации e-mail! адрес пустой!" );
            errors.rejectValue( "email", "error.user.email.tooShort" );
        }
        else if( !this.isEmailValid( email ) ) {
            logger.error( "Ошибка валидации e-mail! адрес ( "+ email +" ) не соответствует маске!" );
            errors.rejectValue( "email", "error.user.email.formatError" );
        }
        else {
            logger.debug( "Email: "+ email );
        }
        
        if( login == null || login.length() < 3) {
        	logger.error( "Ошибка валидации логина! он пустой или слишком коротки (до 3-х символов)!" );
        	errors.rejectValue(login, "error.user.login.tooShort");
        }
        
        if( password != null && password.length() > 0 ) {
            if( password.length() < 6 ) {
                logger.error( "Ошибка валидации пароля! Длина пароля меньше 6 символов!" );
                errors.rejectValue( "password", "error.user.passwd.tooShort" );
            }
        }
    }
    
    public boolean isEmailValid( String email ) {
    	Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    	Matcher matcher = pattern.matcher(email);
		return matcher.matches();
    }
}
