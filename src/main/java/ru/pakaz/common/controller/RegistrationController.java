package ru.pakaz.common.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

import ru.pakaz.common.dao.RoleDao;
import ru.pakaz.common.dao.UserDao;
import ru.pakaz.common.model.Role;
import ru.pakaz.common.model.User;
import ru.pakaz.common.service.MailService;

@Controller
public class RegistrationController {
    static private Logger logger = Logger.getLogger( RegistrationController.class );

    @Autowired
    private UserDao usersManager;
    @Autowired
    private RoleDao roleManager;

    protected Object formBackingObject(HttpServletRequest request) {
        return new User();
    }

    @RequestMapping(value = "/registration.html", method = RequestMethod.GET)
    public ModelAndView showForm( HttpServletRequest request, HttpServletResponse response,
            @RequestParam(value="loginCheckExist", required=false) String login, 
            @RequestParam(value="emailCheckExist", required=false) String email ) {
        
        ModelAndView mav = new ModelAndView();
        
        if( login != null ) {
            logger.debug( "We have login and it's: "+ login );
            MappingJacksonJsonView view = new MappingJacksonJsonView();
            view.addStaticAttribute( "exist", this.usersManager.getUserByLogin( login ) != null );

            mav.setView( view );
        }
        else if( email != null ) {
            logger.debug( "We have email and it's: "+ email );
            MappingJacksonJsonView view = new MappingJacksonJsonView();
            view.addStaticAttribute( "exist", this.usersManager.getUserByEmail( email ) != null );

            mav.setView( view );
        }
        else {
            mav.setViewName( "registration" );
            mav.addObject( "user", new User() );
        }

        return mav;
    }

    @RequestMapping(value = "/registration.html", method = RequestMethod.POST)
    public ModelAndView register( @ModelAttribute("user") User user, BindingResult result, 
            HttpServletRequest request, HttpServletResponse response ) {

        new UserInfoValidator().validate( user, result );

        if( usersManager.getUserByLogin( user.getLogin() ) != null ) {
            result.rejectValue( "login", "error.user.login.exists" );
            logger.debug("User with login "+ user.getLogin() +" exists!");
        }
        else if( !result.hasFieldErrors("email") && usersManager.getUserByEmail( user.getEmail() ) != null ) {
            result.rejectValue( "email", "error.user.email.exists" );
            logger.debug("User with email "+ user.getEmail() +" exists!");
        }
        else
            logger.debug("User with login "+ user.getLogin() +" does not exists");

        ModelAndView mav = new ModelAndView();
        if( !result.hasFieldErrors("login") && !result.hasFieldErrors("email") ) {
            user.setNickName( user.getLogin() );
            user.setTemporary(true);
            user.setBlocked(true);
            
            Role userRole = roleManager.getRoleByName("ROLE_USER");
            Set<Role> rolesList = new HashSet<Role>();
            rolesList.add(userRole);

            user.setRoles( rolesList );
            
            if( result.hasFieldErrors("password") && result.getFieldError("password").getCode().equals("error.login.emptyPasswd") ) {
                String newPass = RandomStringUtils.randomAlphanumeric(8);
                user.setPassword( newPass );
                logger.debug("Password: "+ newPass);
                
                result.recordSuppressedField("password");
            }

            logger.debug("Try to send e-mail message...");
            if( this.sendEmailMessage(user) ) {
                // Добавляем пользователя
                this.usersManager.createUser(request, user);
                logger.debug("User created");

                mav.setViewName("registrationComplete");
            }
            else {
                mav.setViewName("registrationFailed");
            }
        }
        else {
            mav.setViewName("registration");
//            mav.addObject( "user", user );
        }

        return mav;
    }

    public boolean sendEmailMessage( User recipient ) {

        ClassPathResource ctx = new ClassPathResource("registrationMessage.html");
        StringBuilder message = new StringBuilder();

        if( ctx.exists() ) {
            logger.debug("Reading mail message template...");
            try {
                BufferedReader in = new BufferedReader( new InputStreamReader( ctx.getInputStream(), "UTF-8" ) );
                String temp = null;
                
                String activationCode = "";
                
                do {
                    // Циклическая генерация активационного кода
                    activationCode = RandomStringUtils.randomAlphanumeric(16);
                    logger.debug("Try to generate activation code: "+ activationCode );
                }
                while( usersManager.getUserByActivationCode(activationCode) != null );

                logger.debug("Activation code for user "+ recipient.getLogin() +" is "+ activationCode );
                recipient.setActivationCode(activationCode);

                while( (temp = in.readLine()) != null ) {
                    if( temp.contains("{USER_LOGIN}") )
                        temp = temp.replace( "{USER_LOGIN}", recipient.getLogin() );
                    if( temp.contains("{USER_PASSWORD}") )
                        temp = temp.replace( "{USER_PASSWORD}", recipient.getPassword() );
                    if( temp.contains("{ACTIVATION_CODE}") )
                        temp = temp.replace( "{ACTIVATION_CODE}", activationCode );

                    message.append( temp + "\n" );
                }

                // Обращение к сервису для отправки электронной почты
                MailService sender = MailService.getInstance();
                if( sender != null ) {
                    sender.sendMailMessage( 
                            message.toString(), 
                            recipient.getEmail(), 
                            "Registering on photo.pakaz.ru" 
                    );

                    return true;
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
