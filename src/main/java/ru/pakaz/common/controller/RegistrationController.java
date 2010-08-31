package ru.pakaz.common.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import ru.pakaz.common.dao.UserDao;
import ru.pakaz.common.model.User;

@Controller
public class RegistrationController {
    static private Logger logger = Logger.getLogger( RegistrationController.class );

    @Autowired
    private UserDao usersManager;

    protected Object formBackingObject(HttpServletRequest request) {
        return new User();
    }

    @RequestMapping(value = "/registration.html", method = RequestMethod.GET)
    public ModelAndView showForm( HttpServletRequest request, HttpServletResponse response ) {
        ModelAndView mav = new ModelAndView( "registration" );
        mav.addObject( "user", new User() );
        return mav;
    }

    @RequestMapping(value = "/registration.html", method = RequestMethod.POST)
    public ModelAndView register( @ModelAttribute("user") User user, BindingResult result, 
            HttpServletRequest request, HttpServletResponse response ) {

        new UserInfoValidator().validate( user, result );
        
        User dbUser = usersManager.getUserByLogin( user.getLogin() );
        if( dbUser != null ) {
            result.rejectValue( "login", "error.user.login.exists" );
            logger.debug("User with login "+ user.getLogin() +" exists!");
        }
        
        logger.debug("User with login "+ user.getLogin() +" does not exists");

        ModelAndView mav = new ModelAndView();
        if( !result.hasFieldErrors("login") && !result.hasFieldErrors("email") ) {
            user.setNickName( user.getLogin() );
            user.setTemporary(true);
            user.setBlocked(true);
            
            if( result.hasFieldErrors("password") && result.getFieldError("password").getCode().equals("error.login.emptyPasswd") ) {
                String newPass = RandomStringUtils.randomAlphanumeric(8);
                user.setPassword( newPass );
                logger.debug("Password: "+ newPass);
                
                result.recordSuppressedField("password");
            }

            logger.debug("Try to send e-mail message…");
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
            mav.addObject( "user", user );
        }

        return mav;
    }

    public boolean sendEmailMessage( User recipient ) {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();

        sender.setDefaultEncoding("UTF-8");
/*
        Properties props = new Properties();

		props.put("mail.smtp.host", "smtp.googlemail.com");
		props.put("mail.smtp.auth", "true");
		
		props.put("mail.smtp.starttls.enable", "true");
		
		props.put("mail.debug", "true");
		props.put("mail.smtp.user", "pv.kazantsev@gmail.com");
		props.put("mail.smtp.password", "frankwilson");
		props.put("mail.smtp.port", "465" );
		
		props.put.put("mail.smtp.localhost", "pakaz.ru" );

		Authenticator auth = new SMTPAuthenticator();
		
		Session session = Session.getDefaultInstance(props, auth);
		session.setDebug(true);
        sender.setSession(session);
*/
        MimeMessage emess = sender.createMimeMessage();

        ClassPathResource ctx = new ClassPathResource("registrationMessage.html");
        StringBuilder message = new StringBuilder();

        if( ctx.exists() ) {
            logger.debug("Reading mail message template…");
            try {
                MimeMessageHelper helper = new MimeMessageHelper(emess, true);
                helper.setTo( recipient.getEmail() );
                helper.setFrom( "Photo.Pakaz.Ru <wilson@pakaz.ru>" );
                helper.setSubject( "Registering on photo.pakaz.ru" );

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

                logger.debug( "Mail message: \n"+ message.toString() );
//                helper.setText( javax.mail.internet.MimeUtility.encodeText(message.toString(), "UTF-8", null), true);
                helper.setText( message.toString(), true);
                
                logger.debug( helper.getEncoding() );
                
                sender.send(emess);
                return true;
            }
            catch(MailException ex) {
                logger.debug("Email was not sent:");
                logger.error( ex.getMessage() );
            }
            catch (IOException e) {
                logger.debug("Generic IOException:");
                logger.error( e.getMessage() );
            } catch (MessagingException e) {
                logger.debug("Can't attach message to email:");
                logger.error( e.getMessage() );
            }
        }
        else {
            logger.error("there is no template file!");
        }
        
        return false;
    }
/*
	private class SMTPAuthenticator extends javax.mail.Authenticator
	{
		public PasswordAuthentication getPasswordAuthentication()
		{
			String username = "pv.kazantsev@gmail.com";
			String password = "frankwilson";

			return new PasswordAuthentication(username, password);
		}
	}*/
}
