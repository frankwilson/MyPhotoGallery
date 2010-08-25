package ru.pakaz.common.controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import ru.pakaz.common.model.User;

public class UserInfoValidator implements org.springframework.validation.Validator {
    private Logger logger = Logger.getLogger( UserInfoValidator.class );

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals( User.class );
    }
    
    private final String EMAIL_PATTERN = "^[-a-z0-9!#$%&'*+/=?^_`{|}~]+(?:\\.[-a-z0-9!#$%&'*+/=?^_`{|}~]+)*@(?:[a-z0-9]([-a-z0-9]{0,61}[a-z0-9])?\\.)"+
        "(aero|arpa|asia|biz|cat|com|coop|edu|gov|info|int|jobs|mil|mobi|museum|name|net|org|pro|tel|travel|[a-z][a-z]|рф)$";
    
    private final String LOGIN_PATTERN = "^[a-zA-Zа-яА-Я0-9-_ .]{0,16}$";

    public void validate(Object command, Errors errors) {
        User user = (User) command;
        if( user == null ) {
            return;
        }

        String email     = user.getEmail();
        String password  = user.getPassword();
        String login     = user.getLogin();

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "error.user.email.tooShort");
        if( !errors.hasFieldErrors("email") && !this.isEmailValid( email ) ) {
            logger.error( "Ошибка валидации e-mail! адрес ( "+ email +" ) не соответствует маске!" );
            errors.rejectValue( "email", "error.user.email.formatError" );
        }
        else {
            logger.debug( "Email: "+ email );
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "login", "error.login.miss");
        if( !errors.hasFieldErrors("login") && (login == null || login.length() < 3)) {
            logger.error( "Ошибка валидации логина! он пустой или слишком коротки (до 3-х символов)!" );
            errors.rejectValue(login, "error.user.login.tooShort");
        }
        else if( !login.matches(LOGIN_PATTERN) ) {
            logger.error( "Ошибка валидации логина! Присутствуют недопустимые символы!" );
            errors.rejectValue(login, "error.user.login.incorrect");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "error.login.emptyPasswd");
        if( !errors.hasFieldErrors("password") && (password == null || password.length() < 6) ) {
            logger.error( "Ошибка валидации пароля! Длина пароля меньше 6 символов!" );
            errors.rejectValue( "password", "error.user.passwd.tooShort" );
        }
        
        String firstName = user.getFirstName();
        if( firstName != null && firstName.length() > 0 ) {
            // TODO validate
        }
        
        String lastName = user.getLastName();
        if( lastName != null && lastName.length() > 0 ) {
            // TODO validate
        }
        
        String nickName = user.getNickName();
        if( nickName != null && nickName.length() > 0 ) {
            // TODO validate
        }
    }
    
    public boolean isEmailValid( String email ) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
