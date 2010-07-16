package ru.pakaz.photo.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContext;

import ru.pakaz.photo.dao.AlbumDao;
import ru.pakaz.photo.model.Album;

@Controller
public class AlbumInfoController {
    static private Logger logger = Logger.getLogger( AlbumInfoController.class );

    @Autowired
    private AlbumDao albumManager;
    
    @RequestMapping(value = "/album_{albumId}/info.html", method = RequestMethod.GET)
    public ModelAndView get( @PathVariable("albumId") int albumId, HttpServletRequest request ) {
    	Album album = this.albumManager.getAlbumById( albumId );

        ModelAndView mav = new ModelAndView();
        mav.setViewName( "albumInfo" );
        mav.addObject( "album", album );
        mav.addObject( "pageName", new RequestContext(request).getMessage( "page.title.albumInfo" ) +" "+ album.getTitle() );

        return mav;
    }
    
    @RequestMapping(value = "/album_{albumId}/delete.html", method = RequestMethod.GET)
    public void delete( @PathVariable("albumId") int albumId, HttpServletRequest request ) {
    	Album album = this.albumManager.getAlbumById( albumId );
    	if( album != null ) {
    		album.setDeleted(true);
    		this.albumManager.updateAlbum(album);
    		logger.debug("Album "+ albumId +" Deleted");
    	}
    	else {
    		logger.debug("Album "+ albumId +" does not exist");
    	}
    }
}
