package ru.pakaz.common.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

/**
 * Класс для отправки сообщений электронной почты
 * 
 * @author wilson
 *
 */
public class MailService {
    static private Logger logger = Logger.getLogger( MailService.class );

    static private MailService instance = null;
    static private String MAIL_SERVER;
    static private String MAIL_PORT;
    static private String MAIL_USERNAME;
    static private String MAIL_PASSWORD;
    static private String MAIL_SRC_ADDR;
    
    private MailService( String server, String port, String username, String password, String srcAddr ) throws Exception {
        if( server != null && server.length() > 0 ) {
            MailService.MAIL_SERVER = server;
        }
        else 
            throw new Exception( "server address is null or empty" );
        
        if( port != null && port.length() > 0 ) {
            MailService.MAIL_PORT = port;
        }
        else 
            throw new Exception( "server port is null or empty" );
        
        if( username != null && username.length() > 0 ) {
            MailService.MAIL_USERNAME = username;
        }
        else 
            throw new Exception( "username is null or empty" );
        
        if( password != null && password.length() > 0 ) {
            MailService.MAIL_PASSWORD = password;
        }
        else 
            throw new Exception( "password is null or empty" );
        
        if( srcAddr != null && srcAddr.length() > 0 ) {
            MailService.MAIL_SRC_ADDR = srcAddr;
        }
        else 
            throw new Exception( "source address is null or empty" );
    }
    
    static public MailService getInstance() {
        if( MailService.instance == null ) {
            Properties properties = new Properties();
            ClassPathResource ctx = new ClassPathResource("settings.properties");
            try {
                properties.load( new InputStreamReader( ctx.getInputStream(), "UTF-8" ) );

                MailService.instance = new MailService(
                        properties.get( "mail.server" ).toString(),
                        properties.get( "mail.port" ).toString(),
                        properties.get( "mail.username" ).toString(),
                        properties.get( "mail.password" ).toString(),
                        properties.get( "mail.srcAddress" ).toString()
                );
            }
            catch( IOException e ) {
                logger.error( "Can't load mail properties:\n"+ e.getMessage() );
            }
            catch( Exception e ) {
                logger.error( "Can't get instance:\n"+ e.getMessage() );
            }
        }
        
        return MailService.instance;
    }
    
    /**
     * Метод принимает текст сообщения, e-mail адрес назначения и тему сообщения
     * и отправляет через гугловый почтовый сервер
     * 
     * @param message
     * @param dstAddress
     * @param subject
     * @return
     */
    public boolean sendMailMessage( String message, String dstAddress, String subject ) {

        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setDefaultEncoding("UTF-8");
        MimeMessage emess = sender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(emess, true);
            helper.setTo( dstAddress );
            helper.setFrom( MAIL_SRC_ADDR );
            helper.setSubject( subject );

            logger.debug( "Mail message: \n"+ message );
            helper.setText( message, true);
        }
        catch( MailException ex ) {
            logger.error( "Email was not sent:\n"+ ex.getMessage() );
            return false;
        }
        catch( MessagingException e ) {
            logger.error( "Can't attach message to email:\n "+ e.getMessage() );
            return false;
        }
        
        setGoogleParams( sender );

        try {
            sender.send(emess);
            return true;
        }
        catch( MailException e ) {
            logger.error( "Email was not sent:\n"+ e.getMessage() );
            return false;
        }
    }
    
    /**
     * Настройки для отправки через гугловый почтовый сервер
     * 
     * @param sender
     */
    private void setGoogleParams( JavaMailSenderImpl sender ) {

        Properties props = new Properties();

        props.put("mail.smtps.host",     MAIL_SERVER );
        props.put("mail.smtps.user",     MAIL_USERNAME);
        props.put("mail.smtps.password", MAIL_PASSWORD);
        props.put("mail.smtps.port",     MAIL_PORT );
        props.put("mail.smtps.auth", "true" );
        props.put("mail.smtps.starttls.enable", "true" );

        props.put("mail.debug", "true");

        props.put("mail.smtp.localhost", "pakaz.ru" );

        Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication( MAIL_USERNAME, MAIL_PASSWORD ); 
            }
        });

        sender.setSession(session);
        session.setDebug(true);
    }
}
