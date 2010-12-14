package ru.pakaz.common.test;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

import junit.framework.TestCase;

public class RegistrationControllerTest extends TestCase {

    public void testSendMailThroughGoogle() {
        Properties props = new Properties();

        props.put("mail.smtp.host",            "smtp.gmail.com");
        props.put("mail.smtp.auth",            "true");
        props.put("mail.smtp.starttls.enable", "true");
//        props.put("mail.smtps.user",            "wilson@pakaz.ru");
//        props.put("mail.smtps.password",        "frankw1987");
        props.put("mail.smtp.port",            "587" );
        props.put("mail.smtp.localhost",       "pakaz.ru" );

        props.put("mail.debug", "true");



        StringBuilder message = new StringBuilder();
        message.append( "Привет, мир! Это тест гугловской почты!" );

        try {
            Session mailSession = Session.getDefaultInstance(props);
/*
            Session mailSession = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() { 
                    return new PasswordAuthentication("wilson@pakaz.ru","frankw1987"); 
                }
            });*/

            mailSession.setDebug(true);
            Transport transport = mailSession.getTransport("smtp");

            MimeMessage emess = new MimeMessage(mailSession);
            emess.setSubject("Registering on photo.pakaz.ru", "UTF-8");
            emess.setText(message.toString(), "UTF-8", "html");

            emess.addRecipient(Message.RecipientType.TO, new InternetAddress("pv.kazantsev@gmail.com"));

            //transport.connect("smtp.gmail.com", 587, "wilson@pakaz.ru", "frankw1987");
            transport.connect("wilson@pakaz.ru", "frankw1987");
            //transport.connect();

            transport.sendMessage(emess, emess.getRecipients(Message.RecipientType.TO));
            transport.close();

            System.out.println( "Mail message: \n"+ message.toString() );

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
