package ru.pakaz.common.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContext;

import ru.pakaz.common.dao.UserDao;
import ru.pakaz.common.model.User;
import ru.pakaz.common.service.MailService;

@Controller
public class PasswordRestoreController {
    static private Logger logger = Logger.getLogger( PasswordRestoreController.class );

    @Autowired
    private UserDao usersManager;
    
    @RequestMapping(value = "/passwordRestore.html", method = RequestMethod.GET)
    public ModelAndView getForm( HttpServletRequest request, @RequestParam(value="reset", required=false) String reset ) {
        ModelAndView mav = new ModelAndView( "passwordRestore" );

        if( reset != null && reset.length() > 0 ) {
            logger.debug( "Reset code is: "+ reset );
            User user = usersManager.getUserByActivationCode(reset);
            if( user == null ) {
                mav.addObject( "resetCodeResult", false );
            }
            else {
                logger.debug( "Reset code is correct and is owned by "+ user.getLogin() );
                mav.setViewName( "passwordChange" );
            }
        }
        
        mav.addObject( "user", new User() );
        
        mav.addObject( "pageName", new RequestContext(request).getMessage("page.title.restorePassword") );
        return mav;
    }
    
    @RequestMapping(value = "/passwordRestore.html", method = RequestMethod.POST)
    public ModelAndView checkForm( @ModelAttribute("user") User user, BindingResult result,
            HttpServletRequest request, @RequestParam(value="reset", required=false) String reset ) {
        ModelAndView mav = new ModelAndView( "passwordRestore" );
        
        // Если есть код сброса пароля — проверяем пользователя и пароль
        if( reset != null && reset.length() > 0 ) {
            User dbUser = usersManager.getUserByActivationCode(reset);
            if( dbUser == null ) {
                logger.debug( "Reset code is incorrect" );

                // Активационный код недействителен
                mav.addObject( "resetCodeResult", false );
            }
            else {
                logger.debug( "Reset code is correct and is owned by "+ dbUser.getLogin() );
                
                new UserInfoValidator().validate( user, result );
                if( !result.hasFieldErrors("password") ) {
                    dbUser.setPassword( user.getPlainPassword() );

                    // Активационный код найден
                    dbUser.setActivationCode(null);
                    dbUser.setTemporary(false);
                    dbUser.setBlocked(false);
                    
                    this.usersManager.updateUser(request, dbUser);
                    mav.addObject( "resetCodeResult", true );
                    user.setPassword(null);
                }

                mav.setViewName( "passwordChange" );
            }
        }
        else {
            new UserInfoValidator().validate( user, result );
            User userByLogin = null;
            User userByEmail = null;
            User currentUser = null;

            // Если у нас есть имя пользователя — проверяем его
            if( user.getLogin() != null && !user.getLogin().equals("") ) {
                userByLogin = usersManager.getUserByLogin( user.getLogin() );
                if( userByLogin == null ) {
                    result.rejectValue( "login", "error.user.doesNotExists" );
                    logger.debug("User with login "+ user.getLogin() +" does not exist");
                }
            }

            // Если у нас есть адрес электронной почты — проверяем его
            if( user.getEmail() != null && !user.getEmail().equals("") && !result.hasFieldErrors("email") ) {
                userByEmail = usersManager.getUserByEmail( user.getEmail() );
                if( userByEmail == null ) {
                    result.rejectValue( "email", "error.user.emailDoesNotExists" );
                    logger.debug("User with email "+ user.getEmail() +" does not exist!");
                }
            }
    
            if( result.hasFieldErrors("email") && result.getFieldError("email").getCode().equals("error.user.email.formatError") ) {
                
            }
            else if( userByLogin != null && userByEmail != null ) {
                if( userByLogin.getLogin().equals( userByEmail.getLogin() ) ) {
                    currentUser = userByLogin;
                }
                else {
                    result.reject("error.user.email.doesNotBelong");
                }
            }
            else if( userByLogin == null && userByEmail != null ) {
                currentUser = userByEmail;
            }
            else if( userByLogin != null && userByEmail == null ) {
                if( userByLogin.getEmail() == null || userByLogin.getEmail().equals("") ) {
                    result.reject("error.user.email.isEmpty");
                }
                else {
                    currentUser = userByLogin;
                }
            }
    
            if( currentUser != null ) {
/*
                currentUser.setTemporary(true);
                currentUser.setBlocked(true);

                if( this.sendEmailMessage(currentUser) ) {
                    logger.debug("Message sent");
                    this.usersManager.updateUser( request, currentUser );
                    mav.addObject( "sendMessageResult", true );
                }
                else {
                    logger.debug("Message not sent");
                    mav.addObject( "sendMessageResult", false );
                }
*/
            }
        }

        mav.addObject( "pageName", new RequestContext(request).getMessage( "page.title.restorePassword" ) );
        return mav;
    }

    /**
     * Метод, отправляющий письмо с магической ссылкой на восстановление пароля
     * 
     * @param recipient
     * @return
     */
    public boolean sendEmailMessage( User recipient ) {
        ClassPathResource ctx = new ClassPathResource("passwordRestoreMessage.html");
        StringBuilder message = new StringBuilder();

        if( ctx.exists() ) {
            logger.debug("Reading mail message template...");
            try {
                BufferedReader in = new BufferedReader( new InputStreamReader( ctx.getInputStream(), "UTF-8" ) );
                String temp = null;
                
                String resetCode = "";
                
                do {
                    // Циклическая генерация активационного кода
                    resetCode = RandomStringUtils.randomAlphanumeric(16);
                    logger.debug("Try to generate reset code: "+ resetCode );
                }
                while( usersManager.getUserByActivationCode(resetCode) != null );

                logger.debug("Reset code for user "+ recipient.getLogin() +" is "+ resetCode );
                recipient.setActivationCode(resetCode);

                while( (temp = in.readLine()) != null ) {
                    if( temp.contains("{USER_LOGIN}") )
                        temp = temp.replace( "{USER_LOGIN}", recipient.getLogin() );
                    if( temp.contains("{RESET_CODE}") )
                        temp = temp.replace( "{RESET_CODE}", resetCode );

                    message.append( temp + "\n" );
                }

                logger.debug("Try to send message to the \""+ recipient.getEmail() +"\"");
                // Обращение к сервису для отправки электронной почты
                MailService sender = MailService.getInstance();
                if( sender != null ) {
                    return sender.sendMailMessage( 
                            message.toString(), 
                            recipient.getEmail(), 
                            "Restoring password on photo.pakaz.ru" 
                    );
                }
                else {
                    return false;
                }
            }
            catch(MailException ex) {
                logger.debug("Email was not sent:");
                logger.error( ex.getMessage() );
            }
            catch (IOException e) {
                logger.debug("Generic IOException:");
                logger.error( e.getMessage() );
            }
        }
        else {
            logger.error("there is no template file!");
        }
        
        return false;
    }
}
