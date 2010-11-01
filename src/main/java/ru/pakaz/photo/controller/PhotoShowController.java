package ru.pakaz.photo.controller;

import java.util.List;
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
import ru.pakaz.photo.dao.PhotoDao;
import ru.pakaz.photo.model.Photo;

@Controller
public class PhotoShowController {
    private Logger logger = Logger.getLogger( PhotoShowController.class );

    @Autowired
    private UserDao usersManager;
    @Autowired
    private PhotoDao photoManager;
    
    /**
     * Страница, отображающая одну фотографию, названия, комментарии
     * 
     * @param photoId
     * @param request
     * @return
     */
    @RequestMapping(value="/photo_{photoId}.html", method=RequestMethod.GET)
    public ModelAndView viewPhoto( @PathVariable("photoId") int photoId, HttpServletRequest request ) {
        ModelAndView mav = new ModelAndView( "viewPhoto" );
        
        Photo prev    = null;
        Photo current = this.photoManager.getPhotoById( photoId );
        Photo next    = null;
        
        if( current != null ) {
            
            int currentPhotoNumber = 1;

            String title  = new RequestContext(request).getMessage( "page.title.viewPhoto" ) +" "+ current.getTitle();

            logger.debug( "Adding title string to the view: '"+ title +"'" );
            mav.addObject( "pageName", title );
            mav.addObject( "photo", current );

            /**
             * Getting full photos list to get links for previous and next photos
             */
            if( current.getAlbum() != null ) {
                List<Photo> albumPhotoList = current.getAlbum().getPhotos();

                for (int i = 0; i < albumPhotoList.size(); i++) {
                    if( albumPhotoList.get(i).getPhotoId() == current.getPhotoId() ) {
                        prev = i > 0 ? albumPhotoList.get(i - 1) : null;
                        next = albumPhotoList.size() > i + 1 ? albumPhotoList.get(i + 1) : null;

                        currentPhotoNumber = i + 1;
                        break;
                    }
                }
            }

            User currentUser = this.usersManager.getUserFromSecurityContext();
            if( currentUser != null )
                mav.addObject( "isThisUser", currentUser.getUserId() == current.getUser().getUserId() );

            mav.addObject( "currentPhotoNumber", currentPhotoNumber );
            mav.addObject( "prevPhoto", prev );
            mav.addObject( "nextPhoto", next );
        }
        else {
            // Если фотография не существует или удалена
            mav.setViewName( "photoNotFound" );
        }
        
        return mav;
    }
}
