package ru.pakaz.photo.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContext;

import ru.pakaz.photo.dao.PhotoDao;
import ru.pakaz.photo.model.Photo;

@Controller
public class PhotoInfoController {
    static private Logger logger = Logger.getLogger( PhotoInfoController.class );

    @Autowired
    private PhotoDao photoManager;
    
    @RequestMapping(value = "/photo_{photoId}/info.html", method = RequestMethod.GET)
    public ModelAndView get( @PathVariable("photoId") int photoId, HttpServletRequest request ) {
    	Photo photo = this.photoManager.getPhotoById(photoId);

        ModelAndView mav = new ModelAndView();
        mav.setViewName( "photoInfo" );
        mav.addObject( "photo", photo );
        mav.addObject( "pageName", new RequestContext(request).getMessage( "page.title.photoInfo" ) +" "+ photo.getTitle() );

        return mav;
    }
    
    @RequestMapping(value = "/photo_{photoId}/delete.html", method = RequestMethod.GET)
    public void delete( @PathVariable("photoId") int photoId, HttpServletRequest request, HttpServletResponse response ) {
    	Photo photo = this.photoManager.getPhotoById(photoId);
    	if( photo != null ) {
    		photo.setDeleted(true);
    		this.photoManager.updatePhoto(photo);
    		logger.debug("Photo "+ photoId +" Deleted");
            
            try {
            	if( photo.getAlbum() != null )
            		response.sendRedirect(request.getContextPath() +"/album_"+ photo.getAlbum().getAlbumId() +".html");
            	else
            		response.sendRedirect(request.getContextPath() +"/unallocatedPhotos.html");
			} catch (IOException e) {
				logger.error("Error on sending redirect to the new album page!");
			}
    	}
    	else {
    		logger.debug("Photo "+ photoId +" does not exist");
    	}
    }

    @RequestMapping(value = "/photo_{photoId}/info.html", method = RequestMethod.POST)  
    public ModelAndView post( @PathVariable("photoId") int photoId, @ModelAttribute("photo") Photo photo, 
            BindingResult result, HttpServletRequest request, HttpServletResponse response ) {
        
    	Photo dbPhoto = this.photoManager.getPhotoById(photoId);
        
        if( photo.getTitle().length() > 0 ) {
        	dbPhoto.setTitle(photo.getTitle());
        	dbPhoto.setDescription(photo.getDescription());

            this.photoManager.updatePhoto(dbPhoto);
            
            try {
                response.sendRedirect(request.getContextPath() +"/photo_"+ photoId +".html");
            } catch (IOException e) {
                logger.error("Error on sending redirect to the updated photo page!");
            }
        }
        else {
            result.rejectValue( "title", "error.photo.titleIsTooShort" );
        }

        ModelAndView mav = new ModelAndView();
        mav.setViewName( "photoInfo" );
        mav.addObject( "pageName", new RequestContext(request).getMessage( "page.title.photoInfo" ) );
        return mav;
    }
}
