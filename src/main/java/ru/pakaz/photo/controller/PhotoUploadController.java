package ru.pakaz.photo.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContext;
import ru.pakaz.common.dao.UserDao;
import ru.pakaz.photo.dao.AlbumDao;
import ru.pakaz.photo.model.Album;
import ru.pakaz.photo.model.FileUploadBean;
import ru.pakaz.photo.model.Photo;

@Controller
public class PhotoUploadController {
    static private Logger logger = Logger.getLogger( PhotoUploadController.class );

    @Autowired
    private UserDao usersManager;
    @Autowired
    private AlbumDao albumsManager;

    @RequestMapping(value = "/album_(*:albumId)/upload.html", method = RequestMethod.GET)
    public ModelAndView get( @RequestParam("albumId") int albumId, HttpServletRequest request ) {
        ModelAndView mav = new ModelAndView( "photoUploadForm" );
//        Album album = this.albumsManager.getAlbumsById( albumId );
//        mav.addObject( "album", album );
//        mav.addObject( "photo", new Photo() );
        mav.addObject( "pageName", new RequestContext(request).getMessage( "page.title.createAlbum" ) );

        return mav;
    }

    @RequestMapping(value = "/album_(*:albumId)/upload.html", method = RequestMethod.POST)  
    public ModelAndView post( @RequestParam("albumId") int albumId, BindingResult result, HttpServletRequest request ) {    
        FileUploadBean bean = new FileUploadBean();

        byte[] file = bean.getFile();
        if( file != null ) {
            PhotoUploadController.logger.debug( "File size is "+ file.length );
            
            File tmpOutputDir = new File( request.getSession().getServletContext().getInitParameter( "catalog" ) +"/tmp" );
            File tmpOutputFile;
            try {
                tmpOutputFile = File.createTempFile( "photo_", ".jpg", tmpOutputDir );
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
            catch( IOException e1 ) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
        else {
            PhotoUploadController.logger.debug( "There is no file!" );
        }

        try {
            ModelAndView mav;
            mav = new ModelAndView( "photoUploadForm" );
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

    public AlbumDao getAlbumDao() {
        return this.albumsManager;
    }
    public void setAlbumDao( AlbumDao albumDao ) {
        this.albumsManager = albumDao;
    }

    public UserDao getUserDao() {
        return this.usersManager;
    }
    public void setUserDao( UserDao userDao ) {
        this.usersManager = userDao;
    }
}
