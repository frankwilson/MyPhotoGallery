package ru.pakaz.common.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

import junit.framework.TestCase;
import ru.pakaz.common.controller.RegistrationController;
import ru.pakaz.common.model.User;

public class RegistrationControllerTest extends TestCase {
/*
    public void testSendEmail() {
        RegistrationController controller = new RegistrationController();
        
        User recipient = new User();
        recipient.setNickName( "Kolya" );
        recipient.setEmail( "pv.kazantsev@gmail.com" );
        
//        controller.sendEmailMessage(recipient);
    }
*/
	public void testSendMailThroughGoogle() {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();

        sender.setDefaultEncoding("UTF-8");

        Properties props = new Properties();

		props.put("mail.smtp.host", "smtp.googlemail.com");
		props.put("mail.smtp.auth", "true");
		
		props.put("mail.smtp.starttls.enable", "true");
		
		props.put("mail.debug", "true");
		props.put("mail.smtp.user", "pv.kazantsev@gmail.com");
		props.put("mail.smtp.password", "frankwilson");
		props.put("mail.smtp.port", "465" );
		
		props.put("mail.smtp.localhost", "pakaz.ru" );

		Authenticator auth = new SMTPAuthenticator();
		
		Session session = Session.getDefaultInstance(props, auth);
		session.setDebug(true);
        sender.setSession(session);

        MimeMessage emess = sender.createMimeMessage();

        StringBuilder message = new StringBuilder();
        message.append( "Привет, мир! Это тест гугловской почты!" );
        
        try {
            MimeMessageHelper helper = new MimeMessageHelper(emess, true);
            helper.setTo( "pv.kazantsev@gmail.com" );
            helper.setFrom( "Photo.Pakaz.Ru <wilson@pakaz.ru>" );
            helper.setSubject( "Registering on photo.pakaz.ru" );

            System.out.println( "Mail message: \n"+ message.toString() );
//                helper.setText( javax.mail.internet.MimeUtility.encodeText(message.toString(), "UTF-8", null), true);
            helper.setText( message.toString(), true);
            
            System.out.println( helper.getEncoding() );
            
            sender.send(emess);
        }
        catch(MailException ex) {
        	System.out.println("Email was not sent:");
        	System.out.println( ex.getMessage() );
        } catch (MessagingException e) {
        	System.out.println("Can't attach message to email:");
        	System.out.println( e.getMessage() );
        }
	}
	
	public void getMd5Password() {
		Md5PasswordEncoder encoder = new Md5PasswordEncoder();
        String password = encoder.encodePassword( "125448", "vasya" );
        System.out.println(password);
	}

	private class SMTPAuthenticator extends javax.mail.Authenticator
	{
		public PasswordAuthentication getPasswordAuthentication()
		{
			String username = "pv.kazantsev@gmail.com";
			String password = "frankwilson";

			return new PasswordAuthentication(username, password);
		}
	}
}
