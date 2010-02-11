package ru.pakaz.photo.controller;

import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContext;
import ru.pakaz.common.dao.UserDao;
import ru.pakaz.photo.dao.PhotoDao;
import ru.pakaz.photo.model.Album;
import ru.pakaz.photo.model.Photo;
import ru.pakaz.photo.service.PhotoFileService;

@Controller
public class PhotosListController {
    private Logger logger = Logger.getLogger( PhotosListController.class );
    
    @Autowired
    private UserDao usersManager;
    @Autowired
    private PhotoDao photoManager;
    @Autowired
    private PhotoFileService photoFileService;

    @RequestMapping(value = "/unallocatedPhotos.html", method = RequestMethod.GET)
    public ModelAndView showUnallocatedPhotosList( HttpServletRequest request ) {
        ModelAndView mav = new ModelAndView( "photosList" );

        ArrayList<Photo> photos = (ArrayList<Photo>)this.photoManager.getUnallocatedPhotos( this.usersManager.getUserFromSession( request ) );
        logger.debug( "Unallocated photos count is "+ photos.size() );

        Album album = new Album();
        album.setTitle( new RequestContext(request).getMessage( "page.title.unallocatedPhotos" ) );
        album.setDescription( new RequestContext(request).getMessage( "page.description.unallocatedPhotos" ) );
        
        mav.addObject( "album", album );
        mav.addObject( "photos", photos );
        mav.addObject( "pageName", new RequestContext(request).getMessage( "page.title.unallocatedPhotos" ) );

        return mav;
    }
/*
    @RequestMapping(value = "/album_{albumId}.html", method = RequestMethod.GET)
    public ModelAndView showPhotosListByAlbum( @PathVariable("albumId") int albumId, HttpServletRequest request ) {
        ModelAndView mav = new ModelAndView( "uploadPhoto" );
        mav.addObject( "pageName", new RequestContext(request).getMessage( "page.title.viewAlbum" ) );

        return mav;
    }
*/
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
