package ru.pakaz.photo.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContext;
import ru.pakaz.common.dao.UserDao;
import ru.pakaz.photo.dao.AlbumDao;
import ru.pakaz.photo.model.Album;

@Controller
public class PhotoUploadController {
    private Logger logger = Logger.getLogger( PhotoUploadController.class );

    @Autowired
    private UserDao usersManager;
    @Autowired
    private AlbumDao albumsManager;

    /**
     * Загрузка фотографии в определенный параметром albumId альбом
     * 
     * @param albumId
     * @param request
     * @return
     */
    @RequestMapping(value = "/album_{albumId}/upload.html", method = RequestMethod.GET)
    public ModelAndView getWithAlbum( @PathVariable("albumId") int albumId, HttpServletRequest request ) {
        ModelAndView mav = new ModelAndView( "uploadPhoto" );
        Album album = this.albumsManager.getAlbumsById( albumId );
        mav.addObject( "album", album );
        mav.addObject( "pageName", new RequestContext(request).getMessage( "page.title.createAlbum" ) );

        return mav;
    }

    /**
     * Загрузка фотографии в определенный параметром albumId альбом
     * 
     * @param albumId
     * @param request
     * @return
     */
    @RequestMapping(value = "/upload.html", method = RequestMethod.GET)
    public ModelAndView get( HttpServletRequest request ) {
        ModelAndView mav = new ModelAndView( "uploadPhoto" );
        mav.addObject( "pageName", new RequestContext(request).getMessage( "page.title.createAlbum" ) );

        return mav;
    }

    /**
     * Загрузка фотографии во временный альбом
     * 
     * @param request
     * @return
     */
    @RequestMapping(value = "/upload.html", method = RequestMethod.POST)
    public ModelAndView upload( HttpServletRequest request, ServletRequestDataBinder binder,
            @RequestParam("file") MultipartFile file  ) {
        
        String tmpPath = request.getSession().getServletContext().getInitParameter( "catalog" ) +"/tmp";

        if( !file.isEmpty() ) {
            try {
                this.uploadPhoto( tmpPath, file.getBytes() );
                this.logger.debug( "File is not empty" );
            }
            catch( IOException e ) {
                this.logger.debug( "Exeption during reading file!" );
                e.printStackTrace();
            }
        } else {
            this.logger.debug( "File is empty" );
        }

        try {
            ModelAndView mav;
            mav = new ModelAndView( "uploadPhoto" );
            return mav;
        }
        catch( Exception e ) {
            this.logger.debug( "JUST ERROR!" );
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 
     * @param albumId
     * @param result
     * @param request
     * @return
     */
    @RequestMapping(value = "/album_{albumId}/upload.html", method = RequestMethod.POST)  
    public ModelAndView uploadWithAlbum( @PathVariable("albumId") int albumId, HttpServletRequest request, 
            HttpServletResponse response, @RequestParam("file") MultipartFile file ) {
        String tmpPath = request.getSession().getServletContext().getInitParameter( "catalog" ) +"/tmp";

        if( !file.isEmpty() ) {
            try {
                this.uploadPhoto( tmpPath, file.getBytes() );
                this.logger.debug( "File is not empty" );
            }
            catch( IOException e ) {
                this.logger.debug( "Exeption during reading file!" );
                e.printStackTrace();
            }
        } else {
            this.logger.debug( "File is empty" );
        }

        try {
            ModelAndView mav;
            mav = new ModelAndView( "uploadPhoto" );
            return mav;
        }
        catch( Exception e ) {
            this.logger.debug( "JUST ERROR!" );
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Обработка и сохранение полученной фотографии
     * 
     * @param tmpPath
     */
    private void uploadPhoto( String tmpPath, byte[] file ) {
        if( file != null ) {
            PhotoUploadController.logger.debug( "File size is "+ file.length );

            File tmpOutputDir = new File( tmpPath );
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
