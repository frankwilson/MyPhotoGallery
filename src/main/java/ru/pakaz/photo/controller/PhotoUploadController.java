package ru.pakaz.photo.controller;

import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContext;
import ru.pakaz.common.dao.UserDao;
//import ru.pakaz.common.model.User;
import ru.pakaz.photo.dao.AlbumDao;
import ru.pakaz.photo.dao.PhotoDao;
import ru.pakaz.photo.model.Album;
import ru.pakaz.photo.model.Photo;
//import ru.pakaz.photo.model.PhotoFile;
import ru.pakaz.photo.service.PhotoFileService;

@Controller
public class PhotoUploadController {
    private Logger logger = Logger.getLogger( PhotoUploadController.class );

    @Autowired
    private UserDao usersManager;
    @Autowired
    private AlbumDao albumsManager;
    @Autowired
    private PhotoDao photoManager;
    @Autowired
    private PhotoFileService photoFileService;

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
        ArrayList<Album> albums = this.albumsManager.getAlbumsByUser( this.usersManager.getUserFromSession( request ) );
        Album album = this.albumsManager.getAlbumById( albumId );
        mav.addObject( "albums", albums );
        mav.addObject( "currentAlbum", album );
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
        ArrayList<Album> albums = this.albumsManager.getAlbumsByUser( this.usersManager.getUserFromSession( request ) );
        mav.addObject( "albums", albums );
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
    public ModelAndView upload( HttpServletRequest request, HttpServletResponse response, 
            @RequestParam("file") MultipartFile file  ) {

        if( !file.isEmpty() ) {
            this.logger.debug( "File is not empty" );

            try {
                Photo newPhoto = new Photo();
                newPhoto.setUser( this.usersManager.getUserFromSession(request) );
                newPhoto.setFileName( file.getOriginalFilename() );
                newPhoto.setTitle( file.getOriginalFilename() );
                
                if( request.getParameter("album") != null ) {
                	try {
                		Album album = this.albumsManager.getAlbumById( Integer.parseInt( request.getParameter("album") ) );
                    	if( album != null ) {
                    		newPhoto.setAlbum(album);
                    	}
                	}
                	catch (NumberFormatException nfe) {
                		this.logger.error("Error while converting albumId to int");
					}
                }
                
                this.photoManager.createPhoto( newPhoto );
                this.logger.debug("We've created new Photo with ID "+ newPhoto.getPhotoId());
                this.photoFileService.savePhoto( file.getBytes(), newPhoto );
                
                int oldUnallocPhotosCount = Integer.parseInt(
                        request.getSession().getAttribute("unallocatedPhotosCount").toString()
                    );
                request.getSession().setAttribute( "unallocatedPhotosCount", oldUnallocPhotosCount + 1 );
            }
            catch( IOException e ) {
                this.logger.debug( "Exception during reading sent file!" );
                e.printStackTrace();
            }
        }
        else {
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
     * 
     * @param albumId
     * @param result
     * @param request
     * @return
     */
    @RequestMapping(value = "/album_{albumId}/upload.html", method = RequestMethod.POST)  
    public ModelAndView uploadWithAlbum( @PathVariable("albumId") int albumId, HttpServletRequest request,
            HttpServletResponse response, @RequestParam("file") MultipartFile file ) {

        if( !file.isEmpty() ) {
            this.logger.debug( "File is not empty" );

            try {
                Album album = this.albumsManager.getAlbumById(albumId);
                
                Photo newPhoto = new Photo();
                newPhoto.setUser( this.usersManager.getUserFromSession(request) );
                newPhoto.setAlbum(album);
                newPhoto.setTitle( file.getOriginalFilename() );
                newPhoto.setFileName( file.getOriginalFilename() );

                this.photoManager.createPhoto( newPhoto );
                this.logger.debug("We've created new Photo with ID "+ newPhoto.getPhotoId());
                this.photoFileService.savePhoto( file.getBytes(), newPhoto );
//                this.photoManager.updatePhoto( newPhoto );
            }
            catch( IOException e ) {
                this.logger.debug( "Exception during reading sent file!" );
                e.printStackTrace();
            }
        }
        else {
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

    public void setAlbumDao( AlbumDao albumDao ) {
        this.albumsManager = albumDao;
    }
    public void setUserDao( UserDao userDao ) {
        this.usersManager = userDao;
    }
    public void setPhotoDao( PhotoDao photoDao ) {
        this.photoManager = photoDao;
    }
    public void setPhotoFileService( PhotoFileService photoFileService ) {
        this.photoFileService = photoFileService;
    }
}
