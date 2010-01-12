package ru.pakaz.photo.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;
import ru.pakaz.common.dao.UserDao;
import ru.pakaz.common.model.User;
import ru.pakaz.photo.model.FileUploadBean;
import ru.pakaz.photo.model.PhotoFile;

public class PhotoUploadController extends SimpleFormController {
    static private Logger logger = Logger.getLogger( PhotoUploadController.class );

    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response,
            Object command, BindException errors) throws ServletException, IOException {

        UserDao usersManager = new UserDao();
        User user = usersManager.getUserFromSession( request );

        if( user == null ) {
            ModelAndView mav;
//            mav = new ModelAndView( new RedirectView( "login", true), null );
            try {
                mav = super.onSubmit( request, response, command, errors );
                mav.addObject( "user", user );
                PhotoUploadController.logger.debug( "No user logged" );
                return mav;
            }
            catch( Exception e ) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        FileUploadBean bean = (FileUploadBean) command;

        byte[] file = bean.getFile();
        if( file != null ) {
            PhotoUploadController.logger.debug( "File size is "+ file.length );
            
            File tmpOutputDir = new File( request.getSession().getServletContext().getInitParameter( "catalog" ) +"/tmp" );
            File tmpOutputFile = File.createTempFile( "photo_", ".jpg", tmpOutputDir );
            
            if( tmpOutputFile.exists() && tmpOutputFile.canWrite() ) {
                FileOutputStream fileWriter;
                
                try {
                    fileWriter = new FileOutputStream( tmpOutputFile );
                    fileWriter.write( file );
                    
                    PhotoUploadController.logger.debug( "File is sucessfully saved in "+ tmpOutputFile.getAbsolutePath() );
                }
                catch( FileNotFoundException e ) {
                    e.printStackTrace();
                    PhotoUploadController.logger.warn( "Can't open stream: "+ e.getMessage() );
                }
                catch( IOException e ) {
                    e.printStackTrace();
                    PhotoUploadController.logger.warn( "Can't write file: "+ e.getMessage() );
                }
            }
            else {
                PhotoUploadController.logger.debug( "Can't create file "+ tmpOutputFile.getAbsolutePath() );
            }
        }
        else {
            PhotoUploadController.logger.debug( "There is no file!" );
        }

        try {
            ModelAndView mav;
            mav = new ModelAndView( "photoUploadForm" );
//            ModelAndView mav = super.onSubmit( request, response, command, errors );
            mav.addObject( "user", user );
            PhotoUploadController.logger.debug( "WTF!?!?!?!?" );
            return mav;
        }
        catch( Exception e ) {
            PhotoUploadController.logger.debug( "JUST ERROR!" );
            e.printStackTrace();
            return null;
        }
    }

    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder)
            throws ServletException {
        // to actually be able to convert Multipart instance to byte[]
        // we have to register a custom editor
        binder.registerCustomEditor( byte[].class, new ByteArrayMultipartFileEditor() );
        // now Spring knows how to handle multipart object and convert them
    }

}
