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
import ru.pakaz.common.model.User;
import ru.pakaz.photo.dao.AlbumDao;
import ru.pakaz.photo.dao.PhotoDao;
import ru.pakaz.photo.model.Album;
import ru.pakaz.photo.model.Photo;

@Controller
public class PhotosListController {
    private Logger logger = Logger.getLogger( PhotosListController.class );
    
    @Autowired
    private UserDao usersManager;
    @Autowired
    private PhotoDao photoManager;
    @Autowired
    private AlbumDao albumManager;

    /**
     * Метод подготавливает к выводу список фотографий пользователя,
     * не определенных ни в один альбом
     * 
     * @param request
     * @return
     */
    @RequestMapping(value = "/unallocatedPhotos.html", method = RequestMethod.GET)
    public ModelAndView showUnallocatedPhotosList( HttpServletRequest request ) {
        ModelAndView mav = new ModelAndView( "albumSimple" );

        User currentUser = this.usersManager.getUserFromSecurityContext();
        
        ArrayList<Photo> photos = (ArrayList<Photo>)this.photoManager.getUnallocatedPhotos( currentUser );
        logger.debug( "Unallocated photos count is "+ photos.size() );

        Album album = new Album();
        album.setUser( currentUser );
        album.setTitle( new RequestContext(request).getMessage( "page.title.unallocatedPhotos" ) );
        album.setDescription( new RequestContext(request).getMessage( "description.unallocatedPhotos" ) );
        album.setPhotos( photos );
        
        mav.addObject( "album", album );
        mav.addObject( "albums", albumManager.getAlbumsByUser( currentUser ) );
        mav.addObject( "isThisUser", true );
        mav.addObject( "pageName", new RequestContext(request).getMessage( "page.title.unallocatedPhotos" ) );

        return mav;
    }

    /**
     * Метод подготавливает к выводу список фотографий указанного альбома
     * 
     * @param albumId
     * @param request
     * @return
     */
    @RequestMapping(value = "/album_{albumId}.html", method = RequestMethod.GET)
    public ModelAndView showPhotosListByAlbum( @PathVariable("albumId") int albumId, HttpServletRequest request ) {
        Album album = this.albumManager.getAlbumById( albumId );
        ModelAndView mav = new ModelAndView( "album" );

        User currentUser = this.usersManager.getUserFromSecurityContext();
        if( currentUser != null )
            mav.addObject( "isThisUser", album.getUser().getUserId() == currentUser.getUserId() );
        
        mav.addObject( "album", album );
        mav.addObject( "albumUrl", "album_"+ albumId +"/" );
        mav.addObject( "albums", albumManager.getAlbumsByUser( currentUser ) );
        mav.addObject( "pageName", new RequestContext(request).getMessage( "page.title.viewAlbum" ) +" "+ album.getTitle() );

        return mav;
    }
}
