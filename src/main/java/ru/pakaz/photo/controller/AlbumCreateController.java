package ru.pakaz.photo.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContext;
import ru.pakaz.common.dao.UserDao;
import ru.pakaz.photo.dao.AlbumDao;
import ru.pakaz.photo.model.Album;

@Controller
public class AlbumCreateController {
    static private Logger logger = Logger.getLogger( AlbumCreateController.class );

    @Autowired
    private UserDao usersManager;
    @Autowired
    private AlbumDao albumManager;
    
    @RequestMapping(value = "/createAlbum.html", method = RequestMethod.GET)
    public ModelAndView get( HttpServletRequest request ) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName( "createAlbum" );
        mav.addObject( "album", new Album() );
        mav.addObject( "pageName", new RequestContext(request).getMessage( "page.title.createAlbum" ) );

        return mav;
    }

    @RequestMapping(value = "/createAlbum.html", method = RequestMethod.POST)  
    public ModelAndView post( @ModelAttribute("album") Album album, BindingResult result,
            HttpServletRequest request, HttpServletResponse response ) {

        ModelAndView mav = new ModelAndView();

        if( album.getTitle().length() > 0 ) {
            album.setUser( this.usersManager.getUserFromSecurityContext() );
            this.albumManager.createAlbum( album );

            mav.setViewName( "redirect:album_"+ album.getAlbumId() +".html" );
        }
        else {
            result.rejectValue( "title", "error.album.titleIsTooShort" );
            mav.setViewName( "createAlbum" );
            mav.addObject( "pageName", new RequestContext(request).getMessage( "page.title.createAlbum" ) );
        }
        return mav;
    }
}
