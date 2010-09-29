package ru.pakaz.common.test;

import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

import junit.framework.TestCase;

public class RegistrationControllerTest extends TestCase {

    public void testSendMailThroughGoogle() {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();

        sender.setDefaultEncoding("UTF-8");

        Properties props = new Properties();

        props.put("mail.smtps.host", "smtp.gmail.com");
        props.put("mail.smtps.auth", "true");

        props.put("mail.smtps.starttls.enable", "true");

        props.put("mail.smtps.user",     "photo@pakaz.ru");
        props.put("mail.smtps.password", "frankw1987");

        props.put("mail.debug", "true");
        props.put("mail.smtps.port", "465" );

        props.put("mail.smtp.localhost", "pakaz.ru" );

        Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() 
        {
            protected PasswordAuthentication getPasswordAuthentication() { 
                return new PasswordAuthentication("photo@pakaz.ru","frankw1987"); 
            }
        });
        session.setDebug(true);
        sender.setSession(session);

        MimeMessage emess = sender.createMimeMessage();

        StringBuilder message = new StringBuilder();
        message.append( "Привет, мир! Это тест гугловской почты!" );

        try {
            MimeMessageHelper helper = new MimeMessageHelper(emess, true);
            helper.setTo( "frankw@mail.ru" );
            helper.setFrom( "Photo.Pakaz.Ru <photo@pakaz.ru>" );
            helper.setSubject( "Registering on photo.pakaz.ru" );

            System.out.println( "Mail message: \n"+ message.toString() );
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
        String password = encoder.encodePassword( "wilson", "wilson" );
        System.out.println(password);
    }
}
